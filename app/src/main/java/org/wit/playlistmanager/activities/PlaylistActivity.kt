package org.wit.playlistmanager.activities
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import org.wit.playlistmanager.R
import org.wit.playlistmanager.databinding.ActivityPlaylistBinding
import org.wit.playlistmanager.helpers.showImagePicker
import org.wit.playlistmanager.main.MainApp
import org.wit.playlistmanager.models.playlist.PlaylistModel
import org.wit.playlistmanager.models.song.SongModel
import org.wit.playlistmanager.models.users.Users


class PlaylistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlaylistBinding
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var auth: FirebaseAuth;

    var playlist = PlaylistModel()
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = "Add a Playlist"
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp

        var currentlyAuthenticatedUser = Users("", arrayListOf(PlaylistModel()))
        auth = Firebase.auth
        val currentUser = auth.currentUser

        val users = app.playlists.returnAllUsers()

        for(user in users){
            if (currentUser != null) {
                if(user.UID == currentUser.uid){
                    currentlyAuthenticatedUser = user
                }
            }

        }

        var edit = false
        if (intent.hasExtra("playlist_edit")) {
            edit = true
            playlist = intent.extras?.getParcelable("playlist_edit")!!
            binding.playlistName.setText(playlist.name)
            binding.btnAdd.text = "Save"
            binding.btnAddImage.text = "Edit Image"
            binding.toolbarAdd.title = "Edit "+playlist.name

            if(playlist.image == Uri.EMPTY){
                binding.editImage.layoutParams.height = 300;
                binding.editImage.layoutParams.width = 300;
                binding.editImage.setImageResource(R.drawable.placeholder)
                //https://stackoverflow.com/questions/3144940/set-imageview-width-and-height-programmatically
            }else {
                Picasso.get().load(playlist.image).resize(300,300).into(binding.editImage)
            }
        }

        binding.btnAdd.setOnClickListener() {
            playlist.name = binding.playlistName.text.toString()
            if(playlist.songs.isNotEmpty()){
                playlist.songs.removeAll(listOf(SongModel()).toSet())
            }
            if (binding.playlistName.text.toString().isEmpty()) {
                Snackbar.make(it,"Please enter a name", Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.playlists.update(playlist.copy(),currentlyAuthenticatedUser)
                    setResult(RESULT_OK)
                    finish()
                } else {
                    app.playlists.create(playlist.copy(),currentlyAuthenticatedUser)
                    setResult(RESULT_OK)
                    finish()
                }
            }
        }
        binding.btnAddImage.setOnClickListener {
            showImagePicker(imageIntentLauncher,this)
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
                            val image = result.data!!.data!!
                            contentResolver.takePersistableUriPermission(image,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            playlist.image = image
                            Picasso.get()
                                .load(playlist.image)
                            binding.btnAddImage.setText(R.string.select_playlist_image)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}