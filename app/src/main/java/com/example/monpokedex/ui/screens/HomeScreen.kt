package com.example.monpokedex.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.monpokedex.R
import com.example.monpokedex.Services.PokemonTypeService
import com.example.monpokedex.model.Pokemon
import com.example.monpokedex.ui.theme.MonPokedexTheme

@Composable
fun HomeScreen(
    pokedexUiState: PokedexUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onPokemonClicked: (Pokemon) -> Unit
) {
    when (pokedexUiState) {
        is PokedexUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is PokedexUiState.Success -> PokemonsGridScreen(
            pokedexUiState.pokemons,
            modifier = modifier
                .fillMaxWidth(),
            onPokemonClicked = onPokemonClicked
        )
        is PokedexUiState.Error -> ErrorScreen(retryAction, modifier = modifier.fillMaxSize())
    }
}

/**
 * The home screen displaying the loading message.
 */
@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

/**
 * The home screen displaying error message with re-attempt button.
 */
@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

/**
 * The home screen displaying Pokemon grid.
 */
@Composable
fun PokemonsGridScreen(
    pokemons: List<Pokemon>,
    modifier: Modifier = Modifier,
    onPokemonClicked: (Pokemon) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(20.dp)
    ) {
        items(items = pokemons, key = { pokemon -> pokemon.id }) { pokemon ->
            PokemonCard(
                pokemon,
                modifier = modifier
                    .fillMaxWidth()
                    .aspectRatio(1.5f),
                onPokemonClicked = {
                    onPokemonClicked(it)
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonCard(
    pokemon: Pokemon,
    modifier: Modifier = Modifier,
    onPokemonClicked: (Pokemon) -> Unit,
) {
    val typeColor : Color = PokemonTypeService().getColorForType(pokemon.types[0])
    val typeDarkerColor : Color = PokemonTypeService().getDarkerColorForType(pokemon.types[0])

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        onClick = { onPokemonClicked(pokemon) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(typeColor)
        ) {
            // Row for Pokemon Id display to the right
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(end = 20.dp)
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "#${pokemon.id.toString().padStart(3, '0')}",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 30.sp,
                    color = typeDarkerColor
                )
            }

            // Row for Pokemon Name display to the left
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(pokemon.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp,
                    color = Color.White
                )
            }

            // Row with to columns
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left column for Pokemon types
                Column(
                    modifier = Modifier
                        .padding(top = 20.dp)
                ) {
                    pokemon.types.forEach { type ->
                        val typeIcon : Int = PokemonTypeService().getImageForType(type)

                        Row (
                            modifier = Modifier
                                .padding(bottom = 10.dp)
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

                // Right Column display Pokemon Image
                Column {
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(pokemon.imgUrl)
                            .crossfade(true)
                            .build(),
                        error = painterResource(R.drawable.ic_broken_image),
                        placeholder = painterResource(R.drawable.loading_img),
                        contentDescription = stringResource(R.string.pokedex),
                        modifier = Modifier
                            .size(150.dp)
                            .clip(MaterialTheme.shapes.large)
                            //.background(MaterialTheme.colorScheme.secondary)
                    )
                }
            }
        }
    }
}

/**
 * Personalized composable for each Pokemon type with the name and the icon of the type
 */
@Composable
fun TypeCard(
    type: String,
    icon: Int
) {

    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(10.dp)
            .padding(horizontal = 10.dp)
    ){
        // Icône représentant le type
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier
                .size(30.dp)
                .clip(MaterialTheme.shapes.medium)
        )
        Row(
            modifier = Modifier
                .padding(start = 15.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                type,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    MonPokedexTheme {
        LoadingScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorScreenPreview() {
    MonPokedexTheme {
        ErrorScreen({})
    }
}