package com.example.job

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import android.view.View
import com.google.firebase.auth.FirebaseAuth

open class MainActivity : AppCompatActivity() {
    lateinit var profile: CardView
    lateinit var post: CardView
    lateinit var logout: CardView
    lateinit var search: CardView

    // Firebase authentication instance
    private lateinit var mAuth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        profile = findViewById(R.id.profile)
        post = findViewById(R.id.post)
        search = findViewById(R.id.searchemployee)
        logout = findViewById(R.id.logout2)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()
        profile.setOnClickListener {
            val intent = Intent(this, CompanyProfile::class.java)
            startActivity(intent)
        }
        post.setOnClickListener {
            val intent = Intent(this, CompanyPosts::class.java)
            startActivity(intent)
        }
        search.setOnClickListener{
            val intent = Intent(this,AppliedCandidates::class.java)
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
