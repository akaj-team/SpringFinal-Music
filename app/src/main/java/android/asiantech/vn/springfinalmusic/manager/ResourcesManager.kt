package android.asiantech.vn.springfinalmusic.manager

import android.asiantech.vn.springfinalmusic.R
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
    private var mListAlbum = mutableListOf<String>()
    private var mListPlaylist = mutableListOf<Playlist>()
    fun getallSongFromDevice(): List<Song> {
        return mListSong
    }

    fun loadResources(context: Context) {
        loadAllSong(context)
        loadListArtist()
        loadAllPlaylist(context)
    }

    fun loadAllSong(context: Context) {
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

    private fun loadListArtist() {
        for (song in mListSong) {
            mListArtist.add(song.artist.toString())
            mListAlbum.add(song.album.toString())

        }
        var set: Set<String> = mListAlbum.toSet()
        mListAlbum = set.toMutableList()
        set = mListArtist.toSet()
        mListArtist = set.toMutableList()
        mListArtist.sort()
        mListAlbum.sort()
    }

    fun getListArtist(): MutableList<String> {
        return mListArtist
    }

    fun getListAlbum(): MutableList<String> {
        return mListAlbum
    }

    fun reloadAllSong(context: Context) {
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
        cursor = context.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                project, selection, null, null)
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

    fun loadAllPlaylist(context: Context) {
        val sharePref = context.getSharedPreferences(context.resources.getString(R.string.library_text_playlist), Context.MODE_PRIVATE)
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
        val sharePref = context.getSharedPreferences(context.resources.getString(R.string.library_text_playlist), Context.MODE_PRIVATE)
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
        mListPlaylist = playlist
        for (item in tempListPlaylist) {
            mListPlaylist.add(item)
        }
    }

}
