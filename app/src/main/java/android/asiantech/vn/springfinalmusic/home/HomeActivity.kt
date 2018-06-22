package android.asiantech.vn.springfinalmusic.home

import android.annotation.SuppressLint
import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.alarm.AlarmDialog
import android.asiantech.vn.springfinalmusic.library.LibraryFragment
import android.asiantech.vn.springfinalmusic.model.Constant
import android.asiantech.vn.springfinalmusic.model.Song
import android.asiantech.vn.springfinalmusic.service.MusicService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), IEventItemHomeClick {
    private var mDialogTimer: AlarmDialog? = null
    private var mToast: Toast? = null
    private var mAdapter = HomeAdapter(this, this)
    private var isShowMinibar = false
    private var mIsPause = false
    private lateinit var mCurrentSong: Song
    private lateinit var mReceiver: BroadcastReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_new)
        initViews()
        initBroadcastReceiver()
        setListeners()
        showViews()
        showMinibar(false)
    }

    @SuppressLint("ShowToast")
    private fun initViews() {
        mDialogTimer = AlarmDialog(this, 0)
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT)
        mToast?.setText(Constant.HOME_TOAST_TEXT)
        recycleViewHome?.layoutManager = LinearLayoutManager(this)
        recycleViewHome?.adapter = mAdapter
        recycleViewHome?.setHasFixedSize(true)
    }

    private fun setListeners() {
        btnMenuHomeTimer?.setOnClickListener {
            showTimer()
        }
        btnDrawerLayout?.setOnClickListener {
            dlHomeDrawerLayout.openDrawer(Gravity.LEFT)
        }
        clHomeMiniBar?.setOnClickListener {

        }
        btnPlayMiniBar?.setOnClickListener {
            mIsPause = !mIsPause
            val intent = Intent(this, MusicService::class.java)
            if (mIsPause) {
                intent.action = Constant.ACTION_PAUSE_MUSIC
            } else {
                intent.action = Constant.ACTION_RESUME_MUSIC
            }
            startService(intent)
        }
        btnNextMiniBar?.setOnClickListener {
            val intent = Intent(this, MusicService::class.java)
            intent.action = Constant.ACTION_NEXT_MUSIC
            startService(intent)
        }

        btnPlayingListMiniBar?.setOnClickListener {
            val intent = Intent(this, MusicService::class.java)
            intent.action = Constant.ACTION_SHOW_LIST_CURRENT_MUSIC
            startService(intent)
        }
        clHomeMiniBar?.setOnClickListener {
            startService(Intent(this, MusicService::class.java).setAction(Constant.ACTION_SHOW_CURRENT_MUSIC))
        }
    }

    fun showViews(isShow: Boolean = true) {
        if (isShow) {
            dlHomeDrawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        } else {
            dlHomeDrawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
    }

    private fun showTimer() {
        mDialogTimer?.show()
    }

    override fun onItemHomeClickDone(type: HomeAdapter.ItemHome) {
        when (type) {
            HomeAdapter.ItemHome.LIBRARY -> {
                val libraryFragment = LibraryFragment()
                val fragmentManager = supportFragmentManager
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter, R.anim.out).replace(R.id.coordinatorLayout, libraryFragment)
                        .addToBackStack(LibraryFragment::class.java.simpleName).commit()
                showViews(false)
                dlHomeDrawerLayout?.requestLayout()
            }
            else -> {
                mToast?.show()
            }
        }
    }

    private fun initBroadcastReceiver() {
        mReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val action = intent?.action
                when (action) {
                    Constant.ACTION_DISPLAY_MUSIC -> {
                        if (intent.extras != null) {
                            val strPosition: Int = intent.extras.getInt(Constant.KEY_POSITION_MEDIA)
                            mCurrentSong = intent.extras.getParcelable(Constant.KEY_SONG)
                            if (!isShowMinibar) {
                                showMinibar()
                            }
                            showMinibarInfo(strPosition)
                            val isPlaying = intent.extras.getBoolean(Constant.KEY_PLAYING)
                            val isPause = !isPlaying
                            changeImageButtonPlay(isPause)
                        }
                    }
                    Constant.ACTION_PAUSE_MUSIC -> {
                        changeImageButtonPlay(false)
                    }
                    Constant.ACTION_RESUME_MUSIC -> {
                        changeImageButtonPlay(true)
                    }
                    Constant.ACTION_SONG_IS_CHANGED -> {
                        if (intent.extras != null) {
                            mCurrentSong = intent.extras.getParcelable(Constant.KEY_SONG)
                            showSongInfo(mCurrentSong)
                        }
                    }
                }
            }
        }
        val intent = IntentFilter()
        intent.addAction(Constant.ACTION_DISPLAY_MUSIC)
        intent.addAction(Constant.ACTION_PAUSE_MUSIC)
        intent.addAction(Constant.ACTION_RESUME_MUSIC)
        intent.addAction(Constant.ACTION_SONG_IS_CHANGED)
        registerReceiver(mReceiver, intent)
    }

    private fun showMinibar(isShow: Boolean = true) {
        isShowMinibar = isShow
        if (isShow) {
            clHomeMiniBar?.visibility = View.VISIBLE
            showSongInfo(mCurrentSong)
        } else {
            clHomeMiniBar?.visibility = View.INVISIBLE
        }
    }

    private fun showMinibarInfo(currentTime: Int) {
        val duration = mCurrentSong.duration
        progressBarMiniBar?.progress = (currentTime.toFloat() / duration * 100).toInt()
        tvCurrentTimeMiniBar?.text = String.format("%02d:%02d", currentTime / 1000 / 60, currentTime / 1000 % 60)
    }

    private fun showSongInfo(song: Song) {
        tvNameSingerMiniBar?.text = song.artist
        tvNameSongMiniBar?.text = song.title
    }

    private fun changeImageButtonPlay(isPause: Boolean = false) {
        if (isPause) {
            mIsPause = true
            btnPlayMiniBar?.background = ContextCompat.getDrawable(this, R.drawable.ic_btn_minibar_play_normal)
        } else {
            mIsPause = false
            btnPlayMiniBar?.background = ContextCompat.getDrawable(this, R.drawable.ic_btn_minibar_pause_normal)
        }
    }

    override fun onDestroy() {
        unregisterReceiver(mReceiver)
        super.onDestroy()
    }
}
