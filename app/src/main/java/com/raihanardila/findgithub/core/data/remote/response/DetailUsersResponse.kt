package com.raihanardila.findgithub.core.data.remote.response

import kotlinx.serialization.SerialName

data class DetailUsersResponse(
    val login: String,
    val id: Int,
    @SerialName("avatar_url")
    val avatarURL: String,

    @SerialName("gravatar_id")
    val gravatarID: String,

    val url: String,

    @SerialName("html_url")
    val htmlURL: String,

    @SerialName("followers_url")
    val followersURL: String,

    @SerialName("following_url")
    val followingURL: String,

    @SerialName("repos_url")
    val reposURL: String,

    @SerialName("starred_url")
    val starredURL: String,

)
