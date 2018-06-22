package android.asiantech.vn.springfinalmusic.playmusic

import android.annotation.SuppressLint
import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.alarm.AlarmDialog
import android.asiantech.vn.springfinalmusic.model.Constant
import android.asiantech.vn.springfinalmusic.model.Song
import android.asiantech.vn.springfinalmusic.service.MusicService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.SeekBar
import android.widget.Toast
import android.widget.Toast.makeText
import kotlinx.android.synthetic.main.fragment_play_music.*
import java.util.concurrent.TimeUnit

class PlayMusicActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {
    private var mBroadcastReceiver: BroadcastReceiver? = null
    private var mModePlay: Int = Constant.MODE_NORM
    private lateinit var mCurrentSong: Song
    private lateinit var mToast: Toast
    private var mMinutesUntilFinished: Int? = 0
    private var mIsPause = false
    private var mIsChangeProgress = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_play_music)
        initViewsAndEvent()
        startMusic()
    }

    private fun showDialogTimer(minutesUntilFinished: Int?) {
        val dialog = AlarmDialog(this, minutesUntilFinished)
        dialog.show()
    }

    private fun startMusic() {
        if (intent.action == Constant.KEY_PLAYING) {
            mCurrentSong = intent.extras.getParcelable(Constant.KEY_SONG)
            showSongAttributeText(mCurrentSong)
        } else {
            startService(Intent(this, MusicService::class.java).setAction(Constant.ACTION_GET_CURRENT_SONG))
        }
    }

    private fun displayInfoSong(currentTime: Int) {
        val duration = mCurrentSong.duration
        seekBarPlayMusic.max = 100
        tvPlayMusicTotalTime.text = miliSecondsToString(duration.toLong())
        if (!mIsChangeProgress) {
            seekBarPlayMusic.progress = ((currentTime.toFloat() / duration) * 100).toInt()
            tvPlayMusicCurrentTime.text = miliSecondsToString(currentTime.toLong())
            viewCicleProgressBar.setProgress((currentTime.toFloat() / duration * 100))
        }
        displayMode()
    }

    private fun showSongAttributeText(song: Song) {
        tvPlayMusicNameOfSong.text = song.title
        tvPlayMusicNameOfSinger.text = song.artist
    }

    private fun changeImageButtonPlay(isPause: Boolean) {
        if (isPause) {
            btnPlayMusicButtonPlay.background = ContextCompat.getDrawable(this@PlayMusicActivity
                    , R.drawable.ic_play_circle_outline_black_24dp)
        } else {
            btnPlayMusicButtonPlay.background = ContextCompat.getDrawable(this@PlayMusicActivity
                    , R.drawable.ic_pause_circle_outline_black_24dp)
        }
    }

    private fun miliSecondsToString(miliseconds: Long?): String? {
        if (miliseconds != null) {
            val minutes = TimeUnit.MILLISECONDS.toMinutes(miliseconds)
            val seconds = TimeUnit.MILLISECONDS.toSeconds(miliseconds) % 60

            var strMinutes: String = minutes.toString()
            var strSecons: String = seconds.toString()
            if (minutes < 10) {
                strMinutes = "0$minutes"
            }
            if (seconds < 10) {
                strSecons = "0$seconds"
            }
            return "$strMinutes:$strSecons"
        }
        return null
    }

    @SuppressLint("ShowToast")
    private fun initViewsAndEvent() {
        viewCicleProgressBar.setBitMap(R.drawable.bg_baner_user_info)
        initReceive()
        seekBarPlayMusic.setOnSeekBarChangeListener(this)
        btnPlayMusicButtonPlay.setOnClickListener {
            mIsPause = !mIsPause
            changeImageButtonPlay(mIsPause)
            if (mIsPause) {
                startServiceByAction(Constant.ACTION_PAUSE_MUSIC)
            } else {
                startServiceByAction(Constant.ACTION_RESUME_MUSIC)
            }
        }
        btnPlayMusicButtonNext.setOnClickListener {
            startServiceByAction(Constant.ACTION_NEXT_MUSIC)
        }
        btnPlayMusicButtonPrev.setOnClickListener {
            startServiceByAction(Constant.ACTION_BACK_MUSIC)
        }
        btnPlayMusicClose.setOnClickListener {
            onBackPressed()
        }
        btnPlayMusicButtonPlaylist.setOnClickListener {
            startServiceByAction(Constant.ACTION_SHOW_LIST_CURRENT_MUSIC)
        }
        btnPlayMusicButtonRepeat.setOnClickListener {
            changeStatusBtnMode()
        }
        mToast = makeText(baseContext, "", Toast.LENGTH_SHORT)
        tvPlayMusicTimeCountDown.setOnClickListener {
            showDialogTimer(mMinutesUntilFinished)
        }
    }

    private fun changeStatusBtnMode() {
        when (mModePlay) {
            Constant.MODE_NORM -> {
                sendModeToService(Constant.MODE_REPEAT_ALBUM)
                mToast.setText(Constant.NAME_MODE_REPEAT_ALBUM)
                mToast.show()
                btnPlayMusicButtonRepeat.background = ContextCompat.getDrawable(baseContext
                        , R.drawable.ic_mode_repeat)
                mModePlay = Constant.MODE_REPEAT_ALBUM
            }
            Constant.MODE_REPEAT_ALBUM -> {
                sendModeToService(Constant.MODE_REPEAT_SONG)
                mToast.setText(Constant.NAME_MODE_REPEAT_SONG)
                mToast.show()
                btnPlayMusicButtonRepeat.background = ContextCompat.getDrawable(baseContext
                        , R.drawable.ic_mode_repeat_one)
                mModePlay = Constant.MODE_REPEAT_SONG
            }
            Constant.MODE_REPEAT_SONG -> {
                sendModeToService(Constant.MODE_RANDOM_ALBUM)
                mToast.setText(Constant.NAME_MODE_RANDOM_ALBUM)
                mToast.show()
                btnPlayMusicButtonRepeat.background = ContextCompat.getDrawable(baseContext
                        , R.drawable.ic_mode_mix)
                mModePlay = Constant.MODE_RANDOM_ALBUM
            }
            Constant.MODE_RANDOM_ALBUM -> {
                sendModeToService(Constant.MODE_NORM)
                mToast.setText(Constant.NAME_MODE_NORM)
                mToast.show()
                btnPlayMusicButtonRepeat.background = ContextCompat.getDrawable(baseContext
                        , R.drawable.ic_mode_norm)
                mModePlay = Constant.MODE_NORM
            }
        }
    }

    private fun displayMode() {
        when (mModePlay) {
            Constant.MODE_REPEAT_ALBUM -> {
                btnPlayMusicButtonRepeat.background = ContextCompat.getDrawable(baseContext
                        , R.drawable.ic_mode_repeat)
            }
            Constant.MODE_REPEAT_SONG -> {
                btnPlayMusicButtonRepeat.background = ContextCompat.getDrawable(baseContext
                        , R.drawable.ic_mode_repeat_one)
            }
            Constant.MODE_RANDOM_ALBUM -> {
                btnPlayMusicButtonRepeat.background = ContextCompat.getDrawable(baseContext
                        , R.drawable.ic_mode_mix)
            }
            Constant.MODE_NORM -> {
                btnPlayMusicButtonRepeat.background = ContextCompat.getDrawable(baseContext
                        , R.drawable.ic_mode_norm)
            }
        }
    }

    private fun sendModeToService(mode: Int) {
        startService(Intent(baseContext, MusicService::class.java)
                .setAction(Constant.ACTION_MODE_CHANGE)
                .putExtra(Constant.KEY_MODE, mode))
    }

    private fun startServiceByAction(action: String) {
        startService(Intent(this, MusicService::class.java).setAction(action))
    }

    private fun initReceive() {
        mBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val action = intent?.action
                when (action) {
                    Constant.ACTION_DISPLAY_MUSIC -> {
                        if (intent.extras != null) {
                            mModePlay = intent.extras.getInt(Constant.KEY_MODE)
                            val strPosition: Int = intent.extras.getInt(Constant.KEY_POSITION_MEDIA)
                            mCurrentSong = intent.extras.getParcelable(Constant.KEY_SONG)
                            displayInfoSong(strPosition)
                            val isPlaying = intent.extras.getBoolean(Constant.KEY_PLAYING)
                            val isRotate = viewCicleProgressBar.getIsRotate()
                            mIsPause = !isPlaying
                            changeImageButtonPlay(mIsPause)
                            if (!isRotate && isPlaying) {
                                viewCicleProgressBar.startRotate()
                            }
                        }
                    }
                    Constant.ACTION_PAUSE_MUSIC -> {
                        mIsPause = true
                        changeImageButtonPlay(mIsPause)
                        viewCicleProgressBar.stopRotate()
                    }
                    Constant.ACTION_RESUME_MUSIC -> {
                        mIsPause = false
                        changeImageButtonPlay(mIsPause)
                        viewCicleProgressBar.startRotate()
                    }
                    Constant.ACTION_SONG_IS_CHANGED -> {
                        if (intent.extras != null) {
                            mCurrentSong = intent.extras.getParcelable(Constant.KEY_SONG)
                            showSongAttributeText(mCurrentSong)
                        }
                    }
                    Constant.ACTION_GET_CURRENT_SONG -> {
                        mCurrentSong = intent.extras.getParcelable(Constant.KEY_SONG)
                        showSongAttributeText(mCurrentSong)
                    }
                    Constant.ACTION_TIMER_TICK -> {
                        val miliSeccons = intent.extras.getLong(Constant.KEY_TIME)
                        mMinutesUntilFinished = miliSeccons.toInt() / 1000 / 60
                    }
                    Constant.ACTION_TIMER_FINISHED -> {
                        mMinutesUntilFinished = intent.extras.getLong(Constant.KEY_TIME).toInt()
                    }
                }
            }
        }
        val intent = IntentFilter()
        intent.addAction(Constant.ACTION_DISPLAY_MUSIC)
        intent.addAction(Constant.ACTION_PAUSE_MUSIC)
        intent.addAction(Constant.ACTION_RESUME_MUSIC)
        intent.addAction(Constant.ACTION_SONG_IS_CHANGED)
        intent.addAction(Constant.ACTION_TIMER_TICK)
        intent.addAction(Constant.ACTION_TIMER_FINISHED)
        intent.addAction(Constant.ACTION_GET_CURRENT_SONG)
        registerReceiver(mBroadcastReceiver, intent)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        mIsChangeProgress = true
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (seekBar != null) {
            val currentTime = seekBar.progress * mCurrentSong.duration / 100
            tvPlayMusicCurrentTime.text = miliSecondsToString(currentTime.toLong())
            viewCicleProgressBar.setProgress((currentTime.toFloat() / mCurrentSong.duration * 100))
        }
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        if (seekBar != null) {
            mIsChangeProgress = false
            val currentTime = seekBar.progress * mCurrentSong.duration / 100
            startService(Intent(this, MusicService::class.java)
                    .setAction(Constant.ACTION_SEEKBAR_CHANGED)
                    .putExtra(Constant.KEY_PROGRESS, currentTime))
        }
    }

    override fun onDestroy() {
        unregisterReceiver(mBroadcastReceiver)
        viewCicleProgressBar.stopRotate()
        super.onDestroy()
    }
}
