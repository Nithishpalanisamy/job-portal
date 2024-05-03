package com.example.job

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EditCompanyProfile : AppCompatActivity() {
    private lateinit var companyNameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var streamEditText: EditText
    private lateinit var locationEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var websiteEditText: EditText
    private lateinit var sizeEditText: EditText
    private lateinit var saveButton: Button
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_company_profile)

        // Initialize views after setting content view
        companyNameEditText = findViewById(R.id.companyName)
        descriptionEditText = findViewById(R.id.description)
        streamEditText = findViewById(R.id.stream)
        locationEditText = findViewById(R.id.companyLocation)
        emailEditText = findViewById(R.id.companyEmail)
        websiteEditText = findViewById(R.id.websitelink)
        sizeEditText = findViewById(R.id.size)
        saveButton = findViewById(R.id.saveButton)

        saveButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val companyName = companyNameEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val stream = streamEditText.text.toString()
            val location = locationEditText.text.toString()
            val website = websiteEditText.text.toString()
            val size = sizeEditText.text.toString()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailEditText.error = "Invalid email address"
                return@setOnClickListener
            }

            if (companyName.isEmpty()) {
                companyNameEditText.error = "Company name cannot be empty"
                return@setOnClickListener
            }

            if (stream.isEmpty()) {
                streamEditText.error = "Stream cannot be empty"
                return@setOnClickListener
            }

            val userMap = hashMapOf(
                "companyName" to companyName,
                "description" to description,
                "stream" to stream,
                "location" to location,
                "email" to email,
                "website" to website,
                "size" to size
            )

            val currentUser = FirebaseAuth.getInstance().currentUser
            currentUser?.uid?.let { userId ->
                db.collection("companyprofile").document(userId)
                    .set(userMap)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, CompanyProfile::class.java))
                        finish() // finish this activity after starting new one
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } ?: run {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
