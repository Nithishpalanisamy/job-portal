package com.example.job

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class JobDetails : AppCompatActivity() {

    private lateinit var mTitle: TextView
    private lateinit var mDate: TextView
    private lateinit var mDescription: TextView
    private lateinit var mSkills: TextView
    private lateinit var mSalary: TextView
    private lateinit var mEmail: TextView
    private lateinit var mContact: Button
    private lateinit var mApply: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_details)

        mTitle = findViewById(R.id.job_details_title)
        mDate = findViewById(R.id.job_details_date)
        mDescription = findViewById(R.id.job_details_description)
        mSkills = findViewById(R.id.job_details_skill)
        mSalary = findViewById(R.id.job_details_salary)
        mEmail = findViewById(R.id.job_details_email)
        mContact = findViewById(R.id.job_details_contact)
        mApply = findViewById(R.id.job_details_apply)

        // Receive data
        val intent = intent
        val id = intent.getStringExtra("uId")
        val title = intent.getStringExtra("title")
        val date = intent.getStringExtra("date")
        val description = intent.getStringExtra("description")
        val skills = intent.getStringExtra("skill")
        val salary = intent.getStringExtra("salary")
        val email = intent.getStringExtra("email")
        mTitle.text = title
        mDate.text = date
        mDescription.text = description
        mSkills.text = skills
        mSalary.text = salary
        mEmail.text = email

        val emailAddress = mEmail.text.toString()

        mContact.setOnClickListener {
            // Create an Intent to send email
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:$emailAddress") // Set recipient email address
                putExtra(Intent.EXTRA_SUBJECT, "Subject") // Set email subject
                putExtra(Intent.EXTRA_TEXT, "Message body") // Set email message body
            }

            // Verify if there's an app to handle the Intent
            if (emailIntent.resolveActivity(packageManager) != null) {
                startActivity(emailIntent) // Open email app
            } else {
                // If no app can handle the Intent, display a message
                Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show()
            }
        }

        mApply.setOnClickListener {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null && id != null) {
                // Reference to store under userId in 'applied' collection
                val appliedRef = FirebaseDatabase.getInstance().getReference("applied").child(userId)
                // Reference to store under jobId in 'applied candidate' collection
                val appliedCandidateRef = FirebaseDatabase.getInstance().getReference("applied candidate").child(id)

                // Retrieve user details from Firestore using userId
                val userRef = FirebaseFirestore.getInstance().collection("candidate profile").document(userId)
                userRef.get().addOnSuccessListener { document ->
                    if (document != null) {
                        val name = document.getString("name") ?: ""
                        val email = document.getString("email") ?: ""
                        val location = document.getString("location") ?: ""
                        val qualification = document.getString("qualification") ?: ""
                        val experience = document.getString("experience") ?: ""

                        // Create a map of user details
                        val userDetails = hashMapOf(
                            "name" to name,
                            "email" to email,
                            "location" to location,
                            "qualification" to qualification,
                            "experience" to experience,
                            "title" to title,
                            "date" to date,
                            "description" to description,
                            "skill" to skills,
                            "salary" to salary,
                            "email" to emailAddress
                        )

                        // Store user details in the 'applied' collection under the userId
                        appliedRef.setValue(userDetails)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Job applied successfully", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Failed to apply for job: ${e.message}", Toast.LENGTH_SHORT).show()
                            }

                        // Store user details in the 'applied candidate' collection under the jobId
                        appliedCandidateRef.setValue(userDetails)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Job applied successfully in applied candidate", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Failed to apply for job in applied candidate: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(this, "No such document", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(this, "Failed to get candidate details", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}
