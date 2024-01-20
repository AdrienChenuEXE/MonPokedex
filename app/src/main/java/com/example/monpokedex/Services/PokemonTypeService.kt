package com.example.monpokedex.Services

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.monpokedex.R

class PokemonTypeService() {

    fun getImageForType(type: String): Int {
        return when (type.lowercase()) {
            "feu" -> R.drawable.feu_icon
            "vol" -> R.drawable.vol_icon
            "normal" -> R.drawable.normal_icon
            "eau" -> R.drawable.eau_icon
            "insecte" -> R.drawable.insect_icon
            "plante" -> R.drawable.plante_icon
            "poison" -> R.drawable.poison_icon
            else -> R.drawable.ic_broken_image
        }
    }

    fun getColorForType(type: String): Color {
        return when (type.lowercase()) {
            "feu" -> Color(251,108,107,255)
            //"vol" -> Color(0xFF0228E8)
            "normal" -> Color(182,189,196)
            "eau" -> Color(118,189,254,255)
            "insecte" -> Color(250, 215, 117, 255)
            "plante" -> Color(72,208,176,255)
            //"poison" -> Color(0xFFE8022F)
            else -> Color.Gray
        }
    }

    fun getDarkerColorForType(type: String): Color {
        return when (type.lowercase()) {
            "feu" -> Color(185,81,80,255)
            "vol" -> Color(0xFF0228E8)
            "normal" -> Color(127, 132, 138, 255)
            "eau" -> Color(60, 129, 192, 255)
            "insecte" -> Color(213, 179, 85, 255)
            "plante" -> Color(55,153,131,255)
            "poison" -> Color(0xFFE8022F)
            else -> Color.Gray
        }
    }
}