package com.example.besinkitabi

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao
interface FoodsDAO {
    @Query("SELECT * FROM BesinModel")
    fun getAllData() : List<BesinModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(besinListesi: BesinModel): Long


    @Query("SELECT * FROM BesinModel WHERE `id` = :id")
    suspend fun findById(id : Int) : BesinModel

    @Update
    suspend fun update(besin: BesinModel)

    @Query("DELETE FROM BesinModel")
    suspend fun deleteAllFoods()

    @Delete
    suspend fun deleteFood(besin: BesinModel)

}