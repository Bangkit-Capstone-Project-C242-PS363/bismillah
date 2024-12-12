package com.adira.signmaster.ui.profile.history_study

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.adira.signmaster.R
import com.adira.signmaster.data.room.dao.StudyDao
import com.adira.signmaster.data.room.database.StudyDatabase
import com.adira.signmaster.databinding.ActivityStudyHistoryBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StudyHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudyHistoryBinding
    private lateinit var adapter: StudyHistoryAdapter
    private lateinit var studyDao: StudyDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudyHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        studyDao = StudyDatabase.getInstance(this).studyDao()

        setupRecyclerView()
        loadStudyHistory()

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        adapter = StudyHistoryAdapter()
        binding.rvStudyHistory.layoutManager = LinearLayoutManager(this)
        binding.rvStudyHistory.adapter = adapter
    }

    private fun loadStudyHistory() {
        CoroutineScope(Dispatchers.IO).launch {
            val studyHistory = studyDao.getAllStudies() // You need to add this query in StudyDao
            withContext(Dispatchers.Main) {
                if (studyHistory.isNotEmpty()) {
                    adapter.submitList(studyHistory)
                } else {
                    Toast.makeText(this@StudyHistoryActivity, "No saved studies.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}

