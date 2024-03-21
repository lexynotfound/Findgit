package com.raihanardila.findgithub.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.raihanardila.findgithub.core.insert.FavoriteInsert

class FavoriteViewModel private constructor(private val mApplication: Application) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteInsert::class.java)) {
            return FavoriteInsert(mApplication) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var INSTANCE: FavoriteViewModel? = null
        @JvmStatic
        fun getInstance(application: Application): FavoriteViewModel {
            if (INSTANCE == null) {
                synchronized(FavoriteViewModel::class.java) {
                    INSTANCE = FavoriteViewModel(application)
                }
            }
            return INSTANCE as FavoriteViewModel
        }
    }
}
