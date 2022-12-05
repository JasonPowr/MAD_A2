package org.wit.playlistmanager.activities

import android.annotation.SuppressLint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.wit.playlistmanager.R
import org.wit.playlistmanager.databinding.ActivitySongInfoBinding
import org.wit.playlistmanager.main.MainApp
import org.wit.playlistmanager.models.playlist.PlaylistModel
import org.wit.playlistmanager.models.song.SongModel

class SongInfoActivity : AppCompatActivity() {
    lateinit var app: MainApp
    private lateinit var binding: ActivitySongInfoBinding

    @SuppressLint("SetTextI18n")
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

        if(playlist.image == Uri.EMPTY) {
            binding.playlistImage.setImageResource(R.drawable.placeholder)
        }else {
            binding.playlistImage.setImageURI(playlist.image)
        }
        binding.songArist.text = song.artist
        binding.songDuration.text = song.durationMin.toString()+"m "+song.durationSec.toString()+"s"
        binding.songReleaseYear.text = song.releaseYear.toString()
        if(song.wonAward) {
            binding.songWonAward.text = "Yes"
        }else{
            binding.songWonAward.text = "No"
        }
    }
}