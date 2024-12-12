package com.adira.signmaster.ui.quiz.quiz_result

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.adira.signmaster.R
import com.adira.signmaster.data.model.CompleteChapterRequest
import com.adira.signmaster.data.model.Quiz
import com.adira.signmaster.ui.quiz.QuizActivity
import com.adira.signmaster.ui.quiz.QuizViewModel
import com.plattysoft.leonids.ParticleSystem
import kotlinx.coroutines.launch

class QuizResultFragment : Fragment() {

    private lateinit var quizList: List<Quiz>
    private var correctAnswersCount: Int = 0
    private var chapterId: Int = -1

    private val viewModel: QuizViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quizList = arguments?.getParcelableArrayList(ARG_QUIZ_LIST) ?: emptyList()
        correctAnswersCount = arguments?.getInt(ARG_CORRECT_COUNT) ?: 0
        chapterId = arguments?.getInt(ARG_CHAPTER_ID, -1) ?: -1 // Retrieve chapterId

        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToQuizActivity()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_quiz_result, container, false)

        val totalQuestions = quizList.size
        val scorePercentage = if (totalQuestions > 0) {
            (correctAnswersCount * 100) / totalQuestions
        } else {
            0
        }

        // Show the score percentage in the result screen
        view.findViewById<TextView>(R.id.tvQuizResult).text =
            getString(R.string.quiz_result_percentage, scorePercentage)

        triggerConfetti(view)

        // If score is 80 or higher, mark the chapter as completed
        if (scorePercentage >= 80) {
            updateChapterCompletionStatus()
            Toast.makeText(context, "Congratulations! You scored $scorePercentage%", Toast.LENGTH_SHORT).show()
        } else {
            view.findViewById<TextView>(R.id.tvQuizResult).text =
                getString(R.string.failed_message, scorePercentage)
            Toast.makeText(context, "You scored $scorePercentage%. Try again to complete the quiz!", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<Button>(R.id.btnExitQuiz).setOnClickListener {
            navigateToQuizActivity()
        }

        return view
    }

    private fun updateChapterCompletionStatus() {
        viewModel.markChapterAsCompleted(chapterId)
    }

    private fun markChapterAsCompleted() {
        if (chapterId == -1) {
            Log.e("QuizResultFragment", "Invalid chapterId: $chapterId")
            Toast.makeText(context, "Invalid chapter data", Toast.LENGTH_SHORT).show()
            return
        }

        // Launch a coroutine to complete the chapter
        viewLifecycleOwner.lifecycleScope.launch {
            val request = CompleteChapterRequest(chapter_id = chapterId)
            val result = viewModel.completeChapter(request)
            if (result != null && !result.error) {
                Log.d("QuizResultFragment", "Chapter marked as completed successfully")
            } else {
                Log.e("QuizResultFragment", "Failed to mark chapter as completed: ${result?.message}")
                Toast.makeText(
                    context,
                    "Failed to complete chapter: ${result?.message ?: "Unknown error"}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun triggerConfetti(view: View) {
        val rootView = view.findViewById<View>(R.id.rootQuizResult)
        ParticleSystem(requireActivity(), 100, R.drawable.star, 3000)
            .setSpeedRange(0.2f, 0.5f)
            .setFadeOut(2000)
            .setAcceleration(0.0001f, 90)
            .oneShot(rootView, 100)
    }

    private fun navigateToQuizActivity() {
        val intent = Intent(requireContext(), QuizActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        activity?.finish()
    }

    companion object {
        private const val ARG_QUIZ_LIST = "quiz_list"
        private const val ARG_CORRECT_COUNT = "correct_count"
        private const val ARG_CHAPTER_ID = "chapter_id"

        fun newInstance(quizList: List<Quiz>, correctCount: Int, chapterId: Int): QuizResultFragment {
            return QuizResultFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_QUIZ_LIST, ArrayList(quizList))
                    putInt(ARG_CORRECT_COUNT, correctCount)
                    putInt(ARG_CHAPTER_ID, chapterId)
                }
            }
        }
    }
}
