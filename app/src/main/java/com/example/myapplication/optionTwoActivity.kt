package com.example.myapplication

import ApiService

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.speech.RecognizerIntent
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.util.Locale

class OptionTwoActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var selectImageButton: Button
    private lateinit var predictButton: Button
    private var imageUri: Uri? = null

    private val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://172.20.10.9:8000") // Change this to your API URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    private var selectedApi: String = "/predict/bone_fracture" // Change dynamically if needed

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.option_two_activity)

        imageView = findViewById(R.id.imageViewUni)
        selectImageButton = findViewById(R.id.uploadButton)
        predictButton = findViewById(R.id.submitButton)

        selectImageButton.setOnClickListener {
            Log.d("DEBUG", "Select Image button clicked")
            checkPermissionsAndOpenGallery()
        }

        predictButton.setOnClickListener {
            submitImageToApi()
        }
        findViewById<ImageButton>(R.id.headphone).setOnClickListener {
            startSpeechRecognition()
        }

        val radioGroup: RadioGroup = findViewById(R.id.radioGroup)
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            selectedApi = when (checkedId) {
                R.id.bone -> "/predict/bone_fracture"
                R.id.mri -> "/predict/mri_scan"
                R.id.dbtr -> "/predict/diabetes"
                R.id.chestxray -> "/predict/pneumonia"
                R.id.alzheimer -> "/predict/dementia"
                else -> "/predict/bone_fracture"
            }
        }

    }

    // Image Picker Launcher
    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                imageUri = data?.data
                imageView.setImageURI(imageUri)
                Log.d("DEBUG", "Image selected: $imageUri")
            } else {
                Log.e("DEBUG", "Image selection failed")
            }
        }

    // Open Gallery
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePickerLauncher.launch(intent)
    }

    // Check Permissions and Open Gallery
    private fun checkPermissionsAndOpenGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.READ_MEDIA_IMAGES), 100
                )
            } else {
                openGallery()
            }
        } else {
            openGallery()
        }
    }

    // Convert Image to Base64 and Send to API
    private fun submitImageToApi() {
        val drawable = imageView.drawable
        if (drawable !is BitmapDrawable) {
            Toast.makeText(this, "Please select a valid image.", Toast.LENGTH_SHORT).show()
            return
        }

        val bitmap = drawable.bitmap
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val base64Image = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)

        val requestData = DataModels(image = base64Image)

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = when (selectedApi) {
                    "/predict/bone_fracture" -> apiService.predictBoneFracture(requestData)
                    "/predict/diabetes" -> apiService.predictDiabetes(requestData)
                    "/predict/pneumonia" -> apiService.predictPneumonia(requestData)
                    "/predict/dementia" -> apiService.predictDementia(requestData)
                    else -> null
                }

                withContext(Dispatchers.Main) {
                    if (response != null && response.isSuccessful) {
                        response.body()?.let { predictionResponse ->
                            Toast.makeText(
                                this@OptionTwoActivity,
                                "Prediction: ${predictionResponse.prediction}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        Log.d("API_CALL","${response?.errorBody()?.string()}")
                        Toast.makeText(this@OptionTwoActivity, "Failed: ${response?.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d("API_CALL","${e.message}")
                    Toast.makeText(this@OptionTwoActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
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
