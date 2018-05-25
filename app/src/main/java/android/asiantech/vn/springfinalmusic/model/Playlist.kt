package android.asiantech.vn.springfinalmusic.model

data class Playlist(var name: String?, var listSong: MutableList<Song>?) {
    constructor() : this(null, null)
}
