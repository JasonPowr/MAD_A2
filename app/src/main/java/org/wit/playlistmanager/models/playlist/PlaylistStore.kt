package org.wit.playlistmanager.models.playlist

import org.wit.playlistmanager.models.song.SongModel

interface PlaylistStore {
    fun findAll(): List<PlaylistModel>
    fun create(playlist: PlaylistModel)
    fun update(playlist: PlaylistModel)
    fun addSongToPlaylist(playlist: PlaylistModel, song: SongModel)
}