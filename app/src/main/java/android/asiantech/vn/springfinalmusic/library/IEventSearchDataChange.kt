package android.asiantech.vn.springfinalmusic.library

import android.asiantech.vn.springfinalmusic.model.Song

interface IEventSearchDataChange {
    fun onSearchComplete(data :List<Song>)
}