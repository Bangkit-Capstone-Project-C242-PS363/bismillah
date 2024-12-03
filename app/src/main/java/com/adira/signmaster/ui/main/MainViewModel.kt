package com.adira.signmaster.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.adira.signmaster.data.pref.UserModel
import com.adira.signmaster.data.repository.RepositoryAuth
import kotlinx.coroutines.launch

class MainViewModel ()