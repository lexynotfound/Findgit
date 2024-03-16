package com.raihanardila.findgithub.core.data.remote.response

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DetailUsersResponse(
    val login: String,
    val username: String,
    val name: String?,
    val id: Int,

    @SerializedName("avatar_url")
    val avatarURL: String,

    @SerializedName("gravatar_id")
    val gravatarID: String,

    val url: String,

    @SerializedName("html_url")
    val htmlURL: String,

    val followers: String,

    val following: String,

    @SerializedName("repos_url")
    val reposURL: String,

    @SerializedName("starred_url")
    val starredURL: String,

    )
