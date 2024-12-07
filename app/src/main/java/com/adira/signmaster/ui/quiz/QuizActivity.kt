package com.adira.signmaster.ui.quiz

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.adira.signmaster.R
import com.adira.signmaster.databinding.ActivityQuizBinding
import com.adira.signmaster.ui.quiz.quiz_menu_fragment.QuizMenuFragment

class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding
    private val viewModel: QuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        setupRecyclerView()
        fetchChapters()
    }

    private fun setupRecyclerView() {
        val adapter = ChapterAdapter { chapterId, chapterTitle ->
            navigateToQuizMenu(chapterId, chapterTitle) // Pass both id and title
        }
        binding.recyclerViewQuiz.layoutManager = GridLayoutManager(this, 3)
        binding.recyclerViewQuiz.adapter = adapter

        viewModel.chapters.observe(this) { chapters ->
            adapter.submitList(chapters)
        }

        viewModel.error.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchChapters() {
        viewModel.fetchChapters()
    }

    private fun navigateToQuizMenu(chapterId: Int, chapterTitle: String) {
        val fragment = QuizMenuFragment.newInstance(chapterId, chapterTitle)
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right) // Tambahkan animasi
            .replace(R.id.main, fragment) // R.id.main adalah container layout Anda
            .addToBackStack(null)
            .commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right) // Transisi balik
    }
}
