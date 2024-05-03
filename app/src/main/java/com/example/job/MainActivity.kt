package com.example.job

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import android.view.View

open class MainActivity : AppCompatActivity() {
    lateinit var profile: CardView
    lateinit var post: CardView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        profile = findViewById(R.id.profile)
        post = findViewById(R.id.post)

        profile.setOnClickListener {
            val intent = Intent(this, CompanyProfile::class.java)
            startActivity(intent)
        }
        post.setOnClickListener {
            val intent = Intent(this, CompanyPosts::class.java)
            startActivity(intent)
        }


    }
}
