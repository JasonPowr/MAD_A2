package org.wit.playlistmanager.models.users

import org.wit.playlistmanager.models.playlist.PlaylistModel

data class Users(var UID: String?, var playlists: ArrayList<PlaylistModel>)
