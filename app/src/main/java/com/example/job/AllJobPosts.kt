package com.example.job

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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AllJobPosts : AppCompatActivity() {
    // Define a data class to represent job post information
    data class JobPost(
        val title: String = "",
        val description: String = "",
        val salary: String = "",
        val skill: String = "",
        val date: String = "",
    )

    // RecyclerView and Firebase Database reference
    private lateinit var recyclerView: RecyclerView
    private lateinit var mAllJobPost: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_job_posts)

        // Initialize Firebase Database reference
        mAllJobPost = FirebaseDatabase.getInstance().getReference("jobposts")
        mAllJobPost.keepSynced(true) // Keep the data synchronized

        recyclerView = findViewById(R.id.recycler_all_jobs)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true // Display items in reverse order
        layoutManager.stackFromEnd = true // Start stacking from the end

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager
    }

    override fun onStart() {
        super.onStart()

        // Set up options for FirebaseRecyclerAdapter
        val options = FirebaseRecyclerOptions.Builder<JobPost>()
            .setQuery(mAllJobPost, JobPost::class.java)
            .build()

        // Create the adapter
        val adapter = object : FirebaseRecyclerAdapter<JobPost, JobPostViewHolder>(options) {
            override fun onBindViewHolder(holder: JobPostViewHolder, position: Int, model: JobPost) {
                // Bind data to ViewHolder
                holder.bind(model)
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobPostViewHolder {
                // Inflate the layout for each item
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.alljobpost, parent, false)
                return JobPostViewHolder(view)
            }
        }

        recyclerView.adapter = adapter
        adapter.startListening() // Start listening for changes in the data

        // Add a ValueEventListener to display a toast message when data is read
        mAllJobPost.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Toast.makeText(this@AllJobPosts, "Data read from Firebase", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@AllJobPosts, "Error reading data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // ViewHolder for job posts
    class JobPostViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Bind data to views
        fun bind(jobPost: JobPost) {
            itemView.findViewById<TextView>(R.id.all_job_post_title).text = jobPost.title
            itemView.findViewById<TextView>(R.id.all_job_post_date).text = jobPost.date
            itemView.findViewById<TextView>(R.id.all_job_post_description).text = jobPost.description
            itemView.findViewById<TextView>(R.id.all_job_post_skill).text = jobPost.skill
            itemView.findViewById<TextView>(R.id.all_job_post_salary).text = jobPost.salary
        }
    }
}
