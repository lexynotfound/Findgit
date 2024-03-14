package com.raihanardila.findgithub.core.data.remote.response

data class OwnerResponse(
    val login: String,
    val id: Long,
    val avatarUrl: String,
    val url: String,
    val htmlUrl: String
)
