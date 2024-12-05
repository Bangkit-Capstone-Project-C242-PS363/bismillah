package com.adira.signmaster.ui.quiz.quiz_material

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.adira.signmaster.R
import com.adira.signmaster.data.model.Quiz
import com.adira.signmaster.databinding.ActivityQuizMaterialBinding
import com.adira.signmaster.ui.quiz.QuizViewModel
import com.adira.signmaster.ui.quiz.quiz_result.QuizResultFragment
import com.bumptech.glide.Glide

class QuizMaterialActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizMaterialBinding
    private val viewModel: QuizViewModel by viewModels()
    private var quizList: List<Quiz> = emptyList()
    private var currentQuestionIndex = 0
    private var correctAnswersCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizMaterialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil chapterId dan chapterTitle dari Intent
        val chapterId = intent.getIntExtra(EXTRA_CHAPTER_ID, -1)
        val chapterTitle = intent.getStringExtra(EXTRA_CHAPTER_TITLE)

        if (chapterId == -1 || chapterTitle.isNullOrEmpty()) {
            showErrorAndExit("Invalid chapter data")
            return
        }

        supportActionBar?.hide()

        // Tampilkan judul chapter di toolbar
        binding.toolbarTitle.text = chapterTitle

        // Atur ulang UI
        setupUI()

        // Fetch quizzes berdasarkan chapterId
        fetchQuizzes(chapterId)
    }



    private fun setupUI() {
        binding.progressBar.progress = 0
        binding.fabRepeatQuiz.setOnClickListener {
            restartQuiz()
        }
    }


    private fun fetchQuizzes(chapterId: Int) {
        viewModel.fetchQuiz(chapterId)
        viewModel.quiz.observe(this) { quizzes ->
            if (!quizzes.isNullOrEmpty()) {
                quizList = quizzes
                if (quizList.all { it.answers.isNotEmpty() }) {
                    displayQuiz(currentQuestionIndex)
                } else {
                    showErrorAndExit("Some questions have no answers")
                }
            } else {
                showErrorAndExit("No quizzes available for this chapter")
            }
        }

        viewModel.error.observe(this) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                showErrorAndExit("Error fetching quizzes: $errorMessage")
            }
        }
    }


    private fun displayQuiz(index: Int) {
        if (index >= quizList.size) {
            showQuizResult()
            return
        }

        val question = quizList[index]

        // Tampilkan progress kuis
        val progress = ((index + 1) * 100) / quizList.size
        binding.progressBar.progress = progress

        // Set toolbar title
        binding.toolbarTitle.text = getString(R.string.question_progress, index + 1, quizList.size)

        // Tampilkan gambar pertanyaan
        Glide.with(this)
            .load(question.question)
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.error_icon)
            .into(binding.ivQuiz)

        // Atur tombol jawaban
        setupAnswerButtons(question)
    }



    private fun setupAnswerButtons(question: Quiz) {
        val buttons = listOf(binding.btn1, binding.btn2, binding.btn3, binding.btn4)

        buttons.forEach { it.visibility = View.GONE } // Reset semua tombol

        question.answers.forEachIndexed { index, answer ->
            if (index < buttons.size) {
                buttons[index].apply {
                    text = answer.answer
                    visibility = View.VISIBLE
                    setOnClickListener {
                        validateAnswer(index, question.correctAnswerIndex)
                    }
                }
            }
        }
    }


    private fun validateAnswer(selectedIndex: Int, correctIndex: Int) {
        if (selectedIndex == correctIndex) {
            correctAnswersCount++
        }

        // Delay sebelum menampilkan pertanyaan berikutnya
        binding.root.postDelayed({
            currentQuestionIndex++
            displayQuiz(currentQuestionIndex)
        }, 1000)
    }



    private fun restartQuiz() {
        currentQuestionIndex = 0
        correctAnswersCount = 0

        // Tampilkan kembali elemen yang relevan
        binding.ivQuiz.visibility = View.VISIBLE
        binding.progressBar.visibility = View.VISIBLE
        binding.btn1.visibility = View.VISIBLE
        binding.btn2.visibility = View.VISIBLE
        binding.btn3.visibility = View.VISIBLE
        binding.btn4.visibility = View.VISIBLE
        binding.fabRepeatQuiz.visibility = View.VISIBLE

        displayQuiz(currentQuestionIndex)
    }



    private fun showQuizResult() {
        // Sembunyikan elemen yang tidak relevan
        binding.ivQuiz.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.btn1.visibility = View.GONE
        binding.btn2.visibility = View.GONE
        binding.btn3.visibility = View.GONE
        binding.btn4.visibility = View.GONE
        binding.fabRepeatQuiz.visibility = View.GONE

        // Navigasi ke fragment hasil kuis
        val fragment = QuizResultFragment.newInstance(quizList, correctAnswersCount)
        supportFragmentManager.beginTransaction()
            .replace(R.id.main, fragment)
            .addToBackStack(null)
            .commit()
    }


    private fun showErrorAndExit(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        Log.e("QuizMaterialActivity", message)
        finish()
    }

    companion object {
        const val EXTRA_CHAPTER_ID = "extra_chapter_id"
        const val EXTRA_CHAPTER_TITLE = "extra_chapter_title"
    }

}
