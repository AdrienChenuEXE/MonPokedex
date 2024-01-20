package com.example.monpokedex.model

import kotlinx.serialization.Serializable

@Serializable
data class Evolution(
    val before: List<Int>,
    val after: List<Int>
)
