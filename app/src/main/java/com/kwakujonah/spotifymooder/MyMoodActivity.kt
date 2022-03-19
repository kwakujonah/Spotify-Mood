package com.kwakujonah.spotifymooder

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kwakujonah.spotifymooder.adapter.CustomAdapter
import com.kwakujonah.spotifymooder.databinding.ActivityMyMoodBinding
import com.kwakujonah.spotifymooder.network.Utils

class MyMoodActivity : AppCompatActivity() {

    private val itemsList = ArrayList<String>()
    private lateinit var customAdapter: CustomAdapter

//    private late init var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMyMoodBinding

    lateinit var utils: Utils

    private val positiveButtonClick = { dialog: DialogInterface, _: Int ->
        dialog.cancel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyMoodBinding.inflate(layoutInflater)
        utils = Utils()
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""

        val recyclerView: RecyclerView = findViewById(R.id.recyclerview)
        val itemOnClick: (Int) -> Unit = { position ->
//            Toast.makeText(this,itemsList[position],Toast.LENGTH_SHORT).show()
            if(utils.checkForInternet(applicationContext)){
                val intent = Intent(this, ListsActivity::class.java).apply {
                    putExtra("mood-message", itemsList[position])
                }
                startActivity(intent)
            }else{
                val builder = AlertDialog.Builder(this)

                with(builder)
                {
                    setTitle("No Internet")
                    setMessage("Please connect to an active internet connection to get playlists")
                    setPositiveButton("OK", DialogInterface.OnClickListener(function = positiveButtonClick))
                    show()
                }
            }
        }
        customAdapter = CustomAdapter(itemsList, itemClickListener = itemOnClick)
        val layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = customAdapter
        prepareItems()

    }

    private fun prepareItems() {
        itemsList.add("sleep")
        itemsList.add("party")
        itemsList.add("chill")
        itemsList.add("focus")
        customAdapter.notifyDataSetChanged()
    }

}