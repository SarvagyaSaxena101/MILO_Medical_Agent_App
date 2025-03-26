    import com.example.myapplication.DataModels
    import com.example.myapplication.Doctor
    import com.example.myapplication.PredictionResponse
    import com.example.myapplication.TherapistRequest
    import com.example.myapplication.TherapistResponse
    import retrofit2.Response
    import retrofit2.http.Body
    import retrofit2.http.POST

    import retrofit2.Call
    import retrofit2.http.GET
    import retrofit2.http.Query


    interface ApiService {
        @POST("/therapist")
        suspend fun sendTherapistQuery(@Body request: TherapistRequest): Response<TherapistResponse>
        @GET("/getdoctors")
        fun getDoctors(
            @Query("lat") latitude: Double,
            @Query("lon") longitude: Double
        ): Call<List<Doctor>>
        @POST("/predict/bone_fracture")
        suspend fun predictBoneFracture(@Body request: DataModels): Response<PredictionResponse>

        @POST("/predict/dementia")
        suspend fun predictDementia(@Body request: DataModels): Response<PredictionResponse>

        @POST("/predict/diabetic_retinopathy")
        suspend fun predictDiabetes(@Body request: DataModels): Response<PredictionResponse>

        @POST("/predict/pneumonia")
        suspend fun predictPneumonia(@Body request: DataModels): Response<PredictionResponse>
    }
