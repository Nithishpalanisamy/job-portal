package com.example.job

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CandidateHome : AppCompatActivity() {
    lateinit var profile: CardView
    lateinit var search: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_candidate_home)
        profile = findViewById(R.id.profile)
        search = findViewById(R.id.search)

        profile.setOnClickListener {
            val intent = Intent(this, CompanyProfile::class.java)
            startActivity(intent)
        }

        search.setOnClickListener{
            val intent = Intent(this,AllJobPosts::class.java)
            startActivity(intent)
        }
    }
}