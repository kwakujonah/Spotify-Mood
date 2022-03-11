package com.kwakujonah.spotifymooder.network

import android.content.Context
import android.service.autofill.UserData
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.JsonParser
import org.json.JSONObject
import java.nio.charset.Charset


class Interfaces {

    private var tokenUrl = "https://accounts.spotify.com/api/token";
    val gson = Gson()

    private var vales = ""

    fun getSpotifyToken(context: Context):String{

        val queue = Volley.newRequestQueue(context)

        val postRequest = object : StringRequest(
            Method.POST, tokenUrl,
            Response.Listener { response ->
                val jsonParser = JsonParser()
                val jsonObject = jsonParser.parse(response).asJsonObject
                vales = jsonObject.get("access_token").toString()
                getPlaylist(vales, context)
            }, Response.ErrorListener { _ -> vales = "false" }) {

            override fun getBodyContentType(): String {
                return "application/x-www-form-urlencoded; charset=UTF-8"
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val credentials = "14bafb14c4794b37820e1bf768f6e91f:a630c07842c84b7d818e225b95976ef2"
                val base64EncodedCredentials: String =
                    Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)
                val headers: MutableMap<String, String> = HashMap()
                headers["Authorization"] = "Basic $base64EncodedCredentials"
                return headers
            }

            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["grant_type"] = "client_credentials"
                return params
            }
        }

        queue.add(postRequest)

        return vales

    }

    fun getPlaylist(bearer: String, context: Context){
        val queue = Volley.newRequestQueue(context)

        Log.d("Playlists", bearer)

        val postRequest = object : StringRequest(
            Method.GET, "https://api.spotify.com/v1/browse/categories/pop/playlists/",
            Response.Listener { response ->
                Log.d("Playlists", response)
            }, Response.ErrorListener { error -> Log.d("Playlists", error.networkResponse.toString() ) }) {

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                headers["Authorization"] = "Bearer $bearer"
                headers["Content-Type"] = "application/json"
                return headers
            }

        }

        queue.add(postRequest)

    }

}