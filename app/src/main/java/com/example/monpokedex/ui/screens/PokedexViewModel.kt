package com.example.monpokedex.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.monpokedex.PokedexApplication
import com.example.monpokedex.data.PokedexRepository
import com.example.monpokedex.model.Pokemon
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

/**
 * UI state for the Home screen
 */
sealed interface PokedexUiState {
    data class Success(val pokemons: List<Pokemon>) : PokedexUiState
    object Error : PokedexUiState
    object Loading : PokedexUiState
}


class PokedexViewModel(private val pokedexRepository: PokedexRepository) : ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    var pokedexUiState: PokedexUiState by mutableStateOf(PokedexUiState.Loading)
        private set

    /**
     * Call getPokemons() on init so we can display status immediately.
     */
    init {
        getPokemons()
    }

    /**
     * Gets Pokemon information from the Mars API Retrofit service and updates the
     * [Pokemon] [List] [MutableList].
     */
    fun getPokemons() {
        viewModelScope.launch {
            pokedexUiState = PokedexUiState.Loading
            pokedexUiState = try {
                PokedexUiState.Success(
                    pokedexRepository.getPokemons()
                )
            } catch (e: IOException) {
                PokedexUiState.Error
            } catch (e: HttpException) {
                PokedexUiState.Error
            }
        }
    }

    /**
     * Factory for [PokedexViewModel] that takes [PokedexRepository] as a dependency
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as PokedexApplication)
                val pokedexRepository = application.container.pokedexRepository
                PokedexViewModel(pokedexRepository = pokedexRepository)
            }
        }
    }
}
