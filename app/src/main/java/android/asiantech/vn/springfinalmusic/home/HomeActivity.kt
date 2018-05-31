package android.asiantech.vn.springfinalmusic.home

import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.library.LibraryActivity
import android.asiantech.vn.springfinalmusic.timercountdown.FragmentTimerOffApp
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setListeners()
    }

    private fun setListeners() {
        btnHomeButtonLibrary.setOnClickListener {
            val intent = Intent(this@HomeActivity, LibraryActivity::class.java)
            startActivity(intent)
        }
        btnTimer.setOnClickListener {
            showTimer()
        }
        btnDrawerLayout.setOnClickListener {
            dlHomeDrawerLayout.openDrawer(Gravity.LEFT)
        }
    }

    private fun showTimer() {
        val fm = supportFragmentManager
        val userInfoDialog = FragmentTimerOffApp()
        userInfoDialog.showNow(fm, "Hen gio")
    }
}
