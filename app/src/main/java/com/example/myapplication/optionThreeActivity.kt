package com.example.myapplication

import ApiService
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale

class optionThreeActivity: AppCompatActivity() {

    private lateinit var queryInput: EditText
    private lateinit var sendButton: Button
    private lateinit var responseText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.option_three_activity)

        queryInput = findViewById(R.id.Query)
        sendButton = findViewById(R.id.button3)
        responseText = findViewById(R.id.text_response)

        sendButton.setOnClickListener {
            val userInput = queryInput.text.toString().trim()
            if (userInput.isNotEmpty()) {
                sendQuery(userInput)
            } else {
                Toast.makeText(this, "Please enter your query", Toast.LENGTH_SHORT).show()
            }
        }
        findViewById<ImageButton>(R.id.headphone).setOnClickListener {
            startSpeechRecognition()
        }
    }

    private fun sendQuery(userInput: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://172.20.10.9:8000") // Replace with your Flask server IP
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService: ApiService = retrofit.create(ApiService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.sendTherapistQuery(TherapistRequest(userInput))
                if (response.isSuccessful) {
                    val responseData = response.body()
                    runOnUiThread {
                        responseText.text = responseData?.response ?: "No response received"
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@optionThreeActivity, "Error: ${response.message()}", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@optionThreeActivity, "Exception: ${e.message}", Toast.LENGTH_LONG).show()
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
                    "scan" in command -> startActivity(Intent(this, OptionTwoActivity::class.java))
                    "go home" in command -> startActivity(Intent(this, MainActivity::class.java))
                    "profile" in command -> startActivity(Intent(this, ProfileActivity::class.java))
                    "find" in command -> startActivity(Intent(this, optionOneActivity::class.java))
                    "back" in command -> finish()
                    "emotional" in command -> Toast.makeText(this, "Already in Emotional Help", Toast.LENGTH_SHORT).show()
                    else -> Toast.makeText(this, "Command not recognized", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
