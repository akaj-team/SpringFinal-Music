package android.asiantech.vn.springfinalmusic.online

import android.asiantech.vn.springfinalmusic.online.mode.SongSearch
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIMusic {
    @GET("search?&offset=0&type=song&")
    fun getListSongByKeywordSearch(@Query("limit") limit:Int
                                   , @Query("keyword") keyworkSearch: String): Call<SongSearch>

    @GET("song%2Flisten?&profile_id=1")
    fun getSongById(@Query("id") id:Int
                                   , @Query("keyword") keyworkSearch: String): Call<SongSearch>


}
