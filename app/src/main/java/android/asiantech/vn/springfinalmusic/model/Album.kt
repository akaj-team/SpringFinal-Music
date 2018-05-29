package android.asiantech.vn.springfinalmusic.model

class Album(var name: String, var listMusic: List<Song>?) {
    constructor() : this("", null)
}