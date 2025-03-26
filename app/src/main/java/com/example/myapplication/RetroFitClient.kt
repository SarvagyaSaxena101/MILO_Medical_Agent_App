package com.example.myapplication

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroFitClient {
    fun Client(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://172.20.10.9:8000") // Replace with your Flask server IP
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
