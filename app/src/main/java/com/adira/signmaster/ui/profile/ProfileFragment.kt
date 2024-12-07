package com.adira.signmaster.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.adira.signmaster.R
import com.adira.signmaster.data.pref.UserPreference
import com.adira.signmaster.data.pref.dataStore
import com.adira.signmaster.data.retrofit.ApiConfigAuth
import com.adira.signmaster.databinding.FragmentProfileBinding
import com.adira.signmaster.ui.login.LoginActivity
import com.adira.signmaster.ui.viewmodelfactory.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel: ProfileViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe and set the user's username and email
        profileViewModel.getUsername().observe(viewLifecycleOwner) { username ->
            binding.tvUsername.text = username
        }

        profileViewModel.getEmail().observe(viewLifecycleOwner) { email ->
            binding.tvEmail.text = email
        }

        // Observe and set the subscription status from DataStore
        lifecycleScope.launch {
            val pref = UserPreference.getInstance(requireContext().dataStore)
            pref.getSubscriptionStatus().collect { isSubscribed ->
                binding.switchSubscription.isChecked = isSubscribed
            }
        }

        // Handle switch toggle for subscription
        binding.switchSubscription.setOnCheckedChangeListener { _, isChecked ->
            // Disable the switch to avoid multiple requests
            binding.switchSubscription.isEnabled = false
            if (isChecked) {
                subscribe()
            } else {
                unsubscribe()
            }
        }

        // Handle logout action
        binding.tvLogout.setOnClickListener {
            showLogoutConfirmationDialog()
        }
    }

    /**
     * Calls the subscribe API to activate the user's subscription.
     */
    private fun subscribe() {
        lifecycleScope.launch {
            val pref = UserPreference.getInstance(requireContext().dataStore)
            val token = pref.getLoginToken().first()
            try {
                val response = ApiConfigAuth.getApiServiceAuth(token).subscribe()
                if (!response.error) {
                    pref.updateSubscriptionStatus(true)
                } else {
                    Toast.makeText(requireContext(), "Failed to subscribe: ${response.message}", Toast.LENGTH_SHORT).show()
                    binding.switchSubscription.isChecked = false // Revert the switch state
                }
            } catch (e: HttpException) {
                Toast.makeText(requireContext(), "Subscription failed", Toast.LENGTH_SHORT).show()
                binding.switchSubscription.isChecked = false // Revert the switch state
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "An unexpected error occurred", Toast.LENGTH_SHORT).show()
                binding.switchSubscription.isChecked = false // Revert the switch state
            } finally {
                binding.switchSubscription.isEnabled = true // Re-enable the switch
            }
        }
    }

    /**
     * Calls the unsubscribe API to deactivate the user's subscription.
     */
    private fun unsubscribe() {
        lifecycleScope.launch {
            val pref = UserPreference.getInstance(requireContext().dataStore)
            val token = pref.getLoginToken().first()
            try {
                val response = ApiConfigAuth.getApiServiceAuth(token).unsubscribe()
                if (!response.error) {
                    pref.updateSubscriptionStatus(false)
                } else {
                    Toast.makeText(requireContext(), "Failed to unsubscribe: ${response.message}", Toast.LENGTH_SHORT).show()
                    binding.switchSubscription.isChecked = true // Revert the switch state
                }
            } catch (e: HttpException) {
                Toast.makeText(requireContext(), "Unsubscription failed", Toast.LENGTH_SHORT).show()
                binding.switchSubscription.isChecked = true // Revert the switch state
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "An unexpected error occurred", Toast.LENGTH_SHORT).show()
                binding.switchSubscription.isChecked = true // Revert the switch state
            } finally {
                binding.switchSubscription.isEnabled = true // Re-enable the switch
            }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
