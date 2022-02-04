package com.example.musify

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlaylistAdapter(private val playlists: List<Playlist>) : RecyclerView.Adapter<PlaylistAdapter.ViewHolder>() {
    class ViewHolder internal constructor(itemView : View)
        : RecyclerView.ViewHolder(itemView) {
        var playlistName : TextView = itemView.findViewById(R.id.playlistName)
        var playlistSize : TextView = itemView.findViewById(R.id.playlistSize)
        var playlistImage : ImageView = itemView.findViewById((R.id.playlistImage))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.playlist_cell, parent, false) as View
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val playlist = Repository.playList[position]
        holder.playlistName.text = playlist.name
        holder.playlistSize.text =((playlist.songs.size).toString()+" Songs")
        holder.playlistImage.setImageResource(playlist.picture)

        val context = holder.itemView.context
        holder.itemView.setOnClickListener {
            val intent = Intent(context, PlaylistSongsActivity::class.java).apply {
                putExtra("position", position)
                putExtra("size", playlist.songs.size)
                putExtra("picture", playlist.picture)
                //putExtra("songs", playlist.songs)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

}