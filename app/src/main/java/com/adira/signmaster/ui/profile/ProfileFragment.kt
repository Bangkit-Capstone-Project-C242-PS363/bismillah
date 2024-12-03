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
import androidx.lifecycle.lifecycleScope
import com.adira.signmaster.R
import com.adira.signmaster.data.pref.UserPreference
import com.adira.signmaster.data.pref.dataStore
import com.adira.signmaster.databinding.FragmentProfileBinding
import com.adira.signmaster.ui.login.LoginActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding  // Binding untuk mengakses elemen UI

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Menangani klik pada tvLogout
        binding.tvLogout.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        return binding.root
    }

    private fun showLogoutConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.logout))
            .setMessage(getString(R.string.logout_verification))
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                // Panggil fungsi logout dan tampilkan pesan
                logoutUser()
            }
            .show()
    }

    private fun logoutUser() {
        // Hapus session login
        lifecycleScope.launch {
            val pref = UserPreference.getInstance(requireContext().dataStore)
            pref.logout()  // Menghapus data session

            // Tampilkan Toast untuk menunjukkan logout berhasil
            Toast.makeText(requireContext(), getString(R.string.logout_success), Toast.LENGTH_SHORT).show()

            // Navigasikan pengguna ke halaman login atau activity sambutan
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        // Mengarahkan pengguna ke halaman login
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()  // Menutup ProfileActivity atau MainActivity setelah logout
    }
}

