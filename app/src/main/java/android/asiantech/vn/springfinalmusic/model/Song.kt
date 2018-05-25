package android.asiantech.vn.springfinalmusic.model

data class Song(var id: Int, var artist: String?, var title: String?, var data: String?, var displayName: String?, var duration: Int, var album: String?) {
    constructor() : this(0, null, null, null, null, 0, null) {
    }
}