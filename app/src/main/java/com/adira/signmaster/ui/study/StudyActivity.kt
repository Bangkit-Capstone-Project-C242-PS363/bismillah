package com.adira.signmaster.ui.study

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.adira.signmaster.R
import com.adira.signmaster.databinding.ActivityStudyBinding

class StudyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudyBinding
    private val studyViewModel: StudyViewModel by viewModels()
    private lateinit var learnMaterialAdapter: LearnMaterialAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        // Menginisialisasi RecyclerView
        learnMaterialAdapter = LearnMaterialAdapter(emptyList())
        binding.rvLearnMaterial.layoutManager = LinearLayoutManager(this)
        binding.rvLearnMaterial.adapter = learnMaterialAdapter

        // Mengambil data dari ViewModel
        studyViewModel.fetchChapters().observe(this) { chapters ->
            learnMaterialAdapter = LearnMaterialAdapter(chapters)
            binding.rvLearnMaterial.adapter = learnMaterialAdapter
        }
    }
}
