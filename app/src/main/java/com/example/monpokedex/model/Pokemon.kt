package com.example.monpokedex.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Pokemon(
    val id: Int,
    val name: String,
    @SerialName(value = "type")
    val types: List<String>,
    val description: String,
    @SerialName(value = "image_url")
    val imgUrl: String,
    val evolutions: Evolution
)
