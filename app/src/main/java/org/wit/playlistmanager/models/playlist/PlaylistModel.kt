package org.wit.playlistmanager.models.playlist

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.wit.playlistmanager.models.song.SongModel

@Parcelize
data class PlaylistModel(var id:Long = 0,
                         var name: String = "",
                         var songs: ArrayList<SongModel> = arrayListOf(SongModel()),
                         var image: Uri = Uri.EMPTY) : Parcelable
