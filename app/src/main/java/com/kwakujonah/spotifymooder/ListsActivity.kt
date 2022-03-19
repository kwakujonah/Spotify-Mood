package com.kwakujonah.spotifymooder

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.ybq.android.spinkit.SpinKitView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kwakujonah.spotifymooder.adapter.PlaylistAdapter
import com.kwakujonah.spotifymooder.databinding.ActivityListsBinding
import com.kwakujonah.spotifymooder.interfaces.ConnectivityApi
import com.kwakujonah.spotifymooder.model.Playlist
import com.kwakujonah.spotifymooder.network.Interfaces
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit
import java.util.*
import java.util.concurrent.TimeUnit

class ListsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListsBinding

    private lateinit var interfaces : Interfaces
    lateinit var spinKitView : SpinKitView
    lateinit var contentLay : LinearLayout
    lateinit var playlistsRv : RecyclerView
    lateinit var imgView : ImageView
    lateinit var playBtn : FloatingActionButton
    private lateinit var bearer : String;

    private val playlists = ArrayList<Playlist>()
    private lateinit var playlistsAdapter: PlaylistAdapter
    lateinit var mood: String
    private lateinit var rand: Random
    lateinit var playUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListsBinding.inflate(layoutInflater)
        interfaces = Interfaces()
        setContentView(binding.root)

        mood = intent.getStringExtra("mood-message").toString()
        Log.d("Intent Message", mood)


        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        spinKitView = findViewById(R.id.spin_kit)
        contentLay = findViewById(R.id.contentLay)
        playlistsRv = findViewById(R.id.playlistsRv)
        imgView = findViewById(R.id.imgView)
        playBtn = findViewById(R.id.playBtn)

        val itemOnClick: (Int) -> Unit = { position ->
            Log.d("Selected playlist", playlists[position].getURL())

            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(playlists[position].getURL())
            startActivity(intent)
        }

        playBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(playUrl)
            startActivity(intent)
        })

        rand = Random()

        playlistsAdapter = PlaylistAdapter(playlists,  itemClickListener = itemOnClick)
        val layoutManager = LinearLayoutManager(applicationContext)
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
                        bearer = jsonArray["access_token"].toString();
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

                            spinKitView.visibility = View.GONE
                            contentLay.visibility = View.VISIBLE

                            var jsonLists: JSONObject = jsonArray["playlists"] as JSONObject
                            var jsonItems: JSONArray = jsonLists["items"] as JSONArray

                            val ddt = rand.nextInt(jsonItems.length())
                            var ddr = jsonItems.getJSONObject(ddt)
                            var jsonImages: JSONArray = ddr["images"] as JSONArray
                            var jsonUrls: JSONObject = ddr["external_urls"] as JSONObject
                            Log.d("Selected Playlist", jsonUrls.getString("spotify"))

                            playUrl = jsonUrls.getString("spotify")

                            getPlaylistItems(ddr["id"] as String)
                            Picasso.get().load(jsonImages.getJSONObject(0)["url"].toString()).into(imgView)
                        }else{
                            spinKitView.visibility = View.GONE
                            contentLay.visibility = View.VISIBLE
                        }
                    }
                }else{
                    Log.d("Result", response.code().toString())
                }
            }
        }
    }

    private fun getPlaylistItems(playlistId:String){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.spotify.com/v1/playlists/$playlistId/")
            .build()

        val service = retrofit.create(ConnectivityApi::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getPlaylistItems(getHeaderMap(bearer))
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val rrt: String = it.string()
                        var jsonArray = JSONObject(rrt)
                        if (jsonArray.has("items")) {
                            var jsonSongLists: JSONArray = jsonArray["items"] as JSONArray
                            (0 until jsonSongLists.length()).forEach {

                                var jsonSongDetails: JSONObject = jsonSongLists.getJSONObject(it)["track"] as JSONObject
                                var jsonAlbumDetails: JSONObject = jsonSongDetails["album"] as JSONObject
                                var jsonImageDetails: JSONArray = jsonAlbumDetails["images"] as JSONArray
                                var jsonArtistDetails: JSONArray = jsonAlbumDetails["artists"] as JSONArray
                                var imgs: JSONObject = jsonImageDetails[0] as JSONObject
                                var artist: JSONObject = jsonArtistDetails[0] as JSONObject

                                var pSongName: String = jsonSongDetails.getString("name")
                                var pSongDuration: String = jsonSongDetails.getString("duration_ms") as String
                                var pSongURL : String = "https://open.spotify.com/track/"+jsonSongDetails.getString("id")
                                var pSongCover: String = imgs.getString("url")
                                var pSongArtist: String = artist.getString("name")

                                var list = Playlist(pSongName, pSongArtist, pSongURL, "", pSongCover)
                                playlists.add(list)
                                playlistsAdapter.notifyDataSetChanged()
                                playBtn.visibility = View.VISIBLE

                            }

                        } else {
                            Log.d("Results", "No items found!!!")
                        }
                    }
                }
            }
        }

    }

}