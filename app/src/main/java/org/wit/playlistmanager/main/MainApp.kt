package org.wit.playlistmanager.main

import android.app.Application
import org.wit.playlistmanager.models.PlaylistMemStore


class MainApp : Application() {
    val playlists = PlaylistMemStore()
    override fun onCreate() {
        super.onCreate()
    }
}