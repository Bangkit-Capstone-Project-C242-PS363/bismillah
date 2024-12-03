package com.adira.signmaster.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adira.signmaster.data.repository.RepositoryAuth
import kotlinx.coroutines.launch
import com.adira.signmaster.data.repository.Result

class RegisterViewModel(private val repositoryAuth: RepositoryAuth) : ViewModel() {

    // Untuk mengamati hasil registrasi
    private val _registerResult = MutableLiveData<Result<String>>()
    val registerResult: LiveData<Result<String>> = _registerResult

    private val _isError = MutableLiveData<String?>()
    val isError: LiveData<String?> = _isError

    // Fungsi untuk melakukan registrasi
    fun register(name: String, email: String, password: String, confirmPassword: String) {
        if (name.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            _isError.value = "All fields must be filled"
            _registerResult.value = Result.Error("All fields must be filled")
            return
        }

        if (password != confirmPassword) {
            _isError.value = "Passwords do not match"
            _registerResult.value = Result.Error("Passwords do not match")
            return
        }

        // Menggunakan viewModelScope untuk menjalankan coroutine
        viewModelScope.launch {
            try {
                // Melakukan registrasi dengan repository
                val response = repositoryAuth.register(name, email, password, confirmPassword)

                if (response.error) {
                    _registerResult.value = Result.Error(response.message ?: "Registration failed")
                } else {
                    _registerResult.value = Result.Success(response.message ?: "Registration successful")
                }
            } catch (e: Exception) {
                _isError.value = "Error: ${e.message}"
                _registerResult.value = Result.Error("Registration failed due to an error: ${e.message}")
            }
        }
    }
}
