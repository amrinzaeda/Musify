package com.example.musify

class Repository private constructor() {
    companion object {
        val playList = mutableListOf<Playlist>()
    }
}