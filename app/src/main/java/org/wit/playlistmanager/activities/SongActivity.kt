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

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp

        var playlist = PlaylistModel()
        if (intent.hasExtra("playlist_add_song")) {
            playlist = intent.extras?.getParcelable("playlist_add_song")!!
        }

        binding.btnAdd.setOnClickListener() {
            song.title = binding.songTitle.text.toString()
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