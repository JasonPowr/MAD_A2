package org.wit.playlistmanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.wit.playlistmanager.activities.SongListActivity
import org.wit.playlistmanager.databinding.CardSongBinding
import org.wit.playlistmanager.models.song.SongModel

interface SongListener {
}

class SongAdapter(private var songs: ArrayList<SongModel>,
                  private val listener: SongListActivity
) :
    RecyclerView.Adapter<SongAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val song = songs[holder.adapterPosition]
        holder.bind(song,listener)
    }

    override fun getItemCount(): Int = songs.size

    class MainHolder(private val binding : CardSongBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(song: SongModel, listener: SongListActivity) {
            binding.songTitle.text = song.title

//            binding.editPlaylistName.setOnClickListener {listener.onEditButtonClick(playlist)}
        }

    }
}