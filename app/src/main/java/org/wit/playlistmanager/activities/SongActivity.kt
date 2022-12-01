package org.wit.playlistmanager.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import org.wit.playlistmanager.databinding.ActivityMainBinding
import org.wit.playlistmanager.main.MainApp
import org.wit.playlistmanager.models.playlist.PlaylistModel
import org.wit.playlistmanager.models.song.SongModel

class SongActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var app: MainApp
    var song = SongModel()

    var playlist = PlaylistModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        app = application as MainApp

        binding.toolbarAdd.title = "Add a song to "+playlist.name
        setSupportActionBar(binding.toolbarAdd)

        var edit = false

        if (intent.hasExtra("playlist_add_song")) {
            playlist = intent.extras?.getParcelable("playlist_add_song")!!
            print(playlist)
        }else if(intent.hasExtra("song_edit_playlist") && intent.hasExtra("song_edit")){
            playlist = intent.extras?.getParcelable("song_edit_playlist")!!
            edit = true
            binding.btnAdd.text = "Update song"
            song = intent.extras?.getParcelable("song_edit")!!
            binding.toolbarAdd.title = "Update Song"
            binding.songTitle.setText(song.title)
            binding.songArtist.setText(song.artist)
        }

        binding.btnAdd.setOnClickListener() {
            song.title = binding.songTitle.text.toString()
            song.artist = binding.songArtist.text.toString()
            if (song.title.isEmpty()) {
                Snackbar.make(it, "Please Enter a title", Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.playlists.updateSongInPlaylist(playlist, song.copy())
                    val launcherIntent = Intent(this, PlaylistListActivity::class.java)
                    startActivity(launcherIntent)
                }else{
                    app.playlists.addSongToPlaylist(playlist, song.copy())
                }
            }
            setResult(RESULT_OK)
            finish()
        }

    }
}