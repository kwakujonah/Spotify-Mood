package com.kwakujonah.spotifymooder

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.ybq.android.spinkit.SpinKitView
import com.kwakujonah.spotifymooder.adapter.PlaylistAdapter
import com.kwakujonah.spotifymooder.databinding.ActivityListsBinding
import com.kwakujonah.spotifymooder.interfaces.ConnectivityApi
import com.kwakujonah.spotifymooder.model.Playlist
import com.kwakujonah.spotifymooder.network.Interfaces
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit

class ListsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListsBinding

    private lateinit var interfaces : Interfaces
    lateinit var spinKitView : SpinKitView
    lateinit var contentLay : LinearLayout
    lateinit var playlistsRv : RecyclerView

    private val playlists = ArrayList<Playlist>()
    private lateinit var playlistsAdapter: PlaylistAdapter
    lateinit var mood: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListsBinding.inflate(layoutInflater)
        interfaces = Interfaces()
        setContentView(binding.root)

        mood = intent.getStringExtra("mood-message").toString()
        Log.d("Intent Message", mood)


        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Playlists"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        spinKitView = findViewById(R.id.spin_kit)
        contentLay = findViewById(R.id.contentLay)
        playlistsRv = findViewById(R.id.playlistsRv)

        val itemOnClick: (Int) -> Unit = { position ->
            Log.d("Selected playlist", playlists[position].getURL())

            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(playlists[position].getURL())
            startActivity(intent)
        }

        playlistsAdapter = PlaylistAdapter(playlists,  itemClickListener = itemOnClick)
        val layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        playlistsRv.layoutManager = layoutManager
        playlistsRv.adapter = playlistsAdapter



        getToken()

    }

    private fun getToken(){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://accounts.spotify.com/")
            .build()

        val service = retrofit.create(ConnectivityApi::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.createToken("client_credentials")
            withContext(Dispatchers.Main){
                if(response.isSuccessful ){
                    response.body()?.let {
                        val rrt: String = it.string()
                        var jsonArray = JSONObject(rrt)
                        Log.d("Result", jsonArray["access_token"] as String)
                        getPlaylist(jsonArray["access_token"] as String, mood)
                    }
                }else{
                    Log.d("Result", response.code().toString())
                }
            }
        }
    }

    private fun getHeaderMap(bearer: String): Map<String, String> {
        val headerMap = mutableMapOf<String, String>()
        headerMap["Authorization"] = "Bearer $bearer"
        headerMap["Content-Type"] = "application/json"
        return headerMap
    }

    private fun getPlaylist(bearer: String, category :String){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.spotify.com/v1/browse/categories/$category/")
            .build()

        val service = retrofit.create(ConnectivityApi::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getPlaylists(getHeaderMap(bearer))
            withContext(Dispatchers.Main){
                if(response.isSuccessful ){
                    response.body()?.let {
                        val rrt: String = it.string()
                        var jsonArray = JSONObject(rrt)
                        if(jsonArray.has("playlists")){
                            Log.d("Result", "Found")

                            spinKitView.visibility = View.GONE
                            contentLay.visibility = View.VISIBLE

                            var jsonLists: JSONObject = jsonArray["playlists"] as JSONObject
                            var jsonItems: JSONArray = jsonLists["items"] as JSONArray
                            for (i in 0 until jsonItems.length()){
                                var ddt = jsonItems.getJSONObject(i)

                                var jsonImages: JSONArray = ddt["images"] as JSONArray
                                var jsonUrls: JSONObject = ddt["external_urls"] as JSONObject

                                var lists = Playlist(jsonUrls["spotify"] as String?, ddt["description"].toString(),
                                    jsonImages.getJSONObject(0)["url"] as String?
                                )
                                playlists.add(lists)
                            }
                            playlistsAdapter.notifyDataSetChanged()
                        }else{
                            spinKitView.visibility = View.GONE
                            contentLay.visibility = View.VISIBLE
                            Log.d("Result", "Failed")
                        }
                    }
                }else{
                    Log.d("Result", response.code().toString())
                }
            }
        }
    }

}