package com.adira.signmaster.ui.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.adira.signmaster.R
import com.adira.signmaster.data.repository.Result
import com.adira.signmaster.ui.main.MainActivity
import com.adira.signmaster.databinding.ActivityLoginBinding
import com.adira.signmaster.ui.register.RegisterActivity
import com.adira.signmaster.ui.viewmodelfactory.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        loginViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }


        loginViewModel.isSuccess.observe(this) { isSuccess ->
            if (isSuccess) {
                binding.progressBar.visibility = View.GONE
            }
        }


        loginViewModel.loginResult.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    loginViewModel.getSession()
                    navigateToMainActivity()
                    Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show()
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    loginViewModel.isError.observe(this) { errorMessage ->
                        errorMessage?.let {
                            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else -> {
                    Toast.makeText(this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.navigateToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        setupAction()
        setupView()
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
        binding.btnLogin.setOnClickListener {
            val email = binding.emailInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()

            if (!isInputValid(email, password)) {
                return@setOnClickListener
            }

            loginViewModel.isLoading.observe(this) { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }

            loginViewModel.login(email, password)

            loginViewModel.loginResult.observe(this) { result ->
                binding.progressBar.visibility = View.VISIBLE
                when (result) {
                    is Result.Success -> {
                        loginViewModel.getSession()
                        navigateToMainActivity()
                        Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show()
                    }

                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        loginViewModel.isError.observe(this) { errorMessage ->
                            if (errorMessage != null) {
                                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    else -> Toast.makeText(this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun isInputValid(email: String, password: String): Boolean {
        return when {
            email.isEmpty() || password.isEmpty() -> {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                false
            }
            password.length < 8 -> {
                Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}

