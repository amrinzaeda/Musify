package com.example.musify

import android.content.Intent
import android.icu.number.NumberFormatter.with
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class SongAdapter(private val songs: List<Song>, private val playlistPosition: Int) : RecyclerView.Adapter<SongAdapter.ViewHolder>() {
    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var songName : TextView = itemView.findViewById(R.id.songName)
        var artistName : TextView = itemView.findViewById(R.id.artistName)
        var songImage : ImageView = itemView.findViewById(R.id.songImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.song_cell, parent, false) as View
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = songs[position]
        holder.songName.text = song.name
        holder.artistName.text = song.artist
        //holder.songImage.setImageResource(song.image)
        Picasso.get().load(song.image).into(holder.songImage)

        // use this for opening the song when click on the song in the playlist, code goes in the setonclicklistener
       val context = holder.itemView.context
        holder.itemView.setOnClickListener {
            val intent = Intent(context, PlayingSongActivity::class.java).apply {
                putExtra("position", playlistPosition)
                putExtra("song_position", position)
                putExtra("song_name", song.name)
                putExtra("song_artist", song.artist)
                putExtra("song_url", song.url)
                putExtra("song_picture", song.image)
            }
            Log.d("playsong", "going to play song")
            context.startActivity(intent)
       }
    }

    override fun getItemCount(): Int {
        return songs.size
    }
}