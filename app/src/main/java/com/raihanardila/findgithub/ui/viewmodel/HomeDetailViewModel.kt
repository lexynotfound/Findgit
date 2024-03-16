package com.raihanardila.findgithub.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raihanardila.findgithub.core.data.remote.client.ApiClient
import com.raihanardila.findgithub.core.data.remote.response.DetailUsersResponse
import com.raihanardila.findgithub.core.data.remote.response.ReadmeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeDetailViewModel : ViewModel() {

    private val apiClient = ApiClient()
    private val apiService = apiClient.apiService

    private val _user = MutableLiveData<DetailUsersResponse>()
    val user: LiveData<DetailUsersResponse>
        get() = _user

    private val _readme = MutableLiveData<ReadmeResponse>()
    val readme: LiveData<ReadmeResponse>
        get() = _readme

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    fun fetchUser(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userResponse = apiService.getUserByUsername(username)
                if (userResponse.isSuccessful) {
                    val user = userResponse.body()
                    _user.postValue(user ?: return@launch) // Cek nullability sebelum mengakses body()
                    Log.d("HomeDetailViewModel", "Successfully fetched user data for: $username")
                } else {
                    _error.postValue("Failed to fetch user data: ${userResponse.errorBody()?.string()}")
                    Log.e("HomeDetailViewModel", "Failed to fetch user data for: $username")
                }
            } catch (e: Exception) {
                _error.postValue("Error occurred while fetching user data: ${e.message}")
                Log.e("HomeDetailViewModel", "Error occurred while fetching user data: ${e.message}")
            }
        }
    }

    fun fetchReadme(owner: String, repo: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val readmeResponse = apiService.getReadme(owner, repo)
                if (readmeResponse.isSuccessful) {
                    val readme = readmeResponse.body()
                    _readme.postValue(readme ?: return@launch) // Cek nullability sebelum mengakses body()
                    Log.d("HomeDetailViewModel", "Successfully fetched README for: $repo by $owner")
                } else {
                    _error.postValue("Failed to fetch readme data: ${readmeResponse.errorBody()?.string()}")
                    Log.e("HomeDetailViewModel", "Failed to fetch README for: $repo by $owner")
                }
            } catch (e: Exception) {
                _error.postValue("Error occurred while fetching README data: ${e.message}")
                Log.e("HomeDetailViewModel", "Error occurred while fetching README for: $repo by $owner")
            }
        }
    }
}
