package com.example.monpokedex.data


import com.example.monpokedex.network.PokedexApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

/**
 * Dependency Injection container at the application level.
 */
interface AppContainer {
    val pokedexRepository: PokedexRepository
}

/**
 * Implementation for the Dependency Injection container at the application level.
 *
 * Variables are initialized lazily and the same instance is shared across the whole app.
 */
class DefaultAppContainer : AppContainer {
    private val baseUrl = "https://raw.githubusercontent.com/Josstoh/res508-qualite-dev-android/main/rest/"

    /**
     * Use the Retrofit builder to build a retrofit object using a kotlinx.serialization converter
     */
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    /**
     * Retrofit service object for creating api calls
     */
    private val retrofitService: PokedexApiService by lazy {
        retrofit.create(PokedexApiService::class.java)
    }

    /**
     * DI implementation for Pokedex repository
     */
    override val pokedexRepository: PokedexRepository by lazy {
        NetworkPokedexRepository(retrofitService)
    }
}
