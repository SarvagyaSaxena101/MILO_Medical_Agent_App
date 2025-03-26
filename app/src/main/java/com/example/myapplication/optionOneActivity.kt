package com.example.myapplication

import android.content.ActivityNotFoundException
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class optionOneActivity: AppCompatActivity() {
    private val SPEECH_REQUEST_CODE = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.option_one_activity)

        findViewById<ImageButton>(R.id.headphone).setOnClickListener {
            startSpeechRecognition()
        }
        findViewById<ImageButton>(R.id.blood).setOnClickListener {
            Intent(this, donationActivity::class.java).also {
                startActivity(it)
            }
        }
        findViewById<ImageButton>(R.id.nearme).setOnClickListener {
            Intent(this, nearmeActivity::class.java ).also {
                startActivity(it)
            }
        }
        findViewById<ImageButton>(R.id.medicine).setOnClickListener {
            Intent(this, medicineActivity::class.java).also {
                startActivity(it)
            }
        }




    }
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
                    "doctors near me" in command -> startActivity(Intent(this,nearmeActivity::class.java))
                    "medicine" in command -> startActivity(Intent(this,medicineActivity::class.java))
                    "blood donation" in command -> startActivity(Intent(this, donationActivity::class.java))
                    "profile" in command -> startActivity(Intent(this, ProfileActivity::class.java))
                    "go home" in command -> startActivity(Intent(this, MainActivity::class.java))
                    "find" in command -> Toast.makeText(this, "Already in find", Toast.LENGTH_SHORT).show()
                    else -> Toast.makeText(this, "Command not recognized", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}