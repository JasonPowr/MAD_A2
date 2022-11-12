package org.wit.playlistmanager.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.playlistmanager.adapters.SongAdapter
import org.wit.playlistmanager.databinding.ActivitySongListBinding
import org.wit.playlistmanager.main.MainApp
import org.wit.playlistmanager.models.playlist.PlaylistModel
import org.wit.playlistmanager.models.song.SongModel

class SongListActivity : AppCompatActivity() {
    lateinit var app: MainApp
    private lateinit var binding: ActivitySongListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        var playlist = PlaylistModel()
        if (intent.hasExtra("song_list")) {
            playlist = intent.extras?.getParcelable("song_list")!!
        }

        playlist.songs.removeAt(0)

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = SongAdapter(playlist.songs,this)
    }
}
