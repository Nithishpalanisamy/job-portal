package com.example.job

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query

class AppliedJob : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var databaseReference: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_applied_job)
        recyclerView = findViewById(R.id.recycler_applied_job)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onStart() {
        super.onStart()
        try {
            val mUser = FirebaseAuth.getInstance().currentUser
            val uId = mUser?.uid
            if (uId != null) {
                databaseReference = FirebaseDatabase.getInstance().getReference("applied").child(uId)
                val query: Query = databaseReference.orderByKey()

                val options = FirebaseRecyclerOptions.Builder<Details>()
                    .setQuery(query, Details::class.java)
                    .setLifecycleOwner(this)
                    .build()

                val adapter = object : FirebaseRecyclerAdapter<Details, JobViewHolder>(options) {
                    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
                        val view = LayoutInflater.from(parent.context)
                            .inflate(R.layout.applied_jobs_details, parent, false)
                        return JobViewHolder(view)
                    }

                    override fun onBindViewHolder(holder: JobViewHolder, position: Int, model: Details) {
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

    override fun onStop() {
        super.onStop()
        if (recyclerView.adapter != null) {
            (recyclerView.adapter as FirebaseRecyclerAdapter<*, *>).stopListening()
        } else {
            Toast.makeText(this, "Adapter is null", Toast.LENGTH_SHORT).show()
        }
    }
}

data class Details(
    var title: String? = null,
    var date: String? = null,
    var description: String? = null,
    var email: String? = null,
    var skills: String? = null,
    var salary: String? = null,
    var experience: String? = null,
    var location: String? = null,
    var name: String? = null,
    var qualification: String? = null,
    var skill: String? = null
)


class JobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(data: Details) {
        val title: TextView = itemView.findViewById(R.id.job_title)
        val date: TextView = itemView.findViewById(R.id.job_date)
        val description: TextView = itemView.findViewById(R.id.job_description)
        val skills: TextView = itemView.findViewById(R.id.job_skills)
        val salary: TextView = itemView.findViewById(R.id.job_salary)
        val email: TextView = itemView.findViewById(R.id.job_email)

        title.text = data.title
        date.text = data.date
        description.text = data.description
        skills.text = data.skills
        salary.text = data.salary
        email.text = data.email
    }
}
