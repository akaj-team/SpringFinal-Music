package android.asiantech.vn.springfinalmusic.online.mode

import com.google.gson.annotations.SerializedName


class SongSearch(
        @SerializedName("errorCode") var errorCode: Int?, // 0
        @SerializedName("errorMsg") var errorMsg: String?, // Kết quả tìm kiếm
        @SerializedName("data") var data: List<SSData?>?,
        @SerializedName("total") var total: Int? // 146
)
