package com.kwakujonah.spotifymooder

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.github.ybq.android.spinkit.SpinKitView
import com.kwakujonah.spotifymooder.interfaces.ConnectivityApi
import com.kwakujonah.spotifymooder.databinding.ActivityListsBinding
import com.kwakujonah.spotifymooder.network.Interfaces
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Retrofit

class ListsActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityListsBinding

    private lateinit var interfaces : Interfaces
    lateinit var spinKitView : SpinKitView
    lateinit var contentLay : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListsBinding.inflate(layoutInflater)
        interfaces = Interfaces()
        setContentView(binding.root)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.offwhite)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        spinKitView = findViewById(R.id.spin_kit)
        contentLay = findViewById(R.id.contentLay)

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
                        getPlaylist(jsonArray["access_token"] as String)
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

    private fun getPlaylist(bearer: String){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.spotify.com/v1/browse/categories/")
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
                            Log.d("Result", jsonArray["playlists"].toString())
                        }else{
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