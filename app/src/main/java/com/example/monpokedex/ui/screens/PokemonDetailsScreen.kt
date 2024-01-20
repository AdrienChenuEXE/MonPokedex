package com.example.monpokedex.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.monpokedex.R
import com.example.monpokedex.Services.PokemonTypeService
import com.example.monpokedex.model.Pokemon


@Composable
fun PokemonDetailsScreen(
    pokemon: Pokemon,
    pokemonUiState: PokedexUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (pokemonUiState) {
        is PokedexUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is PokedexUiState.Success -> PokemonDetails(
            pokemon,
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .fillMaxHeight()
        )

        is PokedexUiState.Error -> ErrorScreen(retryAction, modifier = modifier.fillMaxSize())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetails(
    pokemon: Pokemon,
    modifier: Modifier = Modifier,
) {
    val typeColor : Color = PokemonTypeService().getColorForType(pokemon.types[0])
    val typeDarkerColor : Color = PokemonTypeService().getDarkerColorForType(pokemon.types[0])


    Column (
        modifier = Modifier
            .background(typeColor)
    ){
        // Ligne pour le nom et l'id du pokemon
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(vertical = 20.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(pokemon.name,
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                color = Color.White
            )
            Text(
                text = "#${pokemon.id.toString().padStart(3, '0')}",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = Color.White
            )
        }

        // Ligne pour les types du pokemon
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp)
        ) {
            pokemon.types.forEach { type ->
                val typeIcon : Int = PokemonTypeService().getImageForType(type)

                Row (
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .clip(shape = RoundedCornerShape(30.dp))
                        .background(Color(255, 255, 255, 80))
                ){
                    TypeCard(
                        type,
                        typeIcon
                    )
                }
            }
        }

        // Details du pokemon
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(top = 250.dp)
                .clip(shape = RoundedCornerShape(topEnd = 50.dp))
                .clip(shape = RoundedCornerShape(topStart = 50.dp))
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 75.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                ) {
                    // Details du pokemon
                    item {
                        Text(
                            text = "Description",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .padding(bottom = 15.dp)
                        )
                        Text(
                            text = pokemon.description,
                            textAlign = TextAlign.Justify
                        )
                        Text(
                            text = "Chaîne d'évolutions",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .padding(bottom = 15.dp)
                                .padding(top = 20.dp)
                        )

                        // Evolution précédentes
                        if (pokemon.evolutions.before.isNotEmpty()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                pokemon.evolutions.before.forEach { evo ->
                                    Card(
                                        modifier = Modifier
                                            .clip(shape = RoundedCornerShape(50.dp)),
                                    ) {
                                        Text(
                                            text = "n° $evo",
                                            modifier = Modifier
                                                .background(typeColor)
                                                .padding(5.dp)
                                                .padding(horizontal = 10.dp),
                                            color = Color.White
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.Default.ArrowForward,
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(context = LocalContext.current)
                                            .data(pokemon.imgUrl)
                                            .crossfade(true)
                                            .build(),
                                        error = painterResource(R.drawable.ic_broken_image),
                                        placeholder = painterResource(R.drawable.loading_img),
                                        contentDescription = stringResource(R.string.pokedex),
                                        modifier = Modifier
                                            .size(50.dp)
                                    )
                                    Text(text = pokemon.name)
                                }
                            }
                        }

                        // Evolution Suivantes
                        if (pokemon.evolutions.after.isNotEmpty()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 20.dp),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(context = LocalContext.current)
                                            .data(pokemon.imgUrl)
                                            .crossfade(true)
                                            .build(),
                                        error = painterResource(R.drawable.ic_broken_image),
                                        placeholder = painterResource(R.drawable.loading_img),
                                        contentDescription = stringResource(R.string.pokedex),
                                        modifier = Modifier
                                            .size(50.dp)
                                    )
                                    Text(text = pokemon.name)
                                }
                                pokemon.evolutions.after.forEach { evo ->
                                    Icon(
                                        imageVector = Icons.Default.ArrowForward,
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Card(
                                        modifier = Modifier
                                            .clip(shape = RoundedCornerShape(50.dp)),
                                    ) {
                                        Text(
                                            text = "n° $evo",
                                            modifier = Modifier
                                                .background(typeColor)
                                                .padding(5.dp)
                                                .padding(horizontal = 10.dp),
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                /*
                Text(
                    text = "Description",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(bottom = 15.dp)
                )
                Text(
                    text = pokemon.description,
                    textAlign = TextAlign.Justify
                )
                Text(
                    text = "Chaîne d'évolutions",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(bottom = 15.dp)
                        .padding(top = 20.dp)
                )

                // Evolution précédentes
                if (pokemon.evolutions.before.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        pokemon.evolutions.before.forEach { evo ->
                            Card(
                                modifier = Modifier
                                    .clip(shape = RoundedCornerShape(50.dp)),
                            ) {
                                Text(
                                    text = "$evo",
                                    modifier = Modifier
                                        .background(typeColor)
                                        .padding(5.dp)
                                        .padding(horizontal = 10.dp),
                                    color = Color.White
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(context = LocalContext.current)
                                    .data(pokemon.imgUrl)
                                    .crossfade(true)
                                    .build(),
                                error = painterResource(R.drawable.ic_broken_image),
                                placeholder = painterResource(R.drawable.loading_img),
                                contentDescription = stringResource(R.string.pokedex),
                                modifier = Modifier
                                    .size(50.dp)
                            )
                            Text(text = pokemon.name)
                        }
                    }
                }

                // Evolution Suivantes
                if (pokemon.evolutions.after.isNotEmpty()){
                    Row (
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(context = LocalContext.current)
                                    .data(pokemon.imgUrl)
                                    .crossfade(true)
                                    .build(),
                                error = painterResource(R.drawable.ic_broken_image),
                                placeholder = painterResource(R.drawable.loading_img),
                                contentDescription = stringResource(R.string.pokedex),
                                modifier = Modifier
                                    .size(50.dp)
                            )
                            Text(text = pokemon.name)
                        }
                        pokemon.evolutions.after.forEach { evo ->
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                            Card(
                                modifier = Modifier
                                    .clip(shape = RoundedCornerShape(50.dp)),
                            ){
                                Text(
                                    text = "$evo",
                                    modifier = Modifier
                                        .background(typeColor)
                                        .padding(5.dp)
                                        .padding(horizontal = 10.dp),
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
                */
            }
        }
    }

    // Image du pokemon
    Box(
        modifier = Modifier,
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 150.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(pokemon.imgUrl)
                    .crossfade(true)
                    .build(),
                error = painterResource(R.drawable.ic_broken_image),
                placeholder = painterResource(R.drawable.loading_img),
                contentDescription = stringResource(R.string.pokedex),
                modifier = Modifier
                    .size(300.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}