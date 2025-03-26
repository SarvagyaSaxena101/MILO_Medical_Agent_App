package com.example.myapplication


import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class nearmeActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var findView: TextView
    private lateinit var findButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.nearme_activity)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        findView = findViewById(R.id.findview)
        findButton = findViewById<Button>(R.id.find)

        findButton.setOnClickListener {
            getCurrentLocation()
        }
        findViewById<ImageButton>(R.id.headphone).setOnClickListener {
            startSpeechRecognition()
        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                100
            )
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                fetchDoctors(location.latitude, location.longitude)  // Pass latitude & longitude
            } else {
                findView.text = "❌ Unable to get location. Try again."
            }
        }.addOnFailureListener { e ->
            findView.text = "❌ Location error: ${e.message}"
        }
    }


    private fun fetchDoctors(latitude: Double, longitude: Double) {
        val apiService = RetroFitClient.Client().create(DoctorApiService::class.java)

        val call = apiService.getDoctors(latitude, longitude) // Pass latitude & longitude as query params

        call.enqueue(object : Callback<DoctorResponse> {
            override fun onResponse(call: Call<DoctorResponse>, response: Response<DoctorResponse>) {
                if (response.isSuccessful && response.body()?.status == "success") {
                    val doctors = response.body()?.doctors
                    if (!doctors.isNullOrEmpty()) {
                        val doctorList = doctors.joinToString("\n\n") {
                            "👨‍⚕️ Dr. ${it.name}\n📍 Address: ${it.address}\n🩺 Specialties: ${it.category.joinToString(", ")}"
                        }
                        findView.text = doctorList  // Show the list in TextView
                    } else {
                        findView.text = "No doctors found nearby."
                    }
                } else {
                    findView.text = "❌ API Error: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<DoctorResponse>, t: Throwable) {
                findView.text = "❌ Failed to fetch doctors: ${t.message}"
            }
        })
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation()
        } else {
            findView.text = "❌ Location permission denied."
        }
    }

    private val SPEECH_REQUEST_CODE = 100
    private fun startSpeechRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Say a command...")
        }
        try {
            startActivityForResult(intent, SPEECH_REQUEST_CODE)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "Speech Recognition not supported", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            val matches = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            matches?.let {
                val command = it[0].lowercase()
                when {
                    "open settings" in command -> startActivity(Intent(this, SettingsActivity::class.java))
                    "back" in command -> finish()
                    "scan" in command -> startActivity(Intent(this, OptionTwoActivity::class.java))
                    "emotional" in command -> startActivity(Intent(this, optionThreeActivity::class.java))
                    "profile" in command -> startActivity(Intent(this, ProfileActivity::class.java))
                    "go home" in command -> startActivity(Intent(this, MainActivity::class.java))
                    "find" in command -> Toast.makeText(this, "Already in Home", Toast.LENGTH_SHORT).show()
                    else -> Toast.makeText(this, "Command not recognized", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
