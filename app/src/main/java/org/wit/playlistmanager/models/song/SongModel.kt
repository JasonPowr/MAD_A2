package org.wit.playlistmanager.models.song

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SongModel(var artist: String = "",
                     var title: String = "",
                     var durationMin: Int = 0,
                     var durationSec: Int = 0,
                     var releaseYear: Int = 1111,
                     var wonAward: Boolean = false,
                     var songId: Long = 0): Parcelable
