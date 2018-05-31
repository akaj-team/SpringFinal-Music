package android.asiantech.vn.springfinalmusic.manager

import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.model.Album
import android.asiantech.vn.springfinalmusic.model.Playlist
import android.asiantech.vn.springfinalmusic.model.Song
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.text.TextUtils
import com.google.gson.Gson
import java.util.ArrayList

class ResourcesManager private constructor() {
    init {
    }

    companion object {
        private val mInstance = ResourcesManager()
        fun getInstance(): ResourcesManager {
            return mInstance
        }
    }

    private var mListSong = ArrayList<Song>()
    private var mListArtist = mutableListOf<String>()
    private var mListAlbum = mutableListOf<Album>()
    private var mListPlaylist = mutableListOf<Playlist>()
    fun getallSongFromDevice(): List<Song> {
        return mListSong
    }

    fun loadResources(context: Context) {
        loadAllSong(context)
        loadListData(context)
    }

    fun loadAllSong(context: Context) {
        mListSong.clear()
        val selection = MediaStore.Audio.Media.IS_MUSIC + " !=0"
        val project = arrayOf(MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM)
        val cursor: Cursor?
        cursor = context.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, project,
                selection, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val song = Song(cursor.getInt(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3),
                        cursor.getString(4), cursor.getInt(5),
                        cursor.getString(6))
                mListSong.add(song)
            }
            cursor.close()
        }
    }

    private fun loadListData(context: Context) {
        mListArtist.clear()
        mListAlbum.clear()
        val listAlbum = mutableListOf<String>()
        for (song in mListSong) {
            mListArtist.add(song.artist.toString())
            listAlbum.add(song.album.toString())
        }
        initListAlbum(listAlbum)
        mListArtist = mListArtist.toSet().toMutableList()
        mListArtist.sort()
        loadAllPlaylist(context)
    }

    private fun initListAlbum(listAlbum: List<String>) {
        val setAbum = listAlbum.toSet()
        for (item in setAbum.toList()) {
            val album = Album()
            album.name = item
            album.listMusic = mListSong.filter { song: Song -> song.album == album.name }
            mListAlbum.add(album)
        }
    }

    fun getListArtist(): MutableList<String> {
        return mListArtist
    }

    fun getListAlbum(): MutableList<Album> {
        return mListAlbum
    }

    fun loadAllPlaylist(context: Context) {
        mListPlaylist.clear()
        val sharePref = context.getSharedPreferences(context.resources.getString(R.string.library_text_playlist),
                Context.MODE_PRIVATE)
        val data = sharePref.getString(context.getString(R.string.library_text_playlist), "")
        if (!TextUtils.isEmpty(data)) {
            val playlists = data.split("*")
            for (playlist in playlists) {
                val gson = Gson()
                val playlistItem = gson.fromJson(playlist, Playlist::class.java)
                if (playlistItem != null)
                    mListPlaylist.add(playlistItem)
            }
        }
    }

    fun updatePlaylist(context: Context, playlist: Playlist) {
        for (index in mListPlaylist.indices) {
            if (mListPlaylist[index].name == playlist.name) {
                mListPlaylist[index] = playlist
                return
            }
        }
        mListPlaylist.add(playlist)
        saveDataPlaylist(context)
    }

    fun saveDataPlaylist(context: Context) {
        val sharePref = context.getSharedPreferences(context.resources.getString(R.string.library_text_playlist),
                Context.MODE_PRIVATE)
        var data = ""
        val gson = Gson()
        for (index in mListPlaylist.indices) {
            data += gson.toJson(mListPlaylist[index])
            data += "*"
        }
        if (!TextUtils.isEmpty(data.trim())) {
            with(sharePref.edit())
            {
                putString(context.resources.getString(R.string.library_text_playlist), data)
                apply()
            }
        }
    }

    fun setPlaylist(playlist: MutableList<Playlist>) {
        val tempListPlaylist = mListPlaylist.toMutableList()
        playlist.clear()
        mListPlaylist = playlist
        for (item in tempListPlaylist) {
            mListPlaylist.add(item)
        }
    }

    fun getMusicOfArtist(artist: String): List<Song> {
        val listSong: List<Song>
        listSong = mListSong.filter { song -> song.artist == artist }
        return listSong
    }

    fun getAlbum(name: String): Album {
        return mListAlbum.filter { album -> album.name == name }[0]
    }

    fun getPlaylist(name: String): Playlist {
        return mListPlaylist.filter { playlist -> playlist.name == name }[0]
    }

    fun deletePlaylist(playlist: Playlist, context: Context) {
        mListPlaylist.remove(playlist)
        saveDataPlaylist(context)
    }

    fun getDataSearchAll(data: String): List<Song> {
        val listSong: List<Song>
        listSong = mListSong.filter { song ->
            song.title.toLowerCase().contains(data.toLowerCase()) || song.artist.toLowerCase().contains(data.toLowerCase())
                    || song.album.toLowerCase().contains(data.toLowerCase())
        }
        return listSong
    }

    fun getDataSearchPlayList(data: String): MutableList<Playlist> {
        val listPlaylist: List<Playlist>
        listPlaylist = mListPlaylist.filter { playlist ->
            playlist.name.toLowerCase().contains(data.toLowerCase())
        }
        return listPlaylist.toMutableList()
    }

    fun getDataSearchArtist(data: String): List<String> {
        val listArtist: List<String>
        listArtist = mListArtist.filter { listArtist ->
            listArtist.toLowerCase().contains(data.toLowerCase())
        }
        return listArtist
    }

    fun getDataSearchAlbum(data: String): List<Album> {
        val listAlbum: List<Album>
        listAlbum = mListAlbum.filter { listAlbum ->
            listAlbum.name.toLowerCase().contains(data.toLowerCase())
        }
        return listAlbum
    }

}
