package org.wit.playlistmanager.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.wit.playlistmanager.databinding.ActivityMainBinding
import org.wit.playlistmanager.main.MainApp
import org.wit.playlistmanager.models.playlist.PlaylistModel
import org.wit.playlistmanager.models.song.Location
import org.wit.playlistmanager.models.song.SongModel
import org.wit.playlistmanager.models.users.Users


class SongActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    lateinit var app: MainApp
    private lateinit var auth: FirebaseAuth;
    var currentlyAuthenticatedUser = Users("wrongUID", arrayListOf(PlaylistModel()))
    var song = SongModel()
    var playlist = PlaylistModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        app = application as MainApp

        binding.toolbarAdd.title = "Add a song to "+playlist.name
        setSupportActionBar(binding.toolbarAdd)

        auth = Firebase.auth
        val currentUser = auth.currentUser
        val users = app.playlists.returnAllUsers()
        for(user in users){
            if (currentUser != null) {
                if(user.UID == currentUser.uid){
                    currentlyAuthenticatedUser = user
                }else if(intent.hasExtra("login") && user.UID == currentUser.uid){
                    currentlyAuthenticatedUser = intent.extras?.getParcelable("login")!!
                    currentlyAuthenticatedUser = user
                }
            }
        }

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
            binding.songPurchaseLocation.text = "Update song purchase location"
            binding.songTitle.setText(song.title)
            binding.songArtist.setText(song.artist)
            binding.songMinute.setText(song.durationMin.toString())
            binding.songSecond.setText(song.durationSec.toString())
            binding.songReleaseYear.setText(song.releaseYear.toString())

            binding.songWonAward.isChecked = song.wonAward
            binding.songWonAward.setOnCheckedChangeListener { _, isChecked ->
                song.wonAward = isChecked
            }
            //https://www.geeksforgeeks.org/switch-in-kotlin/
        }

        binding.songWonAward.setOnCheckedChangeListener { _, isChecked ->
            song.wonAward = isChecked
        }
        //https://www.geeksforgeeks.org/switch-in-kotlin/


        binding.btnAdd.setOnClickListener() {

            if (binding.songTitle.text.toString().isEmpty() || binding.songArtist.text.toString().isEmpty()) {
                Snackbar.make(it, "Song Title and Song artist is required", Snackbar.LENGTH_LONG)
                    .show()
           }
            else if(binding.songMinute.text.toString().toIntOrNull() == null || binding.songSecond.text.toString().toIntOrNull() == null || binding.songReleaseYear.text.toString().toIntOrNull() == null){
                Snackbar.make(it, "Please enter a number for Minutes, Seconds or the release year", Snackbar.LENGTH_LONG)
                    .show()
            }
            else if(binding.songSecond.text.toString().toInt() > 60){
                Snackbar.make(it, "Seconds cannot be greater than 60", Snackbar.LENGTH_LONG)
                    .show()
            }
            else {
                if (edit) {
                    song.title = binding.songTitle.text.toString()
                    song.artist = binding.songArtist.text.toString()
                    song.durationMin = binding.songMinute.text.toString().toInt()
                    song.durationSec = binding.songSecond.text.toString().toInt()
                    song.releaseYear = binding.songReleaseYear.text.toString().toInt()

                    app.playlists.updateSongInPlaylist(playlist, song.copy(), currentlyAuthenticatedUser)
                    setResult(RESULT_OK)
                    finish()
                }else{
                    song.title = binding.songTitle.text.toString()
                    song.artist = binding.songArtist.text.toString()
                    song.durationMin = binding.songMinute.text.toString().toInt()
                    song.durationSec = binding.songSecond.text.toString().toInt()
                    song.releaseYear = binding.songReleaseYear.text.toString().toInt()

                    app.playlists.addSongToPlaylist(playlist, song.copy(),currentlyAuthenticatedUser)
                    setResult(RESULT_OK)
                    finish()
                }
            }
        }
        binding.songPurchaseLocation.setOnClickListener {
            val location = Location(52.245696, -7.139102, 15f)
            if (song.zoom != 0f) {
                location.lat =  song.lat
                location.lng = song.lng
                location.zoom = song.zoom
            }
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }
        registerMapCallback()
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            song.lat = location.lat
                            song.lng = location.lng
                            song.zoom = location.zoom
                        }
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

}