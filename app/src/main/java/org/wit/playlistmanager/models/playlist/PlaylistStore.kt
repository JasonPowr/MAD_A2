package org.wit.playlistmanager.models.playlist

import org.wit.playlistmanager.models.song.SongModel

interface PlaylistStore {
    fun findAll(): List<PlaylistModel>
    fun create(playlist: PlaylistModel)
    fun update(playlist: PlaylistModel)
    fun addSongToPlaylist(playlist: PlaylistModel, song: SongModel)
    fun updateSongInPlaylist(playlist: PlaylistModel, song: SongModel)
    fun findAllPlaylistNames(): ArrayList<String>
    fun findAllSongs(playlist: PlaylistModel): List<SongModel>
    fun filterPlaylistNames(playListName: String): List<PlaylistModel>
}