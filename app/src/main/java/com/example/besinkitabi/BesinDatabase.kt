package com.example.besinkitabi

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [BesinModel::class], version = 3,exportSchema = false)
abstract class BesinDatabase : RoomDatabase() {
    abstract fun besinDAO() : besinDAO
}