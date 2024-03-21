package com.raihanardila.findgithub.core.insert

import com.raihanardila.findgithub.core.data.model.FavoriteUsersModel
import com.raihanardila.findgithub.core.repository.FavoriteUserRepository
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel



class FavoriteInsert(application: Application) : ViewModel() {
    private val mNoteRepository: FavoriteUserRepository = FavoriteUserRepository(application)
    fun insert(note: FavoriteUsersModel) {
        mNoteRepository.insert(note)
    }
    fun delete(note: FavoriteUsersModel) {
        mNoteRepository.delete(note)
    }
    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUsersModel> = mNoteRepository.getFavoriteUserByUsername(username)
}