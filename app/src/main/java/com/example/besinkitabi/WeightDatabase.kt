package com.example.besinkitabi

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Weight::class], version = 1)
abstract class WeightDatabase : RoomDatabase() {
    abstract fun weightDao(): WeightDAO
}
