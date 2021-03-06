package com.kwakujonah.spotifymooder.interfaces

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ConnectivityApi {

    @FormUrlEncoded
    @Headers("Authorization: Basic ", "Content-Type: application/x-www-form-urlencoded")
    @POST("api/token")
    suspend fun createToken(@Field("grant_type") first: String): Response<ResponseBody>


    @GET("playlists/")
    suspend fun getPlaylists(@HeaderMap headers: Map<String, String>): Response<ResponseBody>

    @GET("tracks")
    suspend fun getPlaylistItems(@HeaderMap headers: Map<String, String>): Response<ResponseBody>

}