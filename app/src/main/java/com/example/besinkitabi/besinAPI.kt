package com.example.besinkitabi

import retrofit2.http.GET

interface besinAPI {
    @GET("atilsamancioglu/BTK20-JSONVeriSeti/master/besinler.json")
    suspend fun getBesin() : ArrayList<BesinModel>
}