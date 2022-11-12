package org.wit.playlistmanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.wit.playlistmanager.databinding.CardPlaylistBinding
import org.wit.playlistmanager.models.PlaylistModel

interface PlaylistListener {
    fun onEditButtonClick(playlist: PlaylistModel)
}

class PlaylistAdapter constructor(private var playlists: List<PlaylistModel>,
        private val listener: PlaylistListener) :
    RecyclerView.Adapter<PlaylistAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardPlaylistBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val playlist = playlists[holder.adapterPosition]
        holder.bind(playlist,listener)
    }

    override fun getItemCount(): Int = playlists.size

    class MainHolder(private val binding : CardPlaylistBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(playlist: PlaylistModel, listener: PlaylistListener) {
            binding.playlistName.text = playlist.name
            binding.editPlaylistName.setOnClickListener {listener.onEditButtonClick(playlist)}
        }

    }
}