package com.example.job

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
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
        val email: String = "",
        val Id: String = ""  // Add this line
    )

    // RecyclerView and Firebase Database reference
    private lateinit var recyclerView: RecyclerView
    private lateinit var mAllJobPost: DatabaseReference
    private lateinit var search: EditText
    private lateinit var adapter: FirebaseRecyclerAdapter<JobPost, JobPostViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_job_posts)

        search = findViewById(R.id.job_search)

        // Initialize Firebase Database reference
        mAllJobPost = FirebaseDatabase.getInstance().getReference("Public database")
        mAllJobPost.keepSynced(true) // Keep the data synchronized

        recyclerView = findViewById(R.id.recycler_all_jobs)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true // Display items in reverse order
        layoutManager.stackFromEnd = true // Start stacking from the end

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager

        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                search(s.toString())
            }
        })
    }

    override fun onStart() {
        super.onStart()

        // Set up options for FirebaseRecyclerAdapter
        val options = FirebaseRecyclerOptions.Builder<JobPost>()
            .setQuery(mAllJobPost, JobPost::class.java)
            .build()

        // Create the adapter
        adapter = object : FirebaseRecyclerAdapter<JobPost, JobPostViewHolder>(options) {
            override fun onBindViewHolder(holder: JobPostViewHolder, position: Int, model: JobPost) {
                // Get the Firebase key of the item at the current position
                val key = getRef(position).key
                if (key != null) {
                    // Bind data to ViewHolder along with job ID
                    holder.bind(model, key)
                }
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
    }

    private fun search(string: String) {
        val query = mAllJobPost.orderByChild("title").startAt(string).endAt(string + "\uf8ff")
        val options = FirebaseRecyclerOptions.Builder<JobPost>()
            .setQuery(query, JobPost::class.java)
            .build()

        adapter.updateOptions(options)
    }

    // ViewHolder for job posts
    // ViewHolder for job posts
    class JobPostViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Bind data to views
        fun bind(jobPost: JobPost, jobId: String) {
            itemView.findViewById<TextView>(R.id.all_job_post_title).text = jobPost.title
            itemView.findViewById<TextView>(R.id.all_job_post_date).text = jobPost.date
            itemView.findViewById<TextView>(R.id.all_job_post_description).text = jobPost.description

            // Handle item click
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, JobDetails::class.java).apply {
                    putExtra("jobId", jobId) // Pass the job ID to JobDetails activity
                    putExtra("title", jobPost.title)
                    putExtra("date", jobPost.date)
                    putExtra("description", jobPost.description)
                    putExtra("skill", jobPost.skill)
                    putExtra("salary", jobPost.salary)
                    putExtra("email", jobPost.email)
                    putExtra("uId", jobPost.Id)  // Add this line
                }
                itemView.context.startActivity(intent)
            }
        }
    }

}
