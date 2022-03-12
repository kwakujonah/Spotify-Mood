package com.kwakujonah.spotifymooder.interfaces

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ConnectivityApi {

    @FormUrlEncoded
    @Headers("Authorization: Basic MTRiYWZiMTRjNDc5NGIzNzgyMGUxYmY3NjhmNmU5MWY6YTYzMGMwNzg0MmM4NGI3ZDgxOGUyMjViOTU5NzZlZjI=", "Content-Type: application/x-www-form-urlencoded")
    @POST("api/token")
    suspend fun createToken(@Field("grant_type") first: String): Response<ResponseBody>


    @GET("party/playlists/")
    suspend fun getPlaylists(@HeaderMap headers: Map<String, String>): Response<ResponseBody>


}