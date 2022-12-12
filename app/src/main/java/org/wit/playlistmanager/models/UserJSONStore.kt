package org.wit.playlistmanager.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.wit.playlistmanager.helpers.*
import org.wit.playlistmanager.models.playlist.PlaylistModel
import org.wit.playlistmanager.models.playlist.PlaylistStore
import org.wit.playlistmanager.models.song.Location
import org.wit.playlistmanager.models.song.SongModel
import org.wit.playlistmanager.models.users.Users
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList

const val JSON_FILE = "appData.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<Users>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class UserJSONStore(private val context: Context) : PlaylistStore {

    var users = mutableListOf<Users>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun createUsers(user: Users) {
          users.add(user)
    }

    override fun returnAllUsers(): List<Users> {
        return users
    }

    override fun findAll(user: Users): MutableList<PlaylistModel> {
        return user.playlists
    }

    override fun create(playlist: PlaylistModel,user: Users) {
        playlist.id = generateRandomId()
        user.playlists.add(playlist)
        serialize()
    }


    override fun update(playlist: PlaylistModel, user: Users) {
        val foundPlaylist: PlaylistModel? = user.playlists.find { p -> p.id == playlist.id }
        if (foundPlaylist != null) {
            foundPlaylist.name = playlist.name
            foundPlaylist.image = playlist.image
            serialize()
        }
    }

    override fun delete(playlist: PlaylistModel,user: Users) {
        user.playlists.remove(playlist)
        serialize()
    }

    override fun addSongToPlaylist(playlist: PlaylistModel, song: SongModel, user: Users) {
        val foundPlaylist: PlaylistModel? = user.playlists.find { p -> p.id == playlist.id }
        song.songId = generateRandomId()
        foundPlaylist?.songs?.add(song)
        serialize()
        print(user)
    }

    override fun findAllSongs(playlist: PlaylistModel, user: Users): List<SongModel> {
        val foundPlaylist: PlaylistModel? = user.playlists.find { p -> p.id == playlist.id }
        return foundPlaylist!!.songs
    }

    override fun deleteSongFromPlaylist(song: SongModel, playlist: PlaylistModel, user: Users) {
        val foundPlaylist: PlaylistModel? = user.playlists.find { p -> p.id == playlist.id }
        val foundSong: SongModel? = foundPlaylist?.songs?.find { s -> s.songId == song.songId }
        if ((foundPlaylist != null) && (foundSong != null)) {
            foundPlaylist.songs.remove(foundSong)
            serialize()
        }
        print(foundPlaylist)
    }


    override fun getPlaylistById(id: Long, user: Users): PlaylistModel? {
        for(playlist in user.playlists){
            if (playlist.id == id){
                return playlist
            }
        }
        return null
    }

    override fun updateSongInPlaylist(playlist: PlaylistModel, song: SongModel, user: Users) {
        val foundPlaylist: PlaylistModel? = user.playlists.find { p -> p.id == playlist.id }
        val foundSong: SongModel? = foundPlaylist?.songs?.find { s -> s.songId == song.songId }
        if ((foundPlaylist != null) && (foundSong != null)) {
            foundPlaylist.songs[foundPlaylist.songs.indexOf(foundSong)].title = song.title
            foundPlaylist.songs[foundPlaylist.songs.indexOf(foundSong)].artist = song.artist
            foundPlaylist.songs[foundPlaylist.songs.indexOf(foundSong)].durationMin = song.durationMin
            foundPlaylist.songs[foundPlaylist.songs.indexOf(foundSong)].durationSec = song.durationSec
            foundPlaylist.songs[foundPlaylist.songs.indexOf(foundSong)].releaseYear = song.releaseYear
            foundPlaylist.songs[foundPlaylist.songs.indexOf(foundSong)].wonAward = song.wonAward
            foundPlaylist.songs[foundPlaylist.songs.indexOf(foundSong)].lat = song.lat
            foundPlaylist.songs[foundPlaylist.songs.indexOf(foundSong)].lng = song.lng
            foundPlaylist.songs[foundPlaylist.songs.indexOf(foundSong)].zoom = song.zoom
            serialize()
        }
    }

    override fun filterPlaylistNames(playListName: String, user: Users): List<PlaylistModel> {
        val filteredList = mutableListOf<PlaylistModel>()
        for(playlist in user.playlists){
            if(playlist.name.toLowerCase(Locale.ROOT).contains(playListName.toLowerCase()))
                filteredList.add(playlist)
        }
        return filteredList
    }

    override fun returnAllSongLocations(user: Users): ArrayList<Location> {
        var location: Location
        val locations = arrayListOf<Location>()

        for(playlist in user.playlists){
            for(song in playlist.songs) {
                val songLat = song.lat
                val songLng = song.lng
                val songZoom = song.zoom
                location = Location(songLat, songLng, songZoom)
                locations.add(location)
            }
        }
        return locations
    }


    override fun findAllPlaylistNames(user: Users): ArrayList<String> {
        val playlistNames = ArrayList<String>()
        for(playlist in user.playlists){
            playlistNames.add(playlist.name)
        }
        return  playlistNames
    }

    override fun filterSongTitles(songTitle: String, playlist: PlaylistModel, user: Users): List<SongModel> {
        val songs = findAllSongs(playlist,user)
        val filteredList = mutableListOf<SongModel>()
        for(song in songs){
            if(song.title.toLowerCase(Locale.ROOT).contains(songTitle.toLowerCase()))
                filteredList.add(song)
        }
        return filteredList
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(users, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        users = gsonBuilder.fromJson(jsonString, listType)
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}