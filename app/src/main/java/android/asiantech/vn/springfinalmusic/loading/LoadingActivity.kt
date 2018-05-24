package android.asiantech.vn.springfinalmusic.loading

import android.app.Activity
import android.asiantech.vn.springfinalmusic.R
import android.os.Bundle
import android.os.Handler
import android.util.Log

class LoadingActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
    }

    override fun onStart() {
        super.onStart()
        var handler : Handler = Handler()
        var runnable : Runnable = Runnable {
            Log.e("xxx","xxx")
        }
    }

}