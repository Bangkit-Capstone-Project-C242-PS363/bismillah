package com.adira.signmaster.ui.quiz.quiz_result

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.adira.signmaster.R
import com.adira.signmaster.data.model.Quiz
import com.adira.signmaster.ui.quiz.QuizActivity
import com.plattysoft.leonids.ParticleSystem

class QuizResultFragment : Fragment() {

    private lateinit var quizList: List<Quiz>
    private var correctAnswersCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quizList = arguments?.getParcelableArrayList(ARG_QUIZ_LIST) ?: emptyList()
        correctAnswersCount = arguments?.getInt(ARG_CORRECT_COUNT) ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_quiz_result, container, false)

        // Hitung skor dalam persentase
        val totalQuestions = quizList.size
        val scorePercentage = if (totalQuestions > 0) {
            (correctAnswersCount * 100) / totalQuestions
        } else {
            0 // Default jika tidak ada pertanyaan
        }

        // Tampilkan skor persentase
        view.findViewById<TextView>(R.id.tvQuizResult).text =
            getString(R.string.quiz_result_percentage, scorePercentage)

        // Tambahkan animasi confetti
        triggerConfetti(view)

        // Tombol keluar
        view.findViewById<Button>(R.id.btnExitQuiz).setOnClickListener {
            val intent = Intent(requireContext(), QuizActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left) // Transisi
            activity?.finish() // Tutup aktivitas saat ini
        }


        return view
    }

    private fun triggerConfetti(view: View) {
        val rootView = view.findViewById<View>(R.id.rootQuizResult) // ID root layout
        ParticleSystem(requireActivity(), 100, R.drawable.star, 3000) // Gunakan requireActivity()
            .setSpeedRange(0.2f, 0.5f)
            .setFadeOut(2000)
            .setAcceleration(0.0001f, 90)
            .oneShot(rootView, 100)
    }


    companion object {
        private const val ARG_QUIZ_LIST = "quiz_list"
        private const val ARG_CORRECT_COUNT = "correct_count"

        fun newInstance(quizList: List<Quiz>, correctCount: Int): QuizResultFragment {
            return QuizResultFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_QUIZ_LIST, ArrayList(quizList))
                    putInt(ARG_CORRECT_COUNT, correctCount)
                }
            }
        }
    }
}
