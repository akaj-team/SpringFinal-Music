package android.asiantech.vn.springfinalmusic.home

import android.annotation.SuppressLint
import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.alarm.AlarmDialog
import android.asiantech.vn.springfinalmusic.model.Constant
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    private var mDialogTimer: AlarmDialog? = null
    private var mToast: Toast? = null
    private var mAdapter = HomeAdapter(this)
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
        recycleViewHome.layoutManager = LinearLayoutManager(this)
        recycleViewHome.adapter = mAdapter
        recycleViewHome.setHasFixedSize(true)
    }

    private fun setListeners() {
        btnMenuHomeTimer.setOnClickListener {
            showTimer()
        }
        btnDrawerLayout.setOnClickListener {
            dlHomeDrawerLayout.openDrawer(Gravity.LEFT)
        }
    }

    private fun showTimer() {
        mDialogTimer?.show()
    }

}
