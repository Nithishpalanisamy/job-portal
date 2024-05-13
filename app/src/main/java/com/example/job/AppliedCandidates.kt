package com.example.job

import android.annotation.SuppressLint
import android.icu.text.CaseMap.Title
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query

class AppliedCandidates : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var databaseReference: DatabaseReference
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_applied_candidates)
        recyclerView = findViewById(R.id.recycler_applied_candidates)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
    override fun onStart() {
        super.onStart()
        try {
            val mUser = FirebaseAuth.getInstance().currentUser
            val uId = mUser?.uid
            if (uId != null) {
                databaseReference = FirebaseDatabase.getInstance().getReference("applied candidate").child(uId)
                val query: Query = databaseReference.orderByKey()

                val options = FirebaseRecyclerOptions.Builder<CandidateDetails>()
                    .setQuery(query, CandidateDetails::class.java)
                    .setLifecycleOwner(this)
                    .build()

                val adapter = object : FirebaseRecyclerAdapter<CandidateDetails, CandidateViewHolder>(options) {
                    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CandidateViewHolder {
                        val view = LayoutInflater.from(parent.context)
                            .inflate(R.layout.candidate_post_item, parent, false)
                        return CandidateViewHolder(view)
                    }

                    override fun onBindViewHolder(holder: CandidateViewHolder, position: Int, model: CandidateDetails) {
                        holder.bind(model)
                    }
                }

                recyclerView.adapter = adapter
                // Add toast message when data is successfully read
                Toast.makeText(this, "Data loaded successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "User is not signed in", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

//    override fun onStop() {
//        super.onStop()
//        if (recyclerView.adapter != null) {
//            (recyclerView.adapter as FirebaseRecyclerAdapter<*, *>).stopListening()
//        } else {
//            Toast.makeText(this, "Adapter is null", Toast.LENGTH_SHORT).show()
//        }
//    }
}

data class CandidateDetails(
    var title: String?=null,
    var name: String? = null,
    var email: String? = null,
    var location: String? = null,
    var experience: String? = null,
    var skill: String? = null,
    var qualification: String? = null
)

class CandidateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(data: CandidateDetails) {
        val title: TextView = itemView.findViewById(R.id.candidate_title)
        val name: TextView = itemView.findViewById(R.id.candidate_name)
        val location: TextView = itemView.findViewById(R.id.candidate_location)
        val qualification: TextView = itemView.findViewById(R.id.candidate_qualification)
        val  experience: TextView = itemView.findViewById(R.id.candidate_experience)
        val skills: TextView = itemView.findViewById(R.id.candidate_skills)
        val email: TextView = itemView.findViewById(R.id.candidate_email)

        title.text = data.title
        name.text = data.name
        location.text = data.location
        skills.text = data.skill
        qualification.text = data.qualification
        experience.text = data.experience
        email.text = data.email
    }
}
