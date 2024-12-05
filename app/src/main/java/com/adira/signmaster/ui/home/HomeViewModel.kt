package com.adira.signmaster.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.adira.signmaster.data.repository.RepositoryAuth

class HomeViewModel (
    private val authRepository: RepositoryAuth,
): ViewModel() {

    fun getUsername(): LiveData<String> {
        return authRepository.getUsername().asLiveData()
    }

}