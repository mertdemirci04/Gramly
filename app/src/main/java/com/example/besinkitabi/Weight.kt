package com.example.besinkitabi

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weights")
data class Weight(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val value: Float,
    val timestamp: Long = System.currentTimeMillis()
)
