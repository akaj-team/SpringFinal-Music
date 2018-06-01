package android.asiantech.vn.springfinalmusic.playmusic

import android.annotation.SuppressLint
import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.model.Constant
import android.asiantech.vn.springfinalmusic.model.Song
import android.asiantech.vn.springfinalmusic.service.MusicService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.SeekBar
import android.widget.Toast
import android.widget.Toast.makeText
import kotlinx.android.synthetic.main.fragment_play_music.*
import java.util.concurrent.TimeUnit

class PlayMusicActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {
    private var mBroadcastReceiver: BroadcastReceiver? = null
    private var mSongCurrent: Song? = null
    private var mListSong: List<Song>? = null
    private var mPositionSong: Int = -1
    private var mModePlay: Int = Constant.MODE_NORM
    private lateinit var mToast: Toast
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_play_music)
        extraData()
        startMusic()
        initViewsAndEvent()
    }

    private fun startMusic() {
        if (intent.action == Constant.ACTION_START_SERVICE) {
            startService(Intent(this, MusicService::class.java)
                    .setAction(Constant.ACTION_PLAY_MUSIC)
                    .putExtra(Constant.KEY_POSITION_SONG, mPositionSong)
                    .putParcelableArrayListExtra(Constant.KEY_LIST_SONG, mListSong as ArrayList<out Parcelable>))
        }
    }

    private fun extraData() {
        if (this.intent.extras != null) {
            mListSong = this.intent.extras.getParcelableArrayList(Constant.KEY_LIST_SONG)
            mPositionSong = this.intent.extras.getInt(Constant.KEY_POSITION_SONG)
            mSongCurrent = mListSong?.get(mPositionSong)
        }
    }

    private fun displayInfoSong(currTime: Int) {
        val duration: Int? = mSongCurrent?.duration
        if (mSongCurrent != null && duration != null) {
            seekBarPlayMusic.max = duration
            tvPlayMusicNameOfSong.text = mSongCurrent?.title
            tvPlayMusicNameOfSinger.text = mSongCurrent?.artist
            tvPlayMusicTotalTime.text = miliSecondsToString(duration.toLong())
            tvPlayMusicCurrentTime.text = miliSecondsToString(currTime.toLong())
            seekBarPlayMusic.progress = currTime
            viewCicleProgressBar.setProgress((currTime.toFloat() / duration * 100))
            displayMode()
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
        initReceive()
        displayInfoSong(0)
        seekBarPlayMusic.setOnSeekBarChangeListener(this)
        btnPlayMusicButtonPlay.setOnClickListener {
            starServiceByAction(Constant.ACTION_PAUSE_MUSIC)
        }
        btnPlayMusicButtonNext.setOnClickListener {
            starServiceByAction(Constant.ACTION_NEXT_MUSIC)
        }
        btnPlayMusicButtonPrev.setOnClickListener {
            starServiceByAction(Constant.ACTION_BACK_MUSIC)
        }
        btnPlayMusicClose.setOnClickListener {
            onBackPressed()
        }
        btnPlayMusicButtonPlaylist.setOnClickListener {
            startActivity(Intent(this, ListMusicPlayingFragment::class.java)
                    .putExtra(Constant.KEY_SONG, mPositionSong)
                    .putParcelableArrayListExtra(Constant.KEY_LIST_SONG, mListSong as java.util.ArrayList<out Parcelable>))
        }
        btnPlayMusicButtonRepeat.setOnClickListener {
            changeImageButton()
        }
        mToast= makeText(baseContext,"",Toast.LENGTH_SHORT)
    }

    private fun changeImageButton() {
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

    private fun starServiceByAction(action: String) {
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
                            mSongCurrent = intent.extras.getParcelable(Constant.KEY_SONG)
                            val strPosition: Int = intent.extras.getInt(Constant.KEY_POSITION_MEDIA)
                            displayInfoSong(strPosition)
                        }
                    }
                    Constant.ACTION_PAUSE_MUSIC -> {
                        btnPlayMusicButtonPlay.setOnClickListener {
                            starServiceByAction(Constant.ACTION_PAUSE_MUSIC)
                        }
                        btnPlayMusicButtonPlay.background = ContextCompat.getDrawable(this@PlayMusicActivity
                                , R.drawable.bt_playpage_button_pause_normal_new)
                    }
                    Constant.ACTION_RESUME_MUSIC -> {
                        btnPlayMusicButtonPlay.setOnClickListener {
                            starServiceByAction(Constant.ACTION_RESUME_MUSIC)
                        }
                        btnPlayMusicButtonPlay.background = ContextCompat.getDrawable(this@PlayMusicActivity
                                , R.drawable.bt_play_press)
                    }
                    Constant.ACTION_SONG_IS_CHANGED -> {
                        mPositionSong = intent.extras.getInt(Constant.KEY_POSITION_SONG)
                    }
                }
            }
        }
        val intent = IntentFilter()
        intent.addAction(Constant.ACTION_DISPLAY_MUSIC)
        intent.addAction(Constant.ACTION_PAUSE_MUSIC)
        intent.addAction(Constant.ACTION_RESUME_MUSIC)
        intent.addAction(Constant.ACTION_SONG_IS_CHANGED)
        registerReceiver(mBroadcastReceiver, intent)
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        displayInfoSong(progress)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        startService(Intent(this, MusicService::class.java)
                .setAction(Constant.ACTION_SEEKBAR_CHANGED)
                .putExtra(Constant.KEY_PROGRESS, seekBar?.progress))
    }

    override fun onDestroy() {
        unregisterReceiver(mBroadcastReceiver)
        super.onDestroy()
    }
}
