package org.wit.playlistmanager.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import org.wit.playlistmanager.databinding.ActivityMainBinding
import org.wit.playlistmanager.databinding.ActivityPlaylistBinding
import org.wit.playlistmanager.main.MainApp
import org.wit.playlistmanager.models.playlist.PlaylistModel
import org.wit.playlistmanager.models.song.SongModel

class SongActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var app: MainApp
    var song = SongModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        var playlist = PlaylistModel()
        if (intent.hasExtra("playlist_add_song")) {
            playlist = intent.extras?.getParcelable("playlist_add_song")!!
        }

        binding.toolbarAdd.title = "Add a song to "+playlist.name
        setSupportActionBar(binding.toolbarAdd)

        binding.btnAdd.setOnClickListener() {
            song.title = binding.songTitle.text.toString()
            song.artist = binding.songArtist.text.toString()

            val min = binding.songMinute.text.toString().toDouble()
            val sec = binding.songSecond.text.toString().toDouble()
            song.duration = min + sec
            song.releaseYear = binding.songReleaseYear.text.toString().toInt()
            song.wonAward = binding.songWonAward.isPressed


            if (song.title.isNotEmpty()) {
                app.playlists.addSongToPlaylist(playlist,song.copy())
                setResult(RESULT_OK)
                finish()
            }
            else {
                Snackbar.make(it,"Please Enter a title", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }
}