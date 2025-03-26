package com.example.myapplication

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface DoctorApiService {
    @GET("/getdoctors")
    fun getDoctors(
        @Query("lat") latitude: Double,
        @Query("lng") longitude: Double
    ): Call<DoctorResponse>  // Ensure this returns DoctorResponse, not List<Doctor>
}

