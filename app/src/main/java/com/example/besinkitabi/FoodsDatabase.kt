package com.example.besinkitabi

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [BesinModel::class], version = 2,exportSchema = false)
abstract class FoodsDatabase : RoomDatabase() {
    abstract fun foodDAO() : FoodsDAO
}