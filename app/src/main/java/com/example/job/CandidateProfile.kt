package com.example.job

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class CandidateProfile : AppCompatActivity() {
    private val db = Firebase.firestore
    private val auth = Firebase.auth

    data class CandidateProfileModel(
        val name: String = "",
        val email: String = "",
        val location: String = "",
        val experience: String = "",
        val qualification: String = "",
        val skills: String? = null,

        )

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_candidate_profile)

        val add = findViewById<Button>(R.id.addButton)
        val candidateNameTextView = findViewById<TextView>(R.id.candidateName)
        val emailTextView = findViewById<TextView>(R.id.candidateEmail)
        val locationTextView = findViewById<TextView>(R.id.candidateLocation)
        val experienceTextView = findViewById<TextView>(R.id.candidateExperience)
        val qualificationTextview = findViewById<TextView>(R.id.candidateQualification)
        val skillsTextView = findViewById<TextView>(R.id.candidateSkills)

        add.setOnClickListener {
            startActivity(Intent(this, EditCandidateProfile::class.java))
        }

        // Fetch data from Firestore
        val userId = auth.currentUser?.uid // Get the ID of the currently logged in user
        if (userId != null) {
            db.collection("candidate profile").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val companyProfile = document.toObject<CandidateProfileModel>()
                        // Populate UI with retrieved data
                        companyProfile?.let {
                            candidateNameTextView.text = it.name
                            experienceTextView.text = it.experience
                            qualificationTextview.text = it.qualification
                            locationTextView.text = it.location
                            emailTextView.text = it.email
                            skillsTextView.text = it.skills
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
