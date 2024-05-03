package com.example.job

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

open class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val auth = FirebaseAuth.getInstance()
        val loginemail = findViewById<EditText>(R.id.mail)
        val password = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerButton = findViewById<Button>(R.id.registerButton)

        loginButton.setOnClickListener {
            val email = loginemail.text.toString()
            val pass = password.text.toString()

            if (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                if (pass.isNotEmpty() && isValidPassword(pass)) {
                    auth.signInWithEmailAndPassword(email, pass)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, LaunchPage::class.java))
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    password.error = "Password cannot be empty"
                }
            } else if (email.isEmpty()) {
                loginemail.error = "Email cannot be empty"
            } else {
                loginemail.error = "Please enter valid email"
            }
        }

        registerButton.setOnClickListener {
            val k = Intent(this, Register::class.java)
            startActivity(k)
        }
    }

    open fun isValidPassword(password: String): Boolean {
        // You can replace this with your own password validation logic
        return password.length >= 6
    }
}