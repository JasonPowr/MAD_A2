package org.wit.playlistmanager.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import org.wit.playlistmanager.databinding.ActivityMainBinding
import org.wit.playlistmanager.models.SongModel

class SongActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var song = SongModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAdd.setOnClickListener() {
            song.title = binding.songTitle.text.toString()
            if (song.title.isNotEmpty()) {

            }
            else {
                Snackbar
                    .make(it,"Please Enter a title", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }
}