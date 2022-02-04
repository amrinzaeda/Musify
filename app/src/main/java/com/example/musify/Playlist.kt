package com.example.musify

data class Playlist(val name : String, val size : Int, val picture : Int, val songs : MutableList<Song>)
