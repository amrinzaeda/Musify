package com.example.musify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import java.util.regex.Pattern
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PlaylistSongsActivity : YouTubeBaseActivity() {
    private lateinit var backButton: ImageButton
    private lateinit var addButton: Button
    private lateinit var playButton: Button
    private lateinit var playlistName: TextView
    private lateinit var playlistSize: TextView
    private lateinit var playlistImage: ImageView

    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist_songs)

        //gets the playlist object that was opened
        val position = intent.extras?.getInt("position")
        val playlist = Repository.playList[position!!]
        Log.d("playsong", ("opening playlist activity: " + playlist.toString()))

        backButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
            }
            startActivity(intent)
        }

        addButton = findViewById(R.id.addSongButton)
        addButton.setOnClickListener {
            val intent = Intent(this, SongDetailsActivity::class.java)
                .putExtra("playlistPosition", position)
            startActivity(intent)
        }
        playButton = findViewById(R.id.playButton)
        playButton.setOnClickListener {
            if (playlist.songs.size != 0) {
                val intent = Intent(this, PlayingSongActivity::class.java).apply {
                putExtra("position", position)
                putExtra("song_name", playlist.songs[0].name)
                putExtra("song_artist", playlist.songs[0].artist)
                putExtra("song_url", playlist.songs[0].url)
            }
            startActivity(intent)
            }
        }

        recyclerView = findViewById(R.id.songsRecyclerView)
        val adapter = SongAdapter(playlist.songs, position)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        addNewSong(adapter, position, playlist.songs)

        playlistName = findViewById(R.id.songsPlaylistName)
        playlistSize = findViewById(R.id.songsPlaylistSize)
        playlistImage = findViewById(R.id.songsPlaylistImage)

        playlistName.text = Repository.playList[position].name.toString()
        val numSongs = Repository.playList[position].songs.size
        if (numSongs == 1) { playlistSize.text = getString(R.string.size_one) }
        else { playlistSize.text = ("$numSongs Songs") }
        playlistImage.setImageResource(R.drawable.empty_playlist)
    }

    // adds a song to songs list in playlist object from repository if user input new song
    private fun addNewSong(adapter: SongAdapter, position: Int, playlist: MutableList<Song>) {
        val name = intent.extras?.getString("songName")
        val artist = intent.extras?.getString("artist")
        val songUrl = intent.extras?.getString("url")

        if (name != null && artist != null && songUrl != null) {
            val picture = "https://img.youtube.com/vi/"+getYoutubeVideoId(songUrl)+"/0.jpg"
            Log.d("songurl", picture)
            playlist.add(Song(name, artist, songUrl, picture))
            Log.d("playlist_songs", ("after adding song: $playlist"))
        }

        adapter.notifyItemChanged(position) // pass it position of new song
    }

    fun getYoutubeVideoId(youtubeUrl: String?): String? {
        var video_id: String? = ""
        if ((youtubeUrl != null && youtubeUrl.trim { it <= ' ' }.length > 0 && youtubeUrl.startsWith(
                "http")
                    || (youtubeUrl!![0] == 'y'))
        ) {
            val expression =
                "^.*((youtu.be" + "\\/)" + "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*" // var regExp = /^.*((youtu.be\/)|(v\/)|(\/u\/\w\/)|(embed\/)|(watch\?))\??v?=?([^#\&\?]*).*/;
            val input: CharSequence = youtubeUrl.trim()
            val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
            val matcher = pattern.matcher(input)
            if (matcher.matches()) {
                val groupIndex1 = matcher.group(7)
                if (groupIndex1 != null && groupIndex1.length == 11) video_id = groupIndex1
            }
        }
        return video_id
    }
}