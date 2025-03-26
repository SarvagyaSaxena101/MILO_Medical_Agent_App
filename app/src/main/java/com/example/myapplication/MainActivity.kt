package com.example.myapplication

import android.content.ActivityNotFoundException
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private val SPEECH_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableEdgeToEdge()
        findViewById<ImageButton>(R.id.headphone).setOnClickListener {
            startSpeechRecognition()
        }
        findViewById<ImageButton>(R.id.profile).setOnClickListener {
            Intent(this, SettingsActivity::class.java).also {
                startActivity(it)
            }
        }
        findViewById<ImageButton>(R.id.optionOne).setOnClickListener {
            Intent(this, optionOneActivity::class.java).also {
                startActivity(it)
            }
        }
        findViewById<ImageButton>(R.id.optionTwo).setOnClickListener {
            Intent(this, OptionTwoActivity::class.java).also {
                startActivity(it)
            }
        }
        findViewById<ImageButton>(R.id.optionThree).setOnClickListener {
            Intent(this, optionThreeActivity::class.java).also {
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
                    "open settings" in command -> startActivity(Intent(this, SettingsActivity::class.java))
                    "scan" in command -> startActivity(Intent(this, OptionTwoActivity::class.java))
                    "emotional" in command -> startActivity(Intent(this, optionThreeActivity::class.java))
                    "profile" in command -> startActivity(Intent(this, ProfileActivity::class.java))
                    "find" in command -> startActivity(Intent(this, optionOneActivity::class.java))
                    "go home" in command -> Toast.makeText(this, "Already in Home", Toast.LENGTH_SHORT).show()
                    else -> Toast.makeText(this, "Command not recognized", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
