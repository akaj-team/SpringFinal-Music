package android.asiantech.vn.springfinalmusic.online

import android.asiantech.vn.springfinalmusic.model.Constant
import android.asiantech.vn.springfinalmusic.online.mode.SSData
import android.asiantech.vn.springfinalmusic.online.mode.SongSearch
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ParseMusic {
    private var mRetrofit: Retrofit? = null
    private var mAPIMusic: APIMusic? = null

    init {
        mRetrofit = Retrofit.Builder()
                .baseUrl(Constant.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        mAPIMusic = mRetrofit?.create(APIMusic::class.java)
    }

    fun searchSong(tenBaiHat: String, limit: Int): List<SSData?>? {
        var ls: List<SSData?>? = null
        mAPIMusic?.getListSongByKeywordSearch(limit, tenBaiHat)?.enqueue(object : Callback<SongSearch> {
            override fun onFailure(call: Call<SongSearch>?, t: Throwable?) {
                Log.e("xxx", "Network Error ${t?.message}")
            }

            override fun onResponse(call: Call<SongSearch>?, response: Response<SongSearch>?) {
                Log.e("xxx", "Successful${response?.body()}")
                if (response?.isSuccessful == true) {
                    ls = response.body()?.data
                    Log.e("xxx", "Successful${response?.body()?.errorCode}")
                    Log.e("xxx", "Successful${response?.body()?.errorMsg}")
                    Log.e("xxx", "Successful${response?.body()?.total}")
                } else {
                    Log.e("xxx", "Error${response?.code()}")
                }
            }
        })
        return ls
    }
}
