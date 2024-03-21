package com.raihanardila.findgithub.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteUsersModel(
    @PrimaryKey(autoGenerate = false)
    var username: String = "",
    var avatarUrl: String? = null,
    var isFavorite: Boolean = false
)