package com.example.besinkitabi

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity

data class BesinModel (
    @ColumnInfo(name = "isim")
    @SerializedName("isim")
    var Isim : String,

    @ColumnInfo(name = "kalori")
    @SerializedName("kalori")
    var kalori : String,

    @ColumnInfo(name = "karbonhidrat")
    @SerializedName("karbonhidrat")
    var karbonhidrat : String,

    @ColumnInfo(name = "protein")
    @SerializedName("protein")
    var protein : String,

    @ColumnInfo(name = "yag")
    @SerializedName("yag")
    var yag : String,

    @ColumnInfo(name = "gorsel")
    @SerializedName("gorsel")
    var gorsel : String,

    var gramMiktari: Int = 0 // Yeni eklenen alan

){
    @PrimaryKey(autoGenerate = true)
    var id = 0
}