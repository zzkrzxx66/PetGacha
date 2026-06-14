package com.example.petgacha.util

import android.content.Context
import com.example.petgacha.data.model.Pet
import com.example.petgacha.data.model.Rarity
import org.json.JSONArray
import org.json.JSONObject

object PetDataLoader {
    fun loadPetsFromAssets(context: Context): List<Pet> {
        val pets = mutableListOf<Pet>()
        try {
            val jsonString = context.assets.open("pets.json").bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(jsonString)
            
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val pet = Pet(
                    id = jsonObject.getString("id"),
                    name = jsonObject.getString("name"),
                    rarity = Rarity.valueOf(jsonObject.getString("rarity")),
                    health = jsonObject.getInt("health"),
                    attack = jsonObject.getInt("attack"),
                    defense = jsonObject.getInt("defense"),
                    speed = jsonObject.getInt("speed"),
                    skillName = jsonObject.getString("skillName"),
                    skillDescription = jsonObject.getString("skillDescription"),
                    story = jsonObject.getString("story"),
                    imageFileName = jsonObject.getString("imageFileName"),
                    isCollected = false
                )
                pets.add(pet)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return pets
    }
}