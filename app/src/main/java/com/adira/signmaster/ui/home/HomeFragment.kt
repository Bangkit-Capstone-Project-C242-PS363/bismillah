package com.adira.signmaster.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.adira.signmaster.R
import com.adira.signmaster.databinding.FragmentHomeBinding
import com.adira.signmaster.ui.quiz.QuizActivity
import com.adira.signmaster.ui.study.StudyActivity
import com.adira.signmaster.ui.translate.TranslateActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Hide action bar
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        // Set up card click listeners
        binding.cardStudy.setOnClickListener {
            val intent = Intent(requireContext(), StudyActivity::class.java)
            startActivity(intent)
        }

        binding.cardQuiz.setOnClickListener {
            val intent = Intent(requireContext(), QuizActivity::class.java)
            startActivity(intent)
        }

        binding.cardTranslate.setOnClickListener {
            val intent = Intent(requireContext(), TranslateActivity::class.java)
            startActivity(intent)
        }

        // Data untuk card items
        val cardItems = listOf(
            CardItem("Card 1", R.drawable.placeholder_image),
            CardItem("Card 2", R.drawable.placeholder_image),
        )

        // Menghubungkan RecyclerView dengan adapter
        val cardAdapter = CardAdapter(cardItems)
        binding.rvNews.apply {
            adapter = cardAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
