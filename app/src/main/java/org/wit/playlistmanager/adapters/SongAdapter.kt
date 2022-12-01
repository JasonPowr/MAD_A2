package org.wit.playlistmanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.wit.playlistmanager.databinding.CardSongBinding
import org.wit.playlistmanager.models.playlist.PlaylistModel
import org.wit.playlistmanager.models.song.SongModel


interface SongListener {
    fun onSongPressed(song: SongModel)
    fun onEditButtonPressed(song: SongModel)
}

class SongAdapter(private var songs: List<SongModel>,
                  private val listener: SongListener) :
    RecyclerView.Adapter<SongAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardSongBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val song = songs[holder.adapterPosition]
        holder.bind(song,listener)
    }

    override fun getItemCount(): Int = songs.size

    class MainHolder(private val binding : CardSongBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(song: SongModel, listener: SongListener) {
            binding.songTitle.text = song.title
            binding.songArtist.text = song.artist
            binding.root.setOnClickListener { listener.onSongPressed(song) }
            binding.editSong.setOnClickListener { listener.onEditButtonPressed(song) }
        }
    }
}