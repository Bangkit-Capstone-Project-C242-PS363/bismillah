package com.adira.signmaster.ui.quiz.quiz_result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.adira.signmaster.R
import com.adira.signmaster.data.model.Quiz

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
        view.findViewById<TextView>(R.id.tvQuizResult).text =
            getString(R.string.quiz_result, correctAnswersCount, quizList.size)

        view.findViewById<Button>(R.id.btnExitQuiz).setOnClickListener {
            activity?.finish()
        }

        return view
    }

    companion object {
        private const val ARG_QUIZ_LIST = "quiz_list"
        private const val ARG_CORRECT_COUNT = "correct_count"

        fun newInstance(quizList: List<Quiz>, correctCount: Int): QuizResultFragment {
            return QuizResultFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_QUIZ_LIST, ArrayList(quizList)) // quizList harus Parcelable
                    putInt(ARG_CORRECT_COUNT, correctCount)
                }
            }
        }
    }
}
