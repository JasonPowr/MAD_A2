package org.wit.playlistmanager.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.wit.playlistmanager.R
import org.wit.playlistmanager.adapters.PlaylistAdapter
import org.wit.playlistmanager.adapters.PlaylistListener
import org.wit.playlistmanager.databinding.ActivityPlaylistListBinding
import org.wit.playlistmanager.main.MainApp
import org.wit.playlistmanager.models.playlist.PlaylistModel
import org.wit.playlistmanager.models.users.Users


class PlaylistListActivity : AppCompatActivity(), PlaylistListener {
    private lateinit var binding: ActivityPlaylistListBinding
    lateinit var app: MainApp
    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var auth: FirebaseAuth;
    var currentlyAuthenticatedUser = Users("wrongUID", arrayListOf(PlaylistModel()))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaylistListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = "Your Playlists"
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        //https://www.geeksforgeeks.org/navigation-drawer-in-android/
        drawerLayout = findViewById(R.id.my_drawer_layout)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        auth = Firebase.auth
        registerMapCallback()

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

        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_playlists -> {
                    val launcherIntent = Intent(this, PlaylistListActivity::class.java) //https://androidgeek.co/navigation-drawer-and-drawer-layout-in-kotlin-in-depth-guide-103ce411416d
                    startActivity(launcherIntent)
                    true
                }
                R.id.nav_maps -> {
                    val playlists = app.playlists.findAllUsersPlaylistsAsAArraylist(currentlyAuthenticatedUser)
                    val launcherIntent = Intent(this, MapActivity::class.java).putExtra("songs", playlists)
                    mapIntentLauncher.launch(launcherIntent)
                    true
                }
                R.id.nav_logout -> {
                    Firebase.auth.signOut()
                    val launcherIntent = Intent(this, LoginActivity::class.java)
                    startActivity(launcherIntent)
                    true
                }
                else -> {false}
            }
        }

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerViewplaylist.layoutManager = layoutManager
        binding.recyclerViewplaylist.adapter = PlaylistAdapter(app.playlists.findAll(currentlyAuthenticatedUser), this)

        binding.addPlaylist.setOnClickListener{
            val launcherIntent = Intent(this, PlaylistActivity::class.java)
            getClickResult.launch(launcherIntent)
            binding.emptyMessage.text = ""
        }

        if (app.playlists.findAll(currentlyAuthenticatedUser).isNotEmpty()){
            binding.emptyMessage.text = ""
        }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }


    fun filter(filteredNames: List<PlaylistModel>){
        binding.recyclerViewplaylist.adapter = PlaylistAdapter(filteredNames,this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val menuSearchItem = menu.findItem(R.id.search)
        val searchView = menuSearchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                filter(app.playlists.filterPlaylistNames(query, currentlyAuthenticatedUser))
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onEditButtonClick(playlist: PlaylistModel) {
        val launcherIntent = Intent(this, PlaylistActivity::class.java)
        launcherIntent.putExtra("playlist_edit", playlist)
        getClickResult.launch(launcherIntent)
    }

    override fun onAddButtionClick(playlist: PlaylistModel) {
        val launcherIntent = Intent(this, SongActivity::class.java)
        launcherIntent.putExtra("playlist_add_song", playlist)
        launcherIntent.putExtra("song", playlist.songs)
        getClickResult.launch(launcherIntent)
    }

    override fun onPlaylistClick(playlist: PlaylistModel) {
        val launcherIntent = Intent(this, SongListActivity::class.java)
        launcherIntent.putExtra("song_list", playlist)
        getClickResult.launch(launcherIntent)
    }

    override fun onDeleteButtonClick(playlist: PlaylistModel) {
        app.playlists.delete(playlist, currentlyAuthenticatedUser)
        binding.recyclerViewplaylist.adapter = PlaylistAdapter(app.playlists.findAll(currentlyAuthenticatedUser), this)

        if(app.playlists.findAll(currentlyAuthenticatedUser).isEmpty()){
            binding.emptyMessage.text = "Nothing to see here....\nstart by adding a playlist"
        }
    }

    private val getClickResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerViewplaylist.adapter)?.
                notifyItemRangeChanged(0,app.playlists.findAll(currentlyAuthenticatedUser).size)
            }
        }
}
