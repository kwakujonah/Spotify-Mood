package com.kwakujonah.spotifymooder.model

class Playlist(url: String?, description: String?, image: String?) {

    private var url: String
    private var description: String
    private var image: String

    init {
        this.url = url!!
        this.description = description!!
        this.image = image!!
    }

    fun getURL(): String {
        return url
    }

    fun setURL(name: String?){
        url = name!!
    }

    fun getDescription(): String {
        return description
    }

    fun setDescription(name: String?){
        description = name!!
    }

    fun getImage(): String {
        return image
    }

    fun setImage(name: String?){
        image = name!!
    }

}