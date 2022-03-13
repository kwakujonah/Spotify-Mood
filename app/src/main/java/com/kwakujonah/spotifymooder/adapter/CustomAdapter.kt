package com.kwakujonah.spotifymooder.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kwakujonah.spotifymooder.R
import com.squareup.picasso.Picasso

class CustomAdapter(private var itemsList: List<String>, private val itemClickListener: (Int) -> Unit) : RecyclerView.Adapter<CustomAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var itemTextView: TextView = view.findViewById(R.id.textView)
        var itemImageView: ImageView = view.findViewById(R.id.imageView)
        fun bind(itemClickListener: (Int) -> Unit) {
            itemView.setOnClickListener { itemClickListener(adapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.mood_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(itemClickListener)
        when(itemsList[position]){
            "sleep" -> Picasso.get().load(R.drawable.sleep).into(holder.itemImageView)
            "party" -> Picasso.get().load(R.drawable.smile).into(holder.itemImageView)
            "chill" -> Picasso.get().load(R.drawable.sarcastic).into(holder.itemImageView)
            "alternative" -> Picasso.get().load(R.drawable.thinking).into(holder.itemImageView)
        }
        holder.itemTextView.text = itemsList[position]
    }

    override fun getItemCount(): Int {
        return itemsList.count()
    }

}