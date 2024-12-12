package com.adira.signmaster.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.adira.signmaster.R
import com.adira.signmaster.data.pref.UserPreference
import com.adira.signmaster.data.pref.dataStore
import com.adira.signmaster.databinding.FragmentHomeBinding
import com.adira.signmaster.ui.quiz.QuizActivity
import com.adira.signmaster.ui.study.StudyActivity
import com.adira.signmaster.ui.translate.TranslateActivity
import com.adira.signmaster.ui.viewmodelfactory.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

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
            navigateToActivity(StudyActivity::class.java)
        }

        binding.cardQuiz.setOnClickListener {
            navigateToActivity(QuizActivity::class.java)
        }

        binding.cardTranslate.setOnClickListener {
            val intent = Intent(requireContext(), TranslateActivity::class.java)
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        homeViewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        homeViewModel.getNews()

        observeNews()
        checkSubscriptionStatus()


    }

    private fun observeNews() {
        homeViewModel.newsList.observe(viewLifecycleOwner) { newsList ->
            val newsAdapter = NewsAdapter(newsList)
            binding.rvNews.apply {
                setHasFixedSize(true)
                adapter = newsAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }

    private fun navigateToActivity(activityClass: Class<*>) {
        val intent = Intent(requireContext(), activityClass)
        startActivity(intent)
        activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    private fun checkSubscriptionStatus() {
        lifecycleScope.launch {
            val pref = UserPreference.getInstance(requireContext().dataStore)
            val isSubscribed = pref.getSubscriptionStatus().first()
            binding.ivVip.visibility = if (isSubscribed) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}


