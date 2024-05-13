package com.example.job


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth

class CandidateHome : AppCompatActivity() {
    lateinit var profile: CardView
    lateinit var search: CardView
    lateinit var logout: CardView
    lateinit var applied: CardView



    // Firebase authentication instance
    private lateinit var mAuth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_candidate_home)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        profile = findViewById(R.id.profile)
        search = findViewById(R.id.search)
        logout = findViewById(R.id.logout)
        applied = findViewById(R.id.appliedjob)

        profile.setOnClickListener {
            val intent = Intent(this, CandidateProfile::class.java)
            startActivity(intent)
        }

        search.setOnClickListener{
            val intent = Intent(this, AllJobPosts::class.java)
            startActivity(intent)
        }

        applied.setOnClickListener{
            val intent = Intent(this,AppliedJob::class.java)
            startActivity(intent)
        }

        logout.setOnClickListener{
            logout()
        }
    }

    private fun logout() {
        mAuth.signOut()
        // Redirect to login screen
        val intent = Intent(this, Login::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}
