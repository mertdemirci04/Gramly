package com.example.besinkitabi

import androidx.room.*

@Dao
interface WeightDAO {
    @Insert
    suspend fun insert(weight: Weight)

    @Query("SELECT * FROM weights ORDER BY timestamp ASC")
    suspend fun getAll(): List<Weight>

    @Query("DELETE FROM weights")
    suspend fun deleteAll()
}
