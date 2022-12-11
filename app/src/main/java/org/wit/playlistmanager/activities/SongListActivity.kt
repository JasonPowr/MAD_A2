package org.wit.playlistmanager.activities
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.playlistmanager.R
import org.wit.playlistmanager.adapters.PlaylistAdapter
import org.wit.playlistmanager.adapters.SongAdapter
import org.wit.playlistmanager.adapters.SongListener
import org.wit.playlistmanager.databinding.ActivitySongListBinding
import org.wit.playlistmanager.main.MainApp
import org.wit.playlistmanager.models.playlist.PlaylistModel
import org.wit.playlistmanager.models.song.SongModel

class SongListActivity : AppCompatActivity(), SongListener {
    lateinit var app: MainApp
    private lateinit var binding: ActivitySongListBinding
    lateinit var playlist: PlaylistModel
    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        //https://www.geeksforgeeks.org/navigation-drawer-in-android/
        drawerLayout = findViewById(R.id.my_drawer_layout)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_playlists -> {
                    val launcherIntent = Intent(this, PlaylistListActivity::class.java) //https://androidgeek.co/navigation-drawer-and-drawer-layout-in-kotlin-in-depth-guide-103ce411416d
                    startActivity(launcherIntent)
                    true
                }
                else -> {false}
            }
        }

        if (intent.hasExtra("song_list")) {
            playlist = intent.extras?.getParcelable("song_list")!!
            if (app.playlists.findAllSongs(playlist).isNotEmpty()){
                binding.emptyMessage.text = ""
            }
        }

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerViewSong.layoutManager = layoutManager
        binding.recyclerViewSong.adapter = SongAdapter(app.playlists.findAllSongs(playlist),this)
    }

    fun filter(filteredTitles: List<SongModel>){
        binding.recyclerViewSong.adapter = SongAdapter(filteredTitles, this)
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
                filter(app.playlists.filterSongTitles(query, playlist))
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


    override fun onSongPressed(song: SongModel) {
        val launcherIntent = Intent(this, SongInfoActivity::class.java)
        launcherIntent.putExtra("song_info", song)
        launcherIntent.putExtra("playlist", playlist)
        getClickResult.launch(launcherIntent)
    }

    override fun onEditButtonPressed(song: SongModel) {
        val launcherIntent = Intent(this, SongActivity::class.java)
        launcherIntent.putExtra("song_edit", song)
        launcherIntent.putExtra("song_edit_playlist", playlist)
        getClickResult.launch(launcherIntent)
    }

    override fun onDeleteButtonPressed(song: SongModel) {
        app.playlists.deleteSongFromPlaylist(song, playlist)
        val newPlaylist = app.playlists.getPlaylistById(playlist.id)
        binding.recyclerViewSong.adapter = SongAdapter(app.playlists.findAllSongs(newPlaylist!!),this)

        if (newPlaylist.songs.size == 0){
            binding.emptyMessage.text = "No Songs found...\n start by adding some"
        }
    }

    private val getClickResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerViewSong.adapter)?.
                notifyItemRangeChanged(0,app.playlists.findAll().size)
            }
        }

}
