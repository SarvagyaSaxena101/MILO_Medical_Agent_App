package com.example.myapplication

data class DataModels(
    val image: String
)

data class PredictionResponse(
    val prediction: String
)
//
//data class Doctor(
//    val name: String,
//    val specialization: String,
//    val location: String,
//    val contact: String
//)
data class DoctorResponse(
    val status: String,  // "success" or "error"
    val doctors: List<Doctor>
)

data class Doctor(
    val name: String,
    val address: String,
    val category: List<String>  // Specialties
)

data class LocationRequest(
    val lat: Double,
    val lng: Double
)

data class TherapistRequest(
    val user_input: String
)

data class TherapistResponse(
    val response: String
)

