package com.raihanardila.findgithub.core.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.raihanardila.findgithub.core.data.local.dao.FavoriteUserDao
import com.raihanardila.findgithub.core.data.local.database.FavoriteUserDatabase
import com.raihanardila.findgithub.core.data.model.FavoriteUsersModel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUserRepository(application: Application) {
    private val mFavDao: FavoriteUserDao

    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    init {
        val db = FavoriteUserDatabase.getDatabase(application)
        mFavDao = db.favoriteUserDao()
    }
    fun getAllFavorite(): LiveData<List<FavoriteUsersModel>> = mFavDao.getAllFavorite()

    fun insert(favorite: FavoriteUsersModel) {
        executorService.execute { mFavDao.insert(favorite) }
    }
    fun delete(favorite: FavoriteUsersModel) {
        executorService.execute { mFavDao.delete(favorite) }
    }
    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUsersModel> = mFavDao.getFavoriteUserByUsername(username)
}