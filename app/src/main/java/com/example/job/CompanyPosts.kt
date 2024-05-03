package com.example.job

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query

class CompanyPosts : AppCompatActivity() {
    private lateinit var fabbtn: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_posts)
        fabbtn = findViewById(R.id.fabadd)
        recyclerView = findViewById(R.id.recycler_job_post_id)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fabbtn.setOnClickListener {
            startActivity(Intent(this, InsertJobPost::class.java))
        }
    }

    override fun onStart() {
        super.onStart()

        val mUser = FirebaseAuth.getInstance().currentUser
        val uId = mUser?.uid
        if (uId != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("jobposts").child(uId)
            val query: Query = databaseReference.orderByKey()

            val options = FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(query, Data::class.java)
                .setLifecycleOwner(this)
                .build()

            val adapter = object : FirebaseRecyclerAdapter<Data, MyViewHolder>(options) {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.job_post_item, parent, false)
                    return MyViewHolder(view)
                }

                override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: Data) {
                    holder.bind(model)
                }
            }

            recyclerView.adapter = adapter
        }
    }
}

data class Data(
    var title: String? = null,
    var date: String? = null,
    var description: String? = null,
    var skill: String? = null,
    var salary: String? = null
)

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(data: Data) {
        val title: TextView = itemView.findViewById(R.id.job_title)
        val date: TextView = itemView.findViewById(R.id.job_date)
        val description: TextView = itemView.findViewById(R.id.job_description)
        val skills: TextView = itemView.findViewById(R.id.job_skills)
        val salary: TextView = itemView.findViewById(R.id.job_salary)

        title.text = data.title
        date.text = data.date
        description.text = data.description
        skills.text = data.skill
        salary.text = data.salary
    }
}
