package com.adira.signmaster.ui.study

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.adira.signmaster.R
import com.adira.signmaster.databinding.ActivityStudyBinding
import com.adira.signmaster.ui.study.material_list.MaterialListActivity

class StudyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudyBinding
    private val viewModel: StudyViewModel by viewModels()
    private lateinit var adapter: LearnMaterialAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        setupRecyclerView()
        observeViewModel()

        if (!viewModel.isChaptersFetched.value!!) {
            viewModel.fetchChapters()
        }
    }

    private fun setupRecyclerView() {
        adapter = LearnMaterialAdapter { chapter ->
            navigateToMaterialList(chapter)
        }

        binding.rvLearnMaterial.layoutManager = LinearLayoutManager(this)
        binding.rvLearnMaterial.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.chapters.observe(this) { chapters ->
            binding.progressBar.visibility = View.GONE
            if (chapters != null && chapters.isNotEmpty()) {
                adapter.submitList(chapters)
            } else {
                Toast.makeText(this, "No Materials available", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        viewModel.errorMessage.observe(this) { message ->
            if (!message.isNullOrEmpty()) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToMaterialList(chapter: com.adira.signmaster.data.response.Chapter) {
        val intent = Intent(this, MaterialListActivity::class.java)
        intent.putExtra("CHAPTER_ID", chapter.id.toString())
        intent.putExtra("CHAPTER_TITLE", chapter.title)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}

