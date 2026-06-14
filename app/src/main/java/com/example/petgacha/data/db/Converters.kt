package com.example.petgacha.data.db

import androidx.room.TypeConverter
import com.example.petgacha.data.model.Rarity

class Converters {
    @TypeConverter
    fun fromRarity(rarity: Rarity): String {
        return rarity.name
    }

    @TypeConverter
    fun toRarity(rarity: String): Rarity {
        return Rarity.valueOf(rarity)
    }
}
