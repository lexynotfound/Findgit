package com.raihanardila.findgithub.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raihanardila.findgithub.core.data.model.UsersModel
import com.raihanardila.findgithub.core.data.remote.client.ApiClient
import com.raihanardila.findgithub.core.data.remote.response.UsersResponse
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val listUsers = MutableLiveData<List<UsersModel>>()

    private val apiService = ApiClient().apiService

    fun setSearchUsers(query: String) {
        viewModelScope.launch {
            try {
                val response = apiService.searchUsers(query = query)
                if (response.isSuccessful) {
                    val usersResponse: UsersResponse? = response.body()
                    listUsers.value = usersResponse?.items ?: emptyList()
                } else {
                    // Handle error response
                    Log.e("SearchUsers", "Error: ${response.message()}")
                    // Notify observers about the failure
                    listUsers.value = emptyList()
                }
            } catch (e: Exception) {
                // Handle network or parsing errors
                Log.e("SearchUsers", "Error: ${e.message}", e)
                // Notify observers about the failure
                listUsers.value = emptyList()
            }
        }
    }

    fun getAllUserss() {
        viewModelScope.launch {
            try {
                val response = apiService.getAllUsers(perPage = 40, sinceUserId = 0)
                if (response.isSuccessful) {
                    val usersResponse: List<UsersModel>? = response.body()
                    listUsers.value = usersResponse ?: emptyList()
                } else {
                    // Handle error response
                    Log.e("GetAllUsers", "Error: ${response.message()}")
                    // Notify observers about the failure
                    listUsers.value = emptyList()
                }
            } catch (e: Exception) {
                // Handle network or parsing errors
                Log.e("GetAllUsers", "Error: ${e.message}", e)
                // Notify observers about the failure
                listUsers.value = emptyList()
            }
        }
    }

    fun searchUsers(): LiveData<List<UsersModel>> {
        return listUsers
    }
}
