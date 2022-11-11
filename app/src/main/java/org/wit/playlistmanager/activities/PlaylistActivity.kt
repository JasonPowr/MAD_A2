package org.wit.playlistmanager.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import org.wit.playlistmanager.databinding.ActivityPlaylistBinding
import org.wit.playlistmanager.main.MainApp
import org.wit.playlistmanager.models.PlaylistModel
import org.wit.playlistmanager.models.SongModel

class PlaylistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlaylistBinding

    var playlist = PlaylistModel(0,"", SongModel())
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        if (intent.hasExtra("playlist_edit")) {
            playlist = intent.extras?.getParcelable("playlist_edit")!!
            binding.playlistName.setText(playlist.name)
        }

        binding.btnAdd.setOnClickListener() {
            playlist.name = binding.playlistName.text.toString()
            if (playlist.name.isNotEmpty()) {
                app.playlists.create(playlist.copy())
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