package com.example.monpokedex.network

import com.example.monpokedex.model.Pokemon
import retrofit2.http.GET

/**
 * A public interface that exposes the [getPokemons] method
 */
interface PokedexApiService {
    /**
     * Returns a [List] of [Pokemon] and this method can be called from a Coroutine.
     * The @GET annotation indicates that the "pokemons" endpoint will be requested with the GET
     * HTTP method
     */
    @GET("pokemons.json")
    suspend fun getPokemons(): List<Pokemon>
}