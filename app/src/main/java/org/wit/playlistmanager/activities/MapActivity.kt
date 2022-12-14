package org.wit.playlistmanager.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.wit.playlistmanager.R
import org.wit.playlistmanager.databinding.ActivityMapBinding
import org.wit.playlistmanager.main.MainApp
import org.wit.playlistmanager.models.playlist.PlaylistModel
import org.wit.playlistmanager.models.song.Location
import org.wit.playlistmanager.models.users.Users

class MapActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMarkerDragListener,
    GoogleMap.OnMarkerClickListener {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapBinding

    private var location = Location()
    private var locations = arrayListOf<Location>()
    private var playlists = arrayListOf<PlaylistModel>()

    lateinit var app: MainApp
    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var auth: FirebaseAuth;
    var currentlyAuthenticatedUser = Users("wrongUID", arrayListOf(PlaylistModel()))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        binding.toolbar.title = "Map of different Songs"
        setSupportActionBar(binding.toolbar)

        auth = Firebase.auth
        registerMapCallback()

        //https://www.geeksforgeeks.org/navigation-drawer-in-android/
        drawerLayout = findViewById(R.id.my_drawer_layout)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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

        if(intent.hasExtra("songs")){
             playlists = intent.extras?.getParcelableArrayList<PlaylistModel>("songs")!!
                for(playlist in playlists){
                   for(song in playlist.songs){
                       val songLocation = Location(song.lat,song.lng,song.zoom)
                       locations.add(songLocation)
                   }
                }

        }else {
            location = intent.extras?.getParcelable<Location>("location")!!
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
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

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        if(locations.isEmpty()) {
            val loc = LatLng(location.lat, location.lng)
            val options = MarkerOptions()
                .title("Music Shop")
                .snippet("GPS : $loc")
                .draggable(true)
                .position(loc)
            map.setOnMarkerClickListener(this)
            map.addMarker(options)
            map.setOnMarkerDragListener(this)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, location.zoom))
        }else{
                for(playlist in playlists) {
                    for (song in playlist.songs) {
                        val loc = LatLng(song.lat, song.lng)
                        val options = MarkerOptions()
                            .title(song.title)
                            .snippet("GPS : $loc")
                            .draggable(true)
                            .position(loc)
                        map.setOnMarkerClickListener(this)
                        map.addMarker(options)
                        map.setOnMarkerDragListener(this)
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, song.zoom))

                    }
            }
        }
    }

    override fun onMarkerDrag(p0: Marker) {  }


    override fun onMarkerDragEnd(marker: Marker) {
        location.lat = marker.position.latitude
        location.lng = marker.position.longitude
        location.zoom = map.cameraPosition.zoom
    }

    override fun onMarkerDragStart(p0: Marker) {  }

    override fun onBackPressed() {
        val resultIntent = Intent()
        resultIntent.putExtra("location", location)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
        super.onBackPressed()
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val loc = LatLng(location.lat, location.lng)
        marker.snippet = "GPS : $loc"
        return false
    }

}