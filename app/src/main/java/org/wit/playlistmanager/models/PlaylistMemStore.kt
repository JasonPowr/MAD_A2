package org.wit.playlistmanager.models

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class PlaylistMemStore : PlaylistStore {
    val playlists = ArrayList<PlaylistModel>()

    override fun findAll(): List<PlaylistModel> {
        return playlists
    }

    override fun create(playlist: PlaylistModel) {
        playlist.id = getId()
        playlists.add(playlist)
    }

    override fun update(playlist: PlaylistModel) {
        var foundPlaylist: PlaylistModel? = playlists.find { p -> p.id == playlist.id }
        if (foundPlaylist != null) {
            playlist.name =  playlist.name
        }
    }
}