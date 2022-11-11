package org.wit.playlistmanager.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlaylistModel(var id:Long = 0,
                         var name: String,
                         var songs: SongModel) : Parcelable
