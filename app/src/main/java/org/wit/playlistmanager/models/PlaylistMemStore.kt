package org.wit.playlistmanager.models

import org.wit.playlistmanager.models.playlist.PlaylistModel
import org.wit.playlistmanager.models.playlist.PlaylistStore
import org.wit.playlistmanager.models.song.SongModel

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class PlaylistMemStore : PlaylistStore {
    val playlists = ArrayList<PlaylistModel>()

    override fun findAll(): List<PlaylistModel> {
        return playlists
    }

    override fun findAllSongs(playlist: PlaylistModel): List<SongModel> {
        return playlist.songs
    }

    override fun create(playlist: PlaylistModel) {
        playlist.id = getId()
        playlists.add(playlist)
    }

    override fun update(playlist: PlaylistModel) {
        val foundPlaylist: PlaylistModel? = playlists.find { p -> p.id == playlist.id }
        if (foundPlaylist != null) {
            foundPlaylist.name = playlist.name
            foundPlaylist.image = playlist.image
        }
    }

    override fun addSongToPlaylist(playlist: PlaylistModel, song: SongModel) {
        val foundPlaylist: PlaylistModel? = playlists.find { p -> p.id == playlist.id }
        song.songId = getId()
        foundPlaylist?.songs?.add(song)
    }

    override fun updateSongInPlaylist(playlist: PlaylistModel, song: SongModel) {
        val foundPlaylist: PlaylistModel? = playlists.find { p -> p.id == playlist.id }
        val foundSong: SongModel? = foundPlaylist?.songs?.find { s -> s.songId == song.songId }
        if ((foundPlaylist != null) && (foundSong != null)) {
            foundPlaylist.songs[foundPlaylist.songs.indexOf(foundSong)].title = song.title
            foundPlaylist.songs[foundPlaylist.songs.indexOf(foundSong)].artist = song.artist
        }
    }

}