package android.asiantech.vn.springfinalmusic.online.mode

import com.google.gson.annotations.SerializedName

data class SongListen(
        @SerializedName("errorCode") var errorCode: Int?, // 0
        @SerializedName("errorMsg") var errorMsg: String?, // Success
        @SerializedName("data") var data: SLData?
)
