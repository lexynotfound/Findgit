package com.raihanardila.findgithub.core.data.remote.response

import com.raihanardila.findgithub.core.data.model.OwnerModel

data class ReposResponse(
    val id: Long,
    val name: String,
    val fullName: String,
    val owner: OwnerModel,
    val private: Boolean,
    val htmlUrl: String,
    val description: String?,
    val fork: Boolean,
    val url: String,
    val createdAt: String,
    val updatedAt: String,
    val pushedAt: String,
    val homepage: String?,
    val size: Long,
    val stargazersCount: Long,
    val watchersCount: Long,
    val language: String?,
    val forksCount: Long,
    val openIssuesCount: Long,
    val defaultBranch: String
)
