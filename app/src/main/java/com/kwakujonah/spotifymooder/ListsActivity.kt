package com.kwakujonah.spotifymooder

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.kwakujonah.spotifymooder.databinding.ActivityListsBinding
import com.kwakujonah.spotifymooder.network.Interfaces

class ListsActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityListsBinding

    private lateinit var interfaces : Interfaces

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

        interfaces.getSpotifyToken(applicationContext)
//        if(result != "false" || result != ""){
//            Log.d("df", result)
////            interfaces.getPlaylist(result, applicationContext)
//        }else{
//            Log.d("df", "failed")
////            Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG).show()
//        }

//        Log.d("df", interfaces.getSpotifyToken(applicationContext))



    }

}