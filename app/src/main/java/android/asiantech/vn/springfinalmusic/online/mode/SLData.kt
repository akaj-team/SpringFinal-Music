package android.asiantech.vn.springfinalmusic.online.mode

import com.google.gson.annotations.SerializedName

class SLData(
        @SerializedName("_type") var type: String?, // song
        @SerializedName("id") var id: Int?, // 1671434
        @SerializedName("title") var title: String?, // Người Lạ Ơi
        @SerializedName("streaming_url") var streamingUrl: String?, // https://109a15170.vws.vegacdn.vn/Z2TX2Tbw_kqtiqK8auF4UQ/1528469822/media2/song/web2/194/1589850/1589850.mp3?v=3
        @SerializedName("current_profile") var currentProfile: String? // 320kbps
)
