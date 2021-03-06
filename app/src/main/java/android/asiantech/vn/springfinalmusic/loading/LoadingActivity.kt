package android.asiantech.vn.springfinalmusic.loading

import android.app.Activity
import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.home.HomeActivity
import android.asiantech.vn.springfinalmusic.manager.ResourcesManager
import android.content.Intent
import android.os.Bundle
import android.os.Handler

class LoadingActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        ResourcesManager.getInstance().loadResources(this)
    }

    override fun onStart() {
        super.onStart()
        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(this@LoadingActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}
