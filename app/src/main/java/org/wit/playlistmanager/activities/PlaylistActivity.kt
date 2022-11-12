package org.wit.playlistmanager.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import org.wit.playlistmanager.R
import org.wit.playlistmanager.databinding.ActivityPlaylistBinding
import org.wit.playlistmanager.main.MainApp
import org.wit.playlistmanager.models.playlist.PlaylistModel
import org.wit.playlistmanager.models.song.SongModel

class PlaylistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlaylistBinding

    var playlist = PlaylistModel()
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = "Add a Playlist"
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp

        if (intent.hasExtra("playlist_edit")) {
            playlist = intent.extras?.getParcelable("playlist_edit")!!
            binding.playlistName.setText(playlist.name)

            binding.btnAdd.text = "Save"
            binding.btnAdd.setOnClickListener() {
                playlist.name = binding.playlistName.text.toString()
                if (playlist.name.isNotEmpty()) {
                    app.playlists.update(playlist.copy())
                    setResult(RESULT_OK)
                    finish()
                }
                else {
                    Snackbar.make(it,"Please Enter a title", Snackbar.LENGTH_LONG)
                        .show()
                }
            }

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_playlist, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}