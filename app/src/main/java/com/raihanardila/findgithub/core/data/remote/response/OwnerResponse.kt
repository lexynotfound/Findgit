package com.raihanardila.findgithub.core.data.remote.response

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class OwnerResponse(
    val login: String,
    val id: Long,
    val name: String ?,

    @SerializedName("avatar_url")
    val avatarUrl: String,

    @SerializedName("url")
    val url: String,

    @SerializedName("html_url")
    val htmlUrl: String
)
