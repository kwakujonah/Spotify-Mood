package com.kwakujonah.spotifymooder.model

class Playlist(title: String?, artist: String?, url: String?, duration: String?, image: String?) {

    private var title: String
    private var artist: String
    private var url: String
    private var duration: String
    private var image: String

    init {
        this.title = title!!
        this.artist = artist!!
        this.url = url!!
        this.duration = duration!!
        this.image = image!!
    }

    fun getTitle(): String {
        return title
    }

    fun setTitle(name: String?){
        title = name!!
    }

    fun getArtist(): String {
        return artist
    }

    fun setArtist(name: String?){
        artist = name!!
    }

    fun getURL(): String {
        return url
    }

    fun setURL(name: String?){
        url = name!!
    }

    fun getDuration(): String {
        return duration
    }

    fun setDuration(name: String?){
        duration = name!!
    }

    fun getImage(): String {
        return image
    }

    fun setImage(name: String?){
        image = name!!
    }

}