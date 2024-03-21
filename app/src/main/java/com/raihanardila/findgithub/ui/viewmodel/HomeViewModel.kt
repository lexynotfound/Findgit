package com.raihanardila.findgithub.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.raihanardila.findgithub.core.data.model.FavoriteUsersModel
import com.raihanardila.findgithub.core.data.model.UsersModel
import com.raihanardila.findgithub.core.data.remote.client.ApiClient
import com.raihanardila.findgithub.core.data.remote.response.UsersResponse
import com.raihanardila.findgithub.core.repository.FavoriteUserRepository
import com.raihanardila.findgithub.util.SettingPreferences
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) :  AndroidViewModel(application){
    private val listUsers = MutableLiveData<List<UsersModel>>()
    private val mNoteRepository: FavoriteUserRepository = FavoriteUserRepository(application)
    fun getAllFavorite(): LiveData<List<FavoriteUsersModel>> = mNoteRepository.getAllFavorite()

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
        }
    }

    fun searchUsers(): LiveData<List<UsersModel>> {
        return listUsers
    }

    fun insertFavoriteUser(user: UsersModel) {
        // Tambahkan user ke daftar favorit
        val favoriteUser = FavoriteUsersModel(
            username = user.login,
            avatarUrl = user.avatarURL,
            // tambahkan properti lain yang diperlukan sesuai dengan kebutuhan
        )
        mNoteRepository.insert(favoriteUser)
    }

    fun deleteFavoriteUser(user: UsersModel) {
        viewModelScope.launch {
            val favoriteUser = FavoriteUsersModel(
                username = user.login,
                avatarUrl = user.avatarURL,
                // tambahkan properti lain yang diperlukan sesuai dengan kebutuhan
            )
            mNoteRepository.delete(favoriteUser)
        }
    }

    fun deleteFavoriteUsers(favorite: FavoriteUsersModel) {
        viewModelScope.launch {
            val favoriteUser = FavoriteUsersModel(
                username = favorite.username,
                avatarUrl = favorite.avatarUrl,
                // tambahkan properti lain yang diperlukan sesuai dengan kebutuhan
            )
            mNoteRepository.delete(favoriteUser)
        }
    }

    fun getAllFavoriteUsers(): LiveData<List<FavoriteUsersModel>> {
        return mNoteRepository.getAllFavorite()
    }

    class ViewModelFactory(private val application: Application) : ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                return HomeViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}
