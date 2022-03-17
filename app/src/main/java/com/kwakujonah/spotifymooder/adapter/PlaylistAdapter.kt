package com.kwakujonah.spotifymooder.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kwakujonah.spotifymooder.R
import com.kwakujonah.spotifymooder.model.Playlist
import com.squareup.picasso.Picasso

internal class PlaylistAdapter (private var playlists : List<Playlist>, private val itemClickListener: (Int) -> Unit): RecyclerView.Adapter<PlaylistAdapter.MyViewHolder>(){

    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView = view.findViewById(R.id.playlistImageIv)
        var title: TextView = view.findViewById(R.id.playlistTitleTv)
        var url: TextView = view.findViewById(R.id.playlistURLTv)
        var duration: TextView = view.findViewById(R.id.playlistDurationTv)
        var artist: TextView = view.findViewById(R.id.playlistArtistTv)
        fun bind(itemClickListener: (Int) -> Unit) {
            itemView.setOnClickListener { itemClickListener(adapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): MyViewHolder {
//        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.playlist_item, false)
//        return MyViewHolder(itemView)

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.playlist_item, parent, false)

        return MyViewHolder(view)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(itemClickListener)
        val listItem = playlists[position]
        holder.title.text = listItem.getTitle()
        holder.artist.text = listItem.getArtist()
        holder.url.text = listItem.getURL()
        holder.duration.text = listItem.getDuration()
        Picasso.get().load(listItem.getImage()).into(holder.image)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

}