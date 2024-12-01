package com.adira.signmaster.ui.quiz

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.adira.signmaster.databinding.ActivityQuizBinding

class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()



        val quizData = listOf("Quiz 1", "Quiz 2", "Quiz 3", "Quiz 4", "Quiz 5", "Quiz 6",)
        val quizAdapter = QuizAdapter(quizData)


        binding.recyclerViewQuiz.apply {
            layoutManager = GridLayoutManager(this@QuizActivity, 3)
            adapter = quizAdapter
        }
    }
}
