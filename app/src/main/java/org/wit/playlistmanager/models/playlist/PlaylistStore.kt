package org.wit.playlistmanager.models.playlist

import org.wit.playlistmanager.models.song.SongModel
import org.wit.playlistmanager.models.users.Users

interface PlaylistStore {
    fun findAll(user: Users): List<PlaylistModel>
    fun create(playlist: PlaylistModel, user: Users)
    fun update(playlist: PlaylistModel, user: Users)
    fun delete(playlist: PlaylistModel, user: Users)
    fun addSongToPlaylist(playlist: PlaylistModel, song: SongModel, user: Users)
    fun updateSongInPlaylist(playlist: PlaylistModel, song: SongModel, user: Users)
    fun findAllPlaylistNames(user: Users): ArrayList<String>
    fun findAllSongs(playlist: PlaylistModel, user: Users): List<SongModel>
    fun filterPlaylistNames(playListName: String, user: Users): List<PlaylistModel>
    fun filterSongTitles(songTitle: String, playlist: PlaylistModel, user: Users): List<SongModel>
    fun deleteSongFromPlaylist(song: SongModel,playlist: PlaylistModel, user: Users)
    fun getPlaylistById(id: Long, user: Users): PlaylistModel?
    fun createUsers(user: Users)
    fun returnAllUsers(): List<Users>
    fun findAllUsersPlaylistsAsAArraylist(user: Users): ArrayList<PlaylistModel>
}