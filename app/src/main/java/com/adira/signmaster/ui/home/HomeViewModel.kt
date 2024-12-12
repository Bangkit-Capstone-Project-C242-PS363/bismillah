package com.adira.signmaster.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.adira.signmaster.data.repository.RepositoryAuth
import com.adira.signmaster.data.response.News
import com.adira.signmaster.data.retrofit.ApiConfigNews
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(
    private val authRepository: RepositoryAuth
) : ViewModel() {

    fun getUsername(): LiveData<String> {
        return authRepository.getUsername().asLiveData()
    }

    private val _newsList = MutableLiveData<List<News>>()
    val newsList: LiveData<List<News>> get() = _newsList

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _isNewsFetched = MutableLiveData(false)
    val isNewsFetched: LiveData<Boolean> get() = _isNewsFetched

    fun getNews() {
        if (_isNewsFetched.value == true) return

        _loading.value = true
        val client = ApiConfigNews.apiServiceNews.getAllNews()
        client.enqueue(object : Callback<List<News>> {
            override fun onResponse(call: Call<List<News>>, response: Response<List<News>>) {
                _loading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _newsList.value = response.body()
                    _isNewsFetched.value = true
                } else {
                    _error.value = "Failed to load news: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<List<News>>, t: Throwable) {
                _loading.value = false
                _error.value = "Error occurred: ${t.message}"
            }
        })
    }
}



