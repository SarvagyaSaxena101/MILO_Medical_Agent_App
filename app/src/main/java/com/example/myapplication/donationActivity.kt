package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject

class donationActivity : AppCompatActivity() {

    private lateinit var scrollViewContainer: LinearLayout
    private lateinit var bloodGroupEditText: EditText
    private lateinit var searchButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.blood_form)

        // Initialize views
        scrollViewContainer = findViewById(R.id.linearLayoutContainer)
        bloodGroupEditText = findViewById(R.id.bg)
        searchButton = findViewById(R.id.submit) // Button to trigger search

        // Set click listener to fetch and filter users dynamically
        searchButton.setOnClickListener {
            displayMatchingUsers(getDummyUsers())
        }
    }

    private fun getDummyUsers(): JSONArray {
        val usersArray = JSONArray()

        val dummyUsers = listOf(
            JSONObject().put("name", "John Doe").put("age", "28").put("bloodGroup", "B+"),
            JSONObject().put("name", "Alice Smith").put("age", "32").put("bloodGroup", "B+"),
            JSONObject().put("name", "Michael Brown").put("age", "24").put("bloodGroup", "A+"),
            JSONObject().put("name", "Emma Wilson").put("age", "30").put("bloodGroup", "A+"),
            JSONObject().put("name", "Daniel Johnson").put("age", "27").put("bloodGroup", "O+"),
            JSONObject().put("name", "Sophia Martinez").put("age", "29").put("bloodGroup", "O+")
        )

        dummyUsers.forEach { usersArray.put(it) }
        return usersArray
    }

    private fun displayMatchingUsers(usersArray: JSONArray) {
        scrollViewContainer.removeAllViews() // Clear previous data
        val userBloodGroup = bloodGroupEditText.text.toString().trim()

        if (userBloodGroup.isEmpty()) {
            Toast.makeText(this, "Please enter a blood group", Toast.LENGTH_SHORT).show()
            return
        }

        var found = false
        for (i in 0 until usersArray.length()) {
            val user = usersArray.getJSONObject(i)
            if (user.getString("bloodGroup").equals(userBloodGroup, ignoreCase = true)) {
                val userTextView = TextView(this)
                userTextView.text = "Name: ${user.getString("name")} | Age: ${user.getString("age")} | Blood Group: ${user.getString("bloodGroup")}"
                userTextView.textSize = 16f
                scrollViewContainer.addView(userTextView)
                found = true
            }
        }

        if (!found) {
            Toast.makeText(this, "No matching donors found", Toast.LENGTH_SHORT).show()
        }
    }
}
