package com.example.job

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
//import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class EditCandidateProfile : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 71
    private val PERMISSION_REQUEST_CODE = 1001
    private var filePath: Uri? = null
    private lateinit var namet: EditText
    private lateinit var emailt: EditText
    private lateinit var locationt: EditText
    private lateinit var experiencet: EditText
    private lateinit var qualficationt: EditText
    private lateinit var skillst: EditText
    private lateinit var uploadt: Button
    private lateinit var savet: Button
    //  private lateinit var imgView : ImageView
    private val db = Firebase.firestore

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_candidate_profile)

        // Initialize views after setting content view
        namet = findViewById(R.id.name)
        emailt = findViewById(R.id.email)
        locationt = findViewById(R.id.location)
        experiencet = findViewById(R.id.experience)
        qualficationt = findViewById(R.id.qualification)
        skillst = findViewById(R.id.skill)
        //       uploadt = findViewById(R.id.uploadresume)
        savet = findViewById(R.id.save)
        //  imgView = findViewById(R.id.profileimage)

//        imgView.setOnClickListener {
//            chooseImage()
//        }


        savet.setOnClickListener {
            val name = namet.text.toString()
            val email = emailt.text.toString()
            val location= locationt.text.toString()
            val experience= experiencet.text.toString()
            val qualification = qualficationt.text.toString()
            val skills = skillst.text.toString()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailt.error = "Invalid email address"
                return@setOnClickListener
            }

            if (name.isEmpty()) {
                namet.error = "Company name cannot be empty"
                return@setOnClickListener
            }


            val userMap = hashMapOf(
                "name" to name,
                "email" to email,
                "location" to location,
                "experience" to experience,
                "qualification" to qualification,
                "skills" to skills,

                )

            val currentUser = FirebaseAuth.getInstance().currentUser
            currentUser?.uid?.let { userId ->
                db.collection("candidate profile").document(userId)
                    .set(userMap)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, CandidateProfile::class.java))
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

//    private fun chooseImage() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
//            != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
//                PERMISSION_REQUEST_CODE)
//        } else {
//            openFileChooser()
//        }
//    }
//
//    private fun openFileChooser() {
//        val intent = Intent()
//        intent.type = "*/*"
//        intent.action = Intent.ACTION_GET_CONTENT
//        startActivityForResult(Intent.createChooser(intent, "Select image"), PICK_IMAGE_REQUEST)
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int,
//                                            permissions: Array<String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            PERMISSION_REQUEST_CODE -> {
//                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                    openFileChooser()
//                } else {
//                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
//                }
//                return
//            }
//            else -> {
//                // Ignore all other requests.
//            }
//        }
//    }
//
//
//
//    @Deprecated("Deprecated in Java")
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if(requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
//            && data != null && data.data != null )
//        {
//            filePath = data.data
//            // Add this line
//
//            Glide.with(this)
//                .load(filePath)
//                .circleCrop()
//                .into(imgView)
//        }
//    }
//
//
//    private fun uploadImage() {
//        if(filePath != null)
//        {
//            val ref = FirebaseStorage.getInstance().reference.child("profile images/"+ UUID.randomUUID().toString())
//            ref.putFile(filePath!!)
//                .addOnSuccessListener {
//                    ref.downloadUrl.addOnSuccessListener {
//                        saveImageInfoToDB(it.toString())
//                    }
//                }
//                .addOnFailureListener { e ->
//                    Toast.makeText(applicationContext, "Failed "+e.message, Toast.LENGTH_SHORT).show()
//                }
//        }
//    }
//
//    private fun saveImageInfoToDB(imageUrl: String) {
//        val ref = FirebaseDatabase.getInstance().getReference("profile images").push()
//        ref.setValue(imageUrl)
//            .addOnCompleteListener {
//                Toast.makeText(applicationContext, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show()
//            }
//    }

}
