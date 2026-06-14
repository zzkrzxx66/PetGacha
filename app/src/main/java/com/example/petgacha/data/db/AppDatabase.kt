package com.example.petgacha.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.petgacha.data.model.Pet
import com.example.petgacha.data.model.PlayerData
import com.example.petgacha.data.model.TeamPet

@Database(entities = [Pet::class, PlayerData::class, TeamPet::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun petDao(): PetDao
    abstract fun playerDao(): PlayerDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pet_gacha_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}