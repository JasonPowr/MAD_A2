package org.wit.playlistmanager.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.wit.playlistmanager.databinding.ActivitySongInfoBinding
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
        var playlist = PlaylistModel()

        if (intent.hasExtra("song_info")) {
            song = intent.extras?.getParcelable("song_info")!!
            playlist = intent.extras?.getParcelable("playlist")!!
        }

        binding.toolbar.title = song.title
        setSupportActionBar(binding.toolbar)

        binding.playlistImage.setImageURI(playlist.image)
        binding.songArist.text = song.artist
        binding.songDuration.text = song.duration.toString()
        binding.songReleaseYear.text = song.releaseYear.toString()
        binding.songWonAward.text = song.wonAward.toString()
    }
}