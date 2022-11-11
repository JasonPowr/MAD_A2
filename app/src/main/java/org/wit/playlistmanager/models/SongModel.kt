package org.wit.playlistmanager.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SongModel(var artist: String = "",
                     var title: String = "",
                     var duration: Double = 0.00,
                     var releaseYear: Int = 1111,
                     var wonAward: Boolean = false): Parcelable
