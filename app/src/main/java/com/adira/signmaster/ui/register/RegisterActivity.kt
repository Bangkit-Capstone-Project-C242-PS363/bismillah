package com.adira.signmaster.ui.register

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.adira.signmaster.data.repository.Result
import com.adira.signmaster.databinding.ActivityRegisterBinding
import com.adira.signmaster.ui.login.LoginActivity
import android.util.Patterns
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import com.adira.signmaster.R
import com.adira.signmaster.ui.viewmodelfactory.ViewModelFactory


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Menavigasi ke halaman login
        binding.navigateToLogin.setOnClickListener {
            navigateToLogin()
        }

        setupView()
        setupAction()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.btnRegister.setOnClickListener {
            val name = binding.fullNameInput.text.toString()
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()
            val confirmPassword = binding.confirmPasswordInput.text.toString()

            if (!isInputValid(name, email, password, confirmPassword)) {
                return@setOnClickListener
            }

            binding.progressBar.visibility = View.VISIBLE  // Show progress bar when starting registration
            registerViewModel.register(name, email, password, confirmPassword)

            // Observasi hasil registrasi
            registerViewModel.registerResult.observe(this) { result ->
                binding.progressBar.visibility = View.GONE  // Hide progress bar after receiving the result

                when (result) {
                    is Result.Success -> {
                        Toast.makeText(this, getString(R.string.signup_success), Toast.LENGTH_SHORT).show()
                        navigateToLogin()
                    }

                    is Result.Error -> {
                        // Tangani error jika terjadi kesalahan
                        registerViewModel.isError.observe(this) { errorMessage ->
                            if (errorMessage != null) {
                                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    else -> {
                        Toast.makeText(this, getString(R.string.signup_failed), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun isInputValid(fullName: String, email: String, password: String, confirmPassword: String): Boolean {
        return when {
            fullName.isEmpty() -> {
                Toast.makeText(this, "Full name is required", Toast.LENGTH_SHORT).show()
                false
            }
            email.isEmpty() -> {
                Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show()
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
                false
            }
            password.isEmpty() -> {
                Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show()
                false
            }
            password.length < 8 -> {
                Toast.makeText(this, "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show()
                false
            }
            confirmPassword.isEmpty() -> {
                Toast.makeText(this, "Please confirm your password", Toast.LENGTH_SHORT).show()
                false
            }
            password != confirmPassword -> {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}





