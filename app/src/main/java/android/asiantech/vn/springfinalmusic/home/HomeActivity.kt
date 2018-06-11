package android.asiantech.vn.springfinalmusic.home

import android.annotation.SuppressLint
import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.library.LibraryActivity
import android.asiantech.vn.springfinalmusic.alarm.AlarmDialog
import android.asiantech.vn.springfinalmusic.model.Constant
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    private var mDialogTimer: AlarmDialog? = null
    private var mToast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initViews()
        setListeners()
    }

    @SuppressLint("ShowToast")
    private fun initViews() {
        mDialogTimer = AlarmDialog(this, 0)
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT)
        mToast?.setText(Constant.HOME_TOAST_TEXT)
    }

    private fun setListeners() {
        btnHomeButtonLibrary.setOnClickListener {
            val intent = Intent(this@HomeActivity, LibraryActivity::class.java)
            startActivity(intent)
        }
        btnMenuHomeTimer.setOnClickListener {
            showTimer()
        }
        btnDrawerLayout.setOnClickListener {
            dlHomeDrawerLayout.openDrawer(Gravity.LEFT)
        }
        btnHomeVideo.setOnClickListener {
            mToast?.show()
        }
        btnHomeDownload.setOnClickListener {
            mToast?.show()
        }
        btnHomeSearchOnline.setOnClickListener {
            mToast?.show()
        }
        btnHomeRanker.setOnClickListener {
            mToast?.show()
        }
    }

    private fun showTimer() {
        mDialogTimer?.show()
    }
}
