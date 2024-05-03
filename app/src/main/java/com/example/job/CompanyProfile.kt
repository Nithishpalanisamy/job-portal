package com.example.job

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class CompanyProfile : AppCompatActivity() {
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    data class CompanyProfileModel(
        val companyName: String = "",
        val description: String = "",
        val stream: String = "",
        val location: String = "",
        val email: String = "",
        val website: String? = null,
        val size: String = ""
    )

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_company_profile)

        val add = findViewById<Button>(R.id.addButton)
        val companyNameTextView = findViewById<TextView>(R.id.companyName)
        val descriptionTextView = findViewById<TextView>(R.id.description)
        val streamTextView = findViewById<TextView>(R.id.stream)
        val locationTextView = findViewById<TextView>(R.id.companylocation)
        val emailTextView = findViewById<TextView>(R.id.companyEmail)
        val websiteTextView = findViewById<TextView>(R.id.websitelink)
        val sizeTextView = findViewById<TextView>(R.id.size)

        add.setOnClickListener {
            startActivity(Intent(this, EditCompanyProfile::class.java))
        }

        // Fetch data from Firestore
        val userId = auth.currentUser?.uid // Get the ID of the currently logged in user
        if (userId != null) {
            db.collection("companyprofile").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val companyProfile = document.toObject<CompanyProfileModel>()
                        // Populate UI with retrieved data
                        companyProfile?.let {
                            companyNameTextView.text = it.companyName
                            descriptionTextView.text = it.description
                            streamTextView.text = it.stream
                            locationTextView.text = it.location
                            emailTextView.text = it.email
                            websiteTextView.text = it.website
                            sizeTextView.text = it.size
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
