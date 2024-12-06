package com.adira.signmaster.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.adira.signmaster.R
import com.adira.signmaster.databinding.FragmentHomeBinding
import com.adira.signmaster.ui.quiz.QuizActivity
import com.adira.signmaster.ui.study.StudyActivity
import com.adira.signmaster.ui.translate.TranslateActivity
import com.adira.signmaster.ui.viewmodelfactory.ViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity().application)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).supportActionBar?.hide()


        homeViewModel.getUsername().observe(viewLifecycleOwner) { username ->
            binding.textUserName.text = username
        }

        binding.cardStudy.setOnClickListener {
            val intent = Intent(requireContext(), StudyActivity::class.java)
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left) // Transisi

        }

        binding.cardQuiz.setOnClickListener {
            val intent = Intent(requireContext(), QuizActivity::class.java)
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left) // Transisi
        }


        binding.cardTranslate.setOnClickListener {
            val intent = Intent(requireContext(), TranslateActivity::class.java)
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left) // Transisi

        }


        val cardItems = listOf(
            CardItem("Card 1", R.drawable.placeholder_image),
            CardItem("Card 2", R.drawable.placeholder_image),
        )

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
