package com.example.job

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LaunchPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch_page)
        val seeker = findViewById<Button>(R.id.butt1)
        val employee = findViewById<Button>(R.id.butt2)
        seeker.setOnClickListener {
            startActivity(Intent(this, CandidateHome::class.java))
        }
        employee.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
}