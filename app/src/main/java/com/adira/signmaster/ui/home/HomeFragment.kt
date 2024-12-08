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
import com.adira.signmaster.ui.home.subscription.SubscriptionRequiredFragment
import com.adira.signmaster.ui.quiz.QuizActivity
import com.adira.signmaster.ui.study.StudyActivity
import com.adira.signmaster.ui.translate.TranslateActivity
import com.adira.signmaster.ui.viewmodelfactory.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest
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
            CardItem("Card 3", R.drawable.placeholder_image),
        )

        val cardAdapter = CardAdapter(cardItems)
        binding.rvNews.apply {
            setHasFixedSize(true)
            adapter = cardAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

    }

    private fun checkSubscriptionStatusAndNavigate() {
        lifecycleScope.launch {
            val pref = UserPreference.getInstance(requireContext().dataStore)
            val isSubscribed = pref.getSubscriptionStatus().first()

            if (isSubscribed) {
                // User is subscribed, navigate to QuizActivity
                val intent = Intent(requireContext(), QuizActivity::class.java)
                startActivity(intent)
            } else {
                // User is not subscribed, show the SubscriptionRequiredFragment
                showSubscriptionRequiredFragment()
            }
        }
    }
    private fun showSubscriptionRequiredFragment() {
        val subscriptionFragment = SubscriptionRequiredFragment()
        subscriptionFragment.show(parentFragmentManager, "SubscriptionRequiredFragment")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
