package org.wit.playlistmanager.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.wit.playlistmanager.R
import org.wit.playlistmanager.databinding.ActivityPlaylistListBinding
import org.wit.playlistmanager.databinding.CardPlaylistBinding
import org.wit.playlistmanager.models.playlist.PlaylistModel


interface PlaylistListener {
    fun onEditButtonClick(playlist: PlaylistModel)
    fun onAddButtionClick(playlist: PlaylistModel)
    fun onPlaylistClick(playlist: PlaylistModel)
    fun onDeleteButtonClick(playlist: PlaylistModel)
}

class PlaylistAdapter(private var playlists: List<PlaylistModel>,
                      private val listener: PlaylistListener
) :
    RecyclerView.Adapter<PlaylistAdapter.MainHolder>(){
    private lateinit var binding: ActivityPlaylistListBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val CardBinding = CardPlaylistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(CardBinding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val playlist = playlists[holder.adapterPosition]
        holder.bind(playlist,listener)
    }

    override fun getItemCount(): Int = playlists.size


    class MainHolder(private val binding : CardPlaylistBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(playlist: PlaylistModel, listener: PlaylistListener) {
            binding.playlistName.text = playlist.name

            if(playlist.image == Uri.EMPTY){
                binding.playlistImage.setImageResource(R.drawable.placeholder);
            }else {
                Picasso.get().load(playlist.image).into(binding.playlistImage)
            }

            binding.root.setOnClickListener { listener.onPlaylistClick(playlist) }
            binding.editPlaylistName.setOnClickListener {listener.onEditButtonClick(playlist)}
            binding.btnAdd.setOnClickListener{listener.onAddButtionClick(playlist)}
            binding.playlistDelete.setOnClickListener{listener.onDeleteButtonClick(playlist)}
        }

    }
}