package org.wit.playlistmanager.main

import android.app.Application
import org.wit.playlistmanager.models.PlaylistModel
import org.wit.playlistmanager.models.SongModel


class MainApp : Application() {

    val playlists = ArrayList<PlaylistModel>()

    override fun onCreate() {
        super.onCreate()
        playlists.add(PlaylistModel("Playlist One", SongModel()))
        playlists.add(PlaylistModel("Playlist Two", SongModel()))
        playlists.add(PlaylistModel("Playlist Three", SongModel()))
    }
}
