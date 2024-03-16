package com.raihanardila.findgithub.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raihanardila.findgithub.core.data.model.UsersModel
import com.raihanardila.findgithub.core.data.remote.client.ApiClient
import com.raihanardila.findgithub.core.data.remote.response.DetailUsersResponse
import com.raihanardila.findgithub.core.data.remote.response.ReadmeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val apiClient = ApiClient()
    private val apiService = apiClient.apiService

    private val _user = MutableLiveData<DetailUsersResponse>()
    val user: LiveData<DetailUsersResponse>
        get() = _user

    private val _readme = MutableLiveData<ReadmeResponse>()
    val readme: LiveData<ReadmeResponse>
        get() = _readme

    private val _userFollowing = MutableLiveData<List<UsersModel>>()
    val userFollowing: LiveData<List<UsersModel>>
        get() = _userFollowing

    private val _userFollowers = MutableLiveData<List<UsersModel>>()
    val userFollowers: LiveData<List<UsersModel>>
        get() = _userFollowers

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    fun fetchUser() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userResponse = apiService.getUserByUsername(username = "lexynotfound")
                if (userResponse.isSuccessful) {
                    val user = userResponse.body()
                    _user.postValue(user ?: return@launch) // Cek nullability sebelum mengakses body()
                } else {
                    _error.postValue("Failed to fetch user data: ${userResponse.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _error.postValue("Error occurred while fetching user data: ${e.message}")
            }
        }
    }

    fun fetchReadme() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val readmeResponse = apiService.getReadme(owner = "lexynotfound", repo = "lexynotfound")
                if (readmeResponse.isSuccessful) {
                    val readme = readmeResponse.body()
                    _readme.postValue(readme ?: return@launch) // Cek nullability sebelum mengakses body()
                } else {
                    _error.postValue("Failed to fetch readme data: ${readmeResponse.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _error.postValue("Error occurred while fetching readme data: ${e.message}")
            }
        }
    }


    fun getUserFollowing() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiService.getUserFollowing(username = "lexynotfound")
                if (response.isSuccessful) {
                    _userFollowing.postValue(response.body())
                } else {
                    _error.postValue("Failed to fetch followers")
                }
            } catch (e: Exception) {
                _error.postValue("Error: ${e.message}")
            }
        }
    }

    fun getUserFollowers() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiService.getUserFollowers(username = "lexynotfound")
                if (response.isSuccessful) {
                    _userFollowers.postValue(response.body())
                } else {
                    _error.postValue("Failed to fetch followers")
                }
            } catch (e: Exception) {
                _error.postValue("Error: ${e.message}")
            }
        }
    }
}