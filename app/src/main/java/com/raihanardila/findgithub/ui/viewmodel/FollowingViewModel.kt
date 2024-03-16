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

class FollowingViewModel : ViewModel() {
    private val _userFollowing = MutableLiveData<List<UsersModel>>()
    private val apiService = ApiClient().apiService
    val userFollowing: LiveData<List<UsersModel>>
        get() = _userFollowing

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    fun getUserFollowing(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiService.getUserFollowing(username)
                if (response.isSuccessful) {
                    _userFollowing.postValue(response.body())
                    Log.d("FollowingViewModel", "Successfully fetched following for user: $username")
                } else {
                    _error.postValue("Failed to fetch following: ${response.code()}")
                    Log.e("FollowingViewModel", "Failed to fetch following for user: $username")
                }
            } catch (e: Exception) {
                _error.postValue("Error: ${e.message}")
                Log.e("FollowingViewModel", "Error: ${e.message}")
            }
        }
    }
}
