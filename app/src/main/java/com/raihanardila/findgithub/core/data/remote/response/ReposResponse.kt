package com.raihanardila.findgithub.core.data.remote.response

import com.google.gson.annotations.SerializedName
import com.raihanardila.findgithub.core.data.model.OwnerModel

data class ReposResponse(
    val id: Long,
    val name: String,
    @SerializedName("full_name") val fullName: String,
    val owner: OwnerModel,
    val private: Boolean,
    @SerializedName("html_url") val htmlUrl: String,
    val description: String?,
    val fork: Boolean,
    val url: String,
    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String,

    @SerializedName("pushed_at")
    val pushedAt: String,

    val homepage: String?,
    val size: Long,

    @SerializedName("stargazers_count")
    val stargazersCount: Long,

    @SerializedName("watchers_count")
    val watchersCount: Long,

    val language: String?,
    @SerializedName("forks_count") val forksCount: Long,
    @SerializedName("open_issues_count") val openIssuesCount: Long,
    @SerializedName("default_branch") val defaultBranch: String
)
