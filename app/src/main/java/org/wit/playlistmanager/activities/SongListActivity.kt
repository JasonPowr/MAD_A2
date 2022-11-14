package org.wit.playlistmanager.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.playlistmanager.adapters.SongAdapter
import org.wit.playlistmanager.adapters.SongListener
import org.wit.playlistmanager.databinding.ActivitySongListBinding
import org.wit.playlistmanager.main.MainApp
import org.wit.playlistmanager.models.playlist.PlaylistModel
import org.wit.playlistmanager.models.song.SongModel

class SongListActivity : AppCompatActivity(), SongListener {
    lateinit var app: MainApp
    private lateinit var binding: ActivitySongListBinding
    var playlist = PlaylistModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        if (intent.hasExtra("song_list")) {
            playlist = intent.extras?.getParcelable("song_list")!!
        }

        playlist.songs.removeAt(0)
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = SongAdapter(playlist.songs,this)
    }

    override fun onSongPressed(song: SongModel) {
        val launcherIntent = Intent(this, SongInfoActivity::class.java)
        launcherIntent.putExtra("song_info", song)
        launcherIntent.putExtra("playlist", playlist)
        getClickResult.launch(launcherIntent)
    }

    private val getClickResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.playlists.findAll().size)
            }
        }
}
