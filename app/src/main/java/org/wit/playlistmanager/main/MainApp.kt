package org.wit.playlistmanager.main

import android.app.Application
import org.wit.playlistmanager.models.PlaylistJSONStore
import org.wit.playlistmanager.models.playlist.PlaylistStore


class MainApp : Application() {
    lateinit var playlists: PlaylistStore
    override fun onCreate() {
        super.onCreate()
        playlists = PlaylistJSONStore(applicationContext)
    }
}