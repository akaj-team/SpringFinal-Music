package android.asiantech.vn.springfinalmusic.manager

import android.asiantech.vn.springfinalmusic.model.Song
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.util.Log
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

    fun getallSongFromDevice(): List<Song> {
        return mListSong
    }

    fun loadResources(context: Context) {
        loadAllSong(context)
        loadListArtist()
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
        cursor = context.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, project, selection, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val song = Song(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getString(6))
                mListSong.add(song)
            }
            cursor.close()
        }
    }

    fun loadListArtist() {
        for (song in mListSong) {
            mListArtist.add(song.artist)
        }
        mListArtist.toSet();
        mListArtist.toMutableList()
    }

    fun getListArtist(): MutableList<String> {
        return mListArtist
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
        cursor = context.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, project, selection, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val song = Song(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getString(6))
                mListSong.add(song)
            }
            cursor.close()
        }
    }

}
