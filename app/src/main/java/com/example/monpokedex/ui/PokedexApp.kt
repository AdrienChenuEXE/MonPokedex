@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.monpokedex.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.monpokedex.R
import com.example.monpokedex.Services.PokemonTypeService
import com.example.monpokedex.model.Pokemon
import com.example.monpokedex.ui.screens.HomeScreen
import com.example.monpokedex.ui.screens.PokedexViewModel
import com.example.monpokedex.ui.screens.PokemonDetails


enum class PokemonScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    PokemonDetails(title = R.string.pokemon_details)
}

@Composable
fun PokedexApp(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = PokemonScreen.valueOf(
        backStackEntry?.destination?.route ?: PokemonScreen.Start.name
    )

    /**
     *  Current Pokemon used tu be injected into PokemonDetailsScreen
     */
    var currentPokemon: Pokemon? by remember { mutableStateOf(null) }

    Scaffold(
        topBar = { PokedexTopAppBar(
            canNavigateBack = navController.previousBackStackEntry != null,
            navigateUp = { navController.navigateUp() }
        ) }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            val pokedexViewModel: PokedexViewModel =
                viewModel(factory = PokedexViewModel.Factory)
            NavHost(
                navController = navController,
                startDestination = PokemonScreen.Start.name,
//          modifier = Modifier.padding(innerPadding)
            ) {
                composable(route = PokemonScreen.Start.name) {
                    HomeScreen(
                        pokedexUiState = pokedexViewModel.pokedexUiState,
                        retryAction = pokedexViewModel::getPokemons,
                        /**
                         * If Pokemon Clicked, in the list, the pokemon is stored into currentPokemon variable
                         */
                        onPokemonClicked = {
                            currentPokemon = it
                            navController.navigate(PokemonScreen.PokemonDetails.name)
                        },
                    )
                }
                composable(route = PokemonScreen.PokemonDetails.name) {
                    PokemonDetails(
                        pokemon = currentPokemon!!,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(PokemonTypeService().getColorForType(currentPokemon!!.types[0]))
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokedexTopAppBar(
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back to Home"
                    )
                }
            }
        },
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineMedium,
                modifier = modifier
            )
        },
    )
}