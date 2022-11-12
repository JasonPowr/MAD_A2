package org.wit.playlistmanager.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.playlistmanager.R
import org.wit.playlistmanager.adapters.SongAdapter
import org.wit.playlistmanager.databinding.ActivitySongInfoBinding
import org.wit.playlistmanager.databinding.ActivitySongListBinding
import org.wit.playlistmanager.main.MainApp
import org.wit.playlistmanager.models.playlist.PlaylistModel
import org.wit.playlistmanager.models.song.SongModel

class SongInfoActivity : AppCompatActivity() {
    lateinit var app: MainApp
    private lateinit var binding: ActivitySongInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        var song = SongModel()
        if (intent.hasExtra("song_info")) {
            song = intent.extras?.getParcelable("song_info")!!
        }

        binding.toolbar.title = song.title
        setSupportActionBar(binding.toolbar)

        binding.songArist.text = song.artist
        binding.songDuration.text = song.duration.toString()
        binding.songReleaseYear.text = song.releaseYear.toString()
        binding.songWonAward.text = song.wonAward.toString()

    }
}