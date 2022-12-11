package org.wit.playlistmanager.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.wit.playlistmanager.helpers.*
import org.wit.playlistmanager.models.playlist.PlaylistModel
import org.wit.playlistmanager.models.playlist.PlaylistStore
import org.wit.playlistmanager.models.song.SongModel
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList

const val JSON_FILE = "playlists.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<PlaylistModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class PlaylistJSONStore(private val context: Context) : PlaylistStore {

    var playlists = mutableListOf<PlaylistModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<PlaylistModel> {
        return playlists
    }

    override fun create(playlist: PlaylistModel) {
        playlist.id = generateRandomId()
        playlists.add(playlist)
        serialize()
    }


    override fun update(playlist: PlaylistModel) {
        val foundPlaylist: PlaylistModel? = playlists.find { p -> p.id == playlist.id }
        if (foundPlaylist != null) {
            foundPlaylist.name = playlist.name
            foundPlaylist.image = playlist.image
            serialize()
        }
    }

    override fun delete(playlist: PlaylistModel) {
        playlists.remove(playlist)
        serialize()
    }

    override fun deleteSongFromPlaylist(song: SongModel, playlist: PlaylistModel) {
        val foundPlaylist: PlaylistModel? = playlists.find { p -> p.id == playlist.id }
        val foundSong: SongModel? = foundPlaylist?.songs?.find { s -> s.songId == song.songId }
        if ((foundPlaylist != null) && (foundSong != null)) {
            foundPlaylist.songs.remove(foundSong)
            serialize()
        }
        print(foundPlaylist)
    }

    override fun getPlaylistById(id: Long): PlaylistModel? {
        playlists = findAll()
        for(playlist in playlists){
            if (playlist.id == id){
                return playlist
            }
        }
        return null
    }

    override fun addSongToPlaylist(playlist: PlaylistModel, song: SongModel) {
        val foundPlaylist: PlaylistModel? = playlists.find { p -> p.id == playlist.id }
        song.songId = generateRandomId()
        foundPlaylist?.songs?.add(song)
        serialize()
    }

    override fun updateSongInPlaylist(playlist: PlaylistModel, song: SongModel) {
        val foundPlaylist: PlaylistModel? = playlists.find { p -> p.id == playlist.id }
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

    override fun findAllPlaylistNames(): ArrayList<String> {
        val playlistNames = ArrayList<String>()
        for(playlist in playlists){
            playlistNames.add(playlist.name)
        }
        return  playlistNames
    }

    override fun findAllSongs(playlist: PlaylistModel): List<SongModel> {
        return playlist.songs
    }

    override fun filterPlaylistNames(playListName: String): List<PlaylistModel> {
        val playlists = findAll()
        val filteredList = mutableListOf<PlaylistModel>()
        for(playlist in playlists){
            if(playlist.name.toLowerCase(Locale.ROOT).contains(playListName.toLowerCase()))
                filteredList.add(playlist)
        }
        return filteredList
    }

    override fun filterSongTitles(songTitle: String, playlist: PlaylistModel): List<SongModel> {
        val songs = findAllSongs(playlist)
        val filteredList = mutableListOf<SongModel>()
        for(song in songs){
            if(song.title.toLowerCase(Locale.ROOT).contains(songTitle.toLowerCase()))
                filteredList.add(song)
        }
        return filteredList
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(playlists, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        playlists = gsonBuilder.fromJson(jsonString, listType)
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