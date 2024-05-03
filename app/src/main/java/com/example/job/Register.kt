package com.example.job

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
import java.util.regex.Pattern

class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        val auth = FirebaseAuth.getInstance()
        val registerButton = findViewById<Button>(R.id.registerButton)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val password = findViewById<EditText>(R.id.password)
        val email = findViewById<EditText>(R.id.email)
        val confirmpassword = findViewById<EditText>(R.id.confirm_password)

        registerButton.setOnClickListener {
            val user = email.text.toString().trim()
            val pass = password.text.toString().trim()
            val confirmPass = confirmpassword.text.toString().trim()

            if (user.isEmpty()) {
                email.error = "Email cannot be empty"
            } else if (!isValidEmail(user)) {
                email.error = "Please enter a valid email"
            } else if (pass.isEmpty()) {
                password.error = "Password cannot be empty"
            } else if (!isValidPassword(pass)) {
                password.error = "Invalid password"
            } else if (pass != confirmPass) {
                confirmpassword.error = "Passwords do not match"
            } else {
                auth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Signup successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, Login::class.java))
                    } else {
                        Toast.makeText(this, "Signup Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        loginButton.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        // Define a pattern to validate the password.
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"
        val pattern = Pattern.compile(passwordPattern)
        return pattern.matcher(password).matches()
    }


}