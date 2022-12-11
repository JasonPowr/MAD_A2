package org.wit.playlistmanager.models.playlist

import org.wit.playlistmanager.models.song.Location
import org.wit.playlistmanager.models.song.SongModel

interface PlaylistStore {
    fun findAll(): List<PlaylistModel>
    fun create(playlist: PlaylistModel)
    fun update(playlist: PlaylistModel)
    fun delete(playlist: PlaylistModel)
    fun addSongToPlaylist(playlist: PlaylistModel, song: SongModel)
    fun updateSongInPlaylist(playlist: PlaylistModel, song: SongModel)
    fun findAllPlaylistNames(): ArrayList<String>
    fun findAllSongs(playlist: PlaylistModel): List<SongModel>
    fun filterPlaylistNames(playListName: String): List<PlaylistModel>
    fun filterSongTitles(songTitle: String, playlist: PlaylistModel): List<SongModel>
    fun deleteSongFromPlaylist(song: SongModel,playlist: PlaylistModel)
    fun getPlaylistById(id: Long): PlaylistModel?
    fun returnAllSongLocations(): ArrayList<Location>
}