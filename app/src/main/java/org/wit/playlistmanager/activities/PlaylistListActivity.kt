package org.wit.playlistmanager.activities

import org.wit.playlistmanager.main.MainApp
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.playlistmanager.adapters.PlaylistAdapter
import org.wit.playlistmanager.adapters.PlaylistListener
import org.wit.playlistmanager.databinding.ActivityPlaylistListBinding
import org.wit.playlistmanager.models.playlist.PlaylistModel

class PlaylistListActivity : AppCompatActivity(), PlaylistListener {
    private lateinit var binding: ActivityPlaylistListBinding
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaylistListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = "Your Playlists"
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerViewplaylist.layoutManager = layoutManager
        binding.recyclerViewplaylist.adapter = PlaylistAdapter(app.playlists.findAll(), this)

        binding.addPlaylist.setOnClickListener{
            val launcherIntent = Intent(this, PlaylistActivity::class.java)
            getClickResult.launch(launcherIntent)
        }
    }

    override fun onEditButtonClick(playlist: PlaylistModel) {
        val launcherIntent = Intent(this, PlaylistActivity::class.java)
        launcherIntent.putExtra("playlist_edit", playlist)
        getClickResult.launch(launcherIntent)
    }

    override fun onAddButtionClick(playlist: PlaylistModel) {
        val launcherIntent = Intent(this, SongActivity::class.java)
        launcherIntent.putExtra("playlist_add_song", playlist)
        launcherIntent.putExtra("song", playlist.songs)
        getClickResult.launch(launcherIntent)
    }

    override fun onPlaylistClick(playlist: PlaylistModel) {
        val launcherIntent = Intent(this, SongListActivity::class.java)
        launcherIntent.putExtra("song_list", playlist)
        getClickResult.launch(launcherIntent)
    }

    private val getClickResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerViewplaylist.adapter)?.
                notifyItemRangeChanged(0,app.playlists.findAll().size)
            }
        }
}
