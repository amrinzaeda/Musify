package com.example.musify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class SongDetailsActivity : AppCompatActivity() {
    lateinit var nameInput: EditText
    lateinit var artistInput: EditText
    lateinit var urlInput: EditText
    lateinit var returnButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_details)

        val playlistPosition = intent.extras?.getInt("playlistPosition")

        nameInput = findViewById(R.id.getSongName)
        artistInput = findViewById(R.id.getArtistName)
        urlInput = findViewById(R.id.getUrl)

        setReturnButton(playlistPosition!!)
    }

    private fun setReturnButton(p: Int) {
        returnButton = findViewById(R.id.buttonToPlaylist)
        returnButton.setOnClickListener {
            val intent = Intent(this, PlaylistSongsActivity::class.java).apply {
                putExtra("position", p)
                putExtra("songName", nameInput.text.toString())
                putExtra("artist", artistInput.text.toString())
                putExtra("url", urlInput.text.toString())
            }
            startActivity(intent)
        }

    }
}