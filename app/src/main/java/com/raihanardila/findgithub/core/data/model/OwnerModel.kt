package com.raihanardila.findgithub.core.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class OwnerModel(
    val login: String,
    val name: String,
    val id: Long,

    @SerializedName("avatar_url")
    val avatarUrl: String,

    @SerializedName("url")
    val url: String,

    @SerializedName("html_url")
    val htmlUrl: String
)
