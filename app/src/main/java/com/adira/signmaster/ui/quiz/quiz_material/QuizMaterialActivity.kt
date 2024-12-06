package com.adira.signmaster.ui.quiz.quiz_material

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.adira.signmaster.R
import com.adira.signmaster.data.model.Quiz
import com.adira.signmaster.databinding.ActivityQuizMaterialBinding
import com.adira.signmaster.ui.quiz.QuizViewModel
import com.adira.signmaster.ui.quiz.quiz_result.QuizResultFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.plattysoft.leonids.ParticleSystem

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

        // Set toolbar navigation icon listener
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed() // Navigasi ke fragment sebelumnya
        }

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

        // Update progress
        val progress = ((index + 1) * 100) / quizList.size
        binding.progressBar.progress = progress

        // Preload next question image
        preloadNextQuestionImage(index)

        // Load image with animation
        val imageAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        binding.ivQuiz.startAnimation(imageAnimation)
        Glide.with(this)
            .load(question.question)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.error_icon)
            .into(binding.ivQuiz)

        setupAnswerButtons(question)
    }

    private fun preloadNextQuestionImage(index: Int) {
        if (index + 1 < quizList.size) {
            Glide.with(this)
                .load(quizList[index + 1].question)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .preload()
        }
    }


    private fun setupAnswerButtons(question: Quiz) {
        val buttons = listOf(binding.btn1, binding.btn2, binding.btn3, binding.btn4)

        // Reset semua tombol
        buttons.forEach {
            it.visibility = View.GONE
            it.text = "" // Hapus teks untuk tombol
        }

        // Tampilkan tombol dengan animasi fade-in atau slide-up
        question.answers.forEachIndexed { index, answer ->
            if (index < buttons.size) {
                buttons[index].apply {
                    text = answer.answer
                    visibility = View.VISIBLE

                    // Terapkan animasi slide-up pada tombol
                    val animation = android.view.animation.AnimationUtils.loadAnimation(this@QuizMaterialActivity, R.anim.slide_up)
                    this.startAnimation(animation)

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

        currentQuestionIndex++
        displayQuiz(currentQuestionIndex)
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
        buttons().forEach { button ->
            button.setBackgroundColor(getColor(R.color.white))
        }

        displayQuiz(currentQuestionIndex)
    }
    private fun buttons() = listOf(binding.btn1, binding.btn2, binding.btn3, binding.btn4)




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

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right) // Transisi balik
    }


}
