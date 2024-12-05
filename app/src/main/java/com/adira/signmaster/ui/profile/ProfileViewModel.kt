package com.adira.signmaster.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.adira.signmaster.data.pref.UserModel
import com.adira.signmaster.data.repository.RepositoryAuth
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: RepositoryAuth
) : ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getLoginToken(): LiveData<String> {
        return repository.getToken().asLiveData()
    }

    fun getUsername(): LiveData<String> {
        return repository.getUsername().asLiveData()
    }

    fun getEmail(): LiveData<String> {
        return repository.getEmail().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            try {
                val user = getUsername()
                repository.logout()
                Log.d("MainViewModel", "User $user successfully logged out")
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error logging out user: ${e.message}")
            }
        }
    }

}