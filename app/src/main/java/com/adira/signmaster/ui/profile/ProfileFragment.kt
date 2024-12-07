package com.adira.signmaster.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.adira.signmaster.R
import com.adira.signmaster.data.pref.UserPreference
import com.adira.signmaster.data.pref.dataStore
import com.adira.signmaster.databinding.FragmentProfileBinding
import com.adira.signmaster.ui.home.HomeViewModel
import com.adira.signmaster.ui.login.LoginActivity
import com.adira.signmaster.ui.viewmodelfactory.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        profileViewModel.getUsername().observe(viewLifecycleOwner) { username ->
            binding.tvUsername.text = username
        }

        profileViewModel.getEmail().observe(viewLifecycleOwner) { email ->
            binding.tvEmail.text = email
        }

        // Observe and set the subscription status
        lifecycleScope.launch {
            val pref = UserPreference.getInstance(requireContext().dataStore)
            pref.getSubscriptionStatus().collect { isSubscribed ->
                binding.switchSubscription.isChecked = isSubscribed
            }
        }

        // Handle toggle change for the subscription switch
        binding.switchSubscription.setOnCheckedChangeListener { _, isChecked ->
            updateSubscriptionStatus(isChecked)
        }

        binding.tvLogout.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        return binding.root
    }

    private fun updateSubscriptionStatus(isSubscribed: Boolean) {
        lifecycleScope.launch {
            val pref = UserPreference.getInstance(requireContext().dataStore)
            pref.updateSubscriptionStatus(isSubscribed)
        }
    }


    private fun showLogoutConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.logout))
            .setMessage(getString(R.string.logout_verification))
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                logoutUser()
            }
            .show()
    }

    private fun logoutUser() {
        lifecycleScope.launch {
            val pref = UserPreference.getInstance(requireContext().dataStore)
            pref.logout()
            Toast.makeText(requireContext(), getString(R.string.logout_success), Toast.LENGTH_SHORT).show()
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

}

