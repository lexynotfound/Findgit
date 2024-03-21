package com.raihanardila.findgithub.core.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UsersModel(
    val login: String,
    val username: String,
    val name: String?,
    val id: Int,
    var isFavorite: Boolean = false,

    @SerializedName("node_id")
    val nodeID: String,

    @SerializedName("avatar_url")
    val avatarURL: String,

    @SerializedName("gravatar_id")
    val gravatarID: String,

    val url: String,

    @SerializedName("html_url")
    val htmlURL: String,

    @SerializedName("followers_url")
    val followersURL: String,

    @SerializedName("following_url")
    val followingURL: String,

    @SerializedName("gists_url")
    val gistsURL: String,

    @SerializedName("starred_url")
    val starredURL: String,

    @SerializedName("subscriptions_url")
    val subscriptionsURL: String,

    @SerializedName("organizations_url")
    val organizationsURL: String,

    @SerializedName("repos_url")
    val reposURL: String,

    @SerializedName("events_url")
    val eventsURL: String,

    @SerializedName("received_events_url")
    val receivedEventsURL: String,

    val type: String,

    @SerializedName("site_admin")
    val siteAdmin: Boolean
)
