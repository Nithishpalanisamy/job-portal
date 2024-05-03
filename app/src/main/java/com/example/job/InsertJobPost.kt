package com.example.job

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class InsertJobPost : AppCompatActivity() {
    private lateinit var jobtitle: EditText
    private lateinit var jobdescription: EditText
    private lateinit var jobskill: EditText
    private lateinit var jobsalary: EditText
    private lateinit var post: Button
    private val db = Firebase.database.reference
    private val DatabaseReference mPublicDatabase;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_job_post)

        jobtitle = findViewById(R.id.jobtitle)
        jobdescription = findViewById(R.id.jobdescription)
        jobskill = findViewById(R.id.skill)
        jobsalary = findViewById(R.id.salary)
        post = findViewById(R.id.postButton) // replace with your actual post button id
        mPublicDatabase=FirebaseDataBase.getInstance().getReference().child("public database");
        post.setOnClickListener {
            val title = jobtitle.text.toString().trim()
            val description = jobdescription.text.toString().trim()
            val skill = jobskill.text.toString().trim()
            val salary = jobsalary.text.toString().trim()

            if (title.isEmpty()) {
                jobtitle.error = "Job title cannot be empty"
                return@setOnClickListener
            }

            if (description.isEmpty()) {
                jobdescription.error = "Job description cannot be empty"
                return@setOnClickListener
            }

            if (skill.isEmpty()) {
                jobskill.error = "Job skill cannot be empty"
                return@setOnClickListener
            }

            if (salary.isEmpty()) {
                jobsalary.error = "Job salary cannot be empty"
                return@setOnClickListener
            }

            // Get current date
            val sdf = SimpleDateFormat("yyyy/MM/dd")
            val currentDate = sdf.format(Date())

            val jobpost = hashMapOf(
                "title" to title,
                "description" to description,
                "skill" to skill,
                "salary" to salary,
                "date" to currentDate
            )

            val currentUser = FirebaseAuth.getInstance().currentUser
            currentUser?.uid?.let { userId ->
                db.child("jobposts").child(userId).push()
                    .setValue(jobpost)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Job Posted Successfully", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, CompanyPosts::class.java))
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
