package org.wit.playlistmanager.activities


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.playlistmanager.R
import org.wit.playlistmanager.databinding.ActivityPlaylistBinding
import org.wit.playlistmanager.helpers.showImagePicker
import org.wit.playlistmanager.main.MainApp
import org.wit.playlistmanager.models.playlist.PlaylistModel


class PlaylistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlaylistBinding
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>

    var playlist = PlaylistModel()
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = "Add a Playlist"
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp

        var edit = false
        if (intent.hasExtra("playlist_edit")) {
            edit = true
            playlist = intent.extras?.getParcelable("playlist_edit")!!
            binding.playlistName.setText(playlist.name)
            binding.btnAdd.text = "Save"
            binding.btnAddImage.text = "Save Image"

            Picasso.get()
                .load(playlist.image)
        }

        binding.btnAdd.setOnClickListener() {
            playlist.name = binding.playlistName.text.toString()
            if (playlist.name.isEmpty()) {
                Snackbar.make(it,"Please enter a name", Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.playlists.update(playlist.copy())
                } else {
                    app.playlists.create(playlist.copy())
                }
            }
            setResult(RESULT_OK)
            finish()
        }
        binding.btnAddImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }
        registerImagePickerCallback()
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
    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            //i("Got Result ${result.data!!.data}")
                            playlist.image = result.data!!.data!!
                            Picasso.get()
                                .load(playlist.image)
                        }
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}