package com.raihanardila.findgithub.core.data.remote.client

import com.raihanardila.findgithub.BuildConfig
import com.raihanardila.findgithub.core.data.model.UsersModel
import com.raihanardila.findgithub.core.data.remote.response.DetailUsersResponse
import com.raihanardila.findgithub.core.data.remote.response.ReadmeResponse
import com.raihanardila.findgithub.core.data.remote.response.ReposResponse
import com.raihanardila.findgithub.core.data.remote.response.UsersResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    //Endpoint Search Users
    @GET("search/users")
    suspend fun searchUsers(
        @Query("q") query: String
    ): Response<UsersResponse>

    // Endpoint Get Users
    @GET("users")
    suspend fun getAllUsers(
        @Query("per_page") perPage: Int,
        @Query("since") sinceUserId: Int,
    ): Response<List<UsersModel>>

    // Endpoint Get User by Username
    @GET("users/{username}")
    suspend fun getUserByUsername(
        @Path("username") username: String
    ): Response<DetailUsersResponse>

    // Endpoint Get Followers by Username
    @GET("users/{username}/followers")
    suspend fun getUserFollowers(
        @Path("username") username: String
    ): Response<List<UsersModel>>

    // Endpoint Get Following by Username
    @GET("users/{username}/following")
    suspend fun getUserFollowing(
        @Path("username") username: String
    ): Response<List<UsersModel>>


    // Endpoint untuk mendapatkan readme dari repositori
    @GET("repos/{owner}/{repo}/readme")
    suspend fun getReadme(
        @Path("owner") owner: String, @Path("repo") repo: String
    ): Response<ReadmeResponse>

    // Endpoint Get Repository by Owner and Repo Name
    @GET("repos/{owner}/{repo}")
    suspend fun getRepo(
        @Path("owner") owner: String, @Path("repo") repo: String
    ): Response<ReposResponse>

}