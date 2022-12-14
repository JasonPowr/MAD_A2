package org.wit.playlistmanager.models.users

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.wit.playlistmanager.models.playlist.PlaylistModel

@Parcelize
data class Users(var UID: String?, var playlists: ArrayList<PlaylistModel>) : Parcelable
