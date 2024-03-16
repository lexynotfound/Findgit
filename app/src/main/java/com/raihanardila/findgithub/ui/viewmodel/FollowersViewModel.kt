package com.raihanardila.findgithub.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raihanardila.findgithub.core.data.model.UsersModel
import com.raihanardila.findgithub.core.data.remote.client.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FollowersViewModel : ViewModel() {

    private val _userFollowers = MutableLiveData<List<UsersModel>>()
    private val apiService = ApiClient().apiService
    val userFollowers: LiveData<List<UsersModel>>
        get() = _userFollowers

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    fun getUserFollowers(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiService.getUserFollowers(username)
                if (response.isSuccessful) {
                    _userFollowers.postValue(response.body())
                    Log.d("FollowersViewModel", "Successfully fetched followers for user: $username")
                } else {
                    _error.postValue("Failed to fetch followers: ${response.code()}")
                    Log.e("FollowersViewModel", "Failed to fetch followers for user: $username")
                }
            } catch (e: Exception) {
                _error.postValue("Error: ${e.message}")
                Log.e("FollowersViewModel", "Error: ${e.message}")
            }
        }
    }
}