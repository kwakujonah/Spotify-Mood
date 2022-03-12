package com.kwakujonah.spotifymooder.network

import android.content.Context
import android.service.autofill.UserData
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.kwakujonah.spotifymooder.model.Playlist
import org.json.JSONObject
import java.nio.charset.Charset


class Interfaces {

    private var tokenUrl = "https://accounts.spotify.com/api/token";
    private var playlistUrl = "https://api.spotify.com/v1/browse/categories/pop/playlists/"
    val credentials = "14bafb14c4794b37820e1bf768f6e91f:a630c07842c84b7d818e225b95976ef2"
    val gson = Gson()



}