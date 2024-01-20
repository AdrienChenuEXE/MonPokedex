package com.example.monpokedex.data

import com.example.monpokedex.model.Pokemon
import com.example.monpokedex.network.PokedexApiService

/**
 * Repository that fetch pokemons list from Api.
 */
interface PokedexRepository {
    /** Fetches list of Pokemon from Api */
    suspend fun getPokemons(): List<Pokemon>
}

/**
 * Network Implementation of Repository that fetch pokemon list from Api.
 */
class NetworkPokedexRepository(
    private val pokedexApiService: PokedexApiService
) : PokedexRepository {
    /** Fetches list of Pokemons from Api */
    override suspend fun getPokemons(): List<Pokemon> = pokedexApiService.getPokemons()
}
