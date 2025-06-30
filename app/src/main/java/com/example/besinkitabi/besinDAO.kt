package com.example.besinkitabi

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao
interface besinDAO {
    @Query("SELECT * FROM BesinModel")
    fun getAllData() : List<BesinModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(besinListesi: List<BesinModel>): List<Long>


    @Query("SELECT * FROM BesinModel WHERE `id` = :id")
    suspend fun findById(id : Int) : BesinModel

    @Update
    suspend fun update(besin: BesinModel)


}