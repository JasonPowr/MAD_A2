package org.wit.playlistmanager.main

import android.app.Application
import org.wit.playlistmanager.models.PlaylistMemStore
import org.wit.playlistmanager.models.PlaylistModel
import org.wit.playlistmanager.models.SongModel


class MainApp : Application() {

    val playlists = PlaylistMemStore()

    override fun onCreate() {
        super.onCreate()
        playlists.create(PlaylistModel(0,"Pop",SongModel()))
        playlists.create(PlaylistModel(0,"Rock",SongModel()))
    }
}