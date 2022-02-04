package com.example.musify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var addPlaylistButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpAddPlaylistButton()

        recyclerView = findViewById(R.id.recyclerView)
        val adapter = PlaylistAdapter(Repository.playList)
        recyclerView.adapter = adapter
        layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        // if user made a new playlist
        makeNewPlaylist(adapter)
    }

    private fun makeNewPlaylist(adapter: PlaylistAdapter) {
        val name = intent.extras?.getString("name")
        val position = intent.extras?.getInt("position")

        if (position != null && name != null) {
            val new = Playlist(name, 0, R.drawable.empty_playlist, mutableListOf<Song>())
            Repository.playList.add(new)
            adapter.notifyItemChanged(position)
            Log.d("play", "made playlist")
        }
    }

    private fun setUpAddPlaylistButton() {
        addPlaylistButton = findViewById(R.id.addButton)
        addPlaylistButton.setOnClickListener {
            Log.d("recyclerview size", Repository.playList.size.toString())
            val intent = Intent(this, PlaylistDetailsActivity::class.java).apply {
                putExtra("position", Repository.playList.size) // checked position is correct
            }
            startActivity(intent)
        }
    }
}