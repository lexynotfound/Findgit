package com.raihanardila.findgithub.core.data.remote.response

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ReadmeResponse(
    val name: String,
    val path: String,
    val sha: String,
    val size: Long,

    @SerializedName("url")
    val url: String,

    @SerializedName("html_url")
    val htmlUrl: String,

    @SerializedName("git_url")
    val gitUrl: String,
    @SerializedName("download_url")
    val downloadUrl: String,

    val type: String,
    val content: String,
    val encoding: String
)
