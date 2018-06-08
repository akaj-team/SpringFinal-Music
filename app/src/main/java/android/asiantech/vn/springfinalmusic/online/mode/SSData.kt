package android.asiantech.vn.springfinalmusic.online.mode

import com.google.gson.annotations.SerializedName

class SSData(
        @SerializedName("_type") var type: String?, // song
        @SerializedName("id") var id: Int?, // 1682540
        @SerializedName("title") var title: String?, // Người Âm Phủ
        @SerializedName("artist_id") var artistId: String?, // 102467
        @SerializedName("image_url") var imageUrl: String?, // https://109cdf7de.vws.vegacdn.vn/kv0puCNE4oNNfn7YhOpK/1528423313/v1/artists/s3/0/0/100/102467.jpg
        @SerializedName("image_url_large") var imageUrlLarge: String?, // https://109cdf7de.vws.vegacdn.vn/kv0puCNE4oNNfn7YhOpK/1528423313/v1/artists/s1/0/0/100/102467.jpg
        @SerializedName("artist_title") var artistTitle: String?, // Osad
        @SerializedName("video_id") var videoId: Int?, // 0
        @SerializedName("active_message") var activeMessage: String?,
        @SerializedName("share_message") var shareMessage: String?, // https://nhac.vn/nguoi-am-phu-osad-soeGPV4?type=SONG&id=1682540&title=Người Âm Phủ
        @SerializedName("block_download") var blockDownload: Int?, // 0
        @SerializedName("block_view") var blockView: Int?, // 0
        @SerializedName("message_block") var messageBlock: String?
)
