package com.raihanardila.findgithub.core.data.remote.response

data class ReadmeResponse(
    val name: String,
    val path: String,
    val sha: String,
    val size: Long,
    val url: String,
    val htmlUrl: String,
    val gitUrl: String,
    val downloadUrl: String,
    val type: String,
    val content: String,
    val encoding: String
)
