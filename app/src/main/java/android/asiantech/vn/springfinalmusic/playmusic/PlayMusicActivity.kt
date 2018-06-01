package android.asiantech.vn.springfinalmusic.playmusic

import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.model.Constrant
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
import kotlinx.android.synthetic.main.fragment_play_music.*
import java.util.concurrent.TimeUnit

class PlayMusicActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {

    private var mBroadcastReceiver: BroadcastReceiver? = null
    private var mSongCurrent: Song? = null
    private var mListSong: List<Song>? = null
    private var mPositionSong: Int = -1
    private var mModePlay: Int = Constrant.MODE_NORM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_play_music)
        extraData()
        startMusic()
        initViews()
    }

    private fun startMusic() {
        if (intent.action == Constrant.ACTION_START_SERVICE) {
            startService(Intent(this, MusicService::class.java)
                    .setAction(Constrant.ACTION_PLAY_MUSIC)
                    .putExtra(Constrant.KEY_POSITION_SONG, mPositionSong)
                    .putParcelableArrayListExtra(Constrant.KEY_LIST_SONG, mListSong as ArrayList<out Parcelable>))
        }
    }

    private fun extraData() {
        if (this.intent.extras != null) {
            mListSong = this.intent.extras.getParcelableArrayList(Constrant.KEY_LIST_SONG)
            mPositionSong = this.intent.extras.getInt(Constrant.KEY_POSITION_SONG)
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

    private fun initViews() {
        initReceive()
        displayInfoSong(0)
        seekBarPlayMusic.setOnSeekBarChangeListener(this)
        btnPlayMusicButtonPlay.setOnClickListener {
            starServiceByAction(Constrant.ACTION_PAUSE_MUSIC)
        }
        btnPlayMusicButtonNext.setOnClickListener {
            starServiceByAction(Constrant.ACTION_NEXT_MUSIC)
        }
        btnPlayMusicButtonPrev.setOnClickListener {
            starServiceByAction(Constrant.ACTION_BACK_MUSIC)
        }
        btnPlayMusicClose.setOnClickListener {
            onBackPressed()
        }
        btnPlayMusicButtonRepeat.setOnClickListener {
            changeImageButton()
        }
    }

    private fun changeImageButton() {
        when (mModePlay) {
            Constrant.MODE_NORM -> {
                sendModeToService(Constrant.MODE_REPEAT_ALBUM)
                Toast.makeText(baseContext, Constrant.NAME_MODE_REPEAT_ALBUM, Toast.LENGTH_SHORT).show()
                btnPlayMusicButtonRepeat.background = ContextCompat.getDrawable(baseContext
                        , R.drawable.ic_mode_repeat)
                mModePlay = Constrant.MODE_REPEAT_ALBUM
            }
            Constrant.MODE_REPEAT_ALBUM -> {
                sendModeToService(Constrant.MODE_REPEAT_SONG)
                Toast.makeText(baseContext, Constrant.NAME_MODE_REPEAT_SONG, Toast.LENGTH_SHORT).show()
                btnPlayMusicButtonRepeat.background = ContextCompat.getDrawable(baseContext
                        , R.drawable.ic_mode_repeat_one)
                mModePlay = Constrant.MODE_REPEAT_SONG
            }
            Constrant.MODE_REPEAT_SONG -> {
                sendModeToService(Constrant.MODE_RANDOM_ALBUM)
                Toast.makeText(baseContext, Constrant.NAME_MODE_RANDOM_ALBUM, Toast.LENGTH_SHORT).show()
                btnPlayMusicButtonRepeat.background = ContextCompat.getDrawable(baseContext
                        , R.drawable.ic_mode_mix)
                mModePlay = Constrant.MODE_RANDOM_ALBUM
            }
            Constrant.MODE_RANDOM_ALBUM -> {
                sendModeToService(Constrant.MODE_NORM)
                Toast.makeText(baseContext, Constrant.NAME_MODE_NORM, Toast.LENGTH_SHORT).show()
                btnPlayMusicButtonRepeat.background = ContextCompat.getDrawable(baseContext
                        , R.drawable.ic_mode_norm)
                mModePlay = Constrant.MODE_NORM
            }
        }
    }

    private fun displayMode() {
        when (mModePlay) {
            Constrant.MODE_REPEAT_ALBUM -> {
                btnPlayMusicButtonRepeat.background = ContextCompat.getDrawable(baseContext
                        , R.drawable.ic_mode_repeat)
            }
            Constrant.MODE_REPEAT_SONG -> {
                btnPlayMusicButtonRepeat.background = ContextCompat.getDrawable(baseContext
                        , R.drawable.ic_mode_repeat_one)
            }
            Constrant.MODE_RANDOM_ALBUM -> {
                btnPlayMusicButtonRepeat.background = ContextCompat.getDrawable(baseContext
                        , R.drawable.ic_mode_mix)
            }
            Constrant.MODE_NORM -> {
                btnPlayMusicButtonRepeat.background = ContextCompat.getDrawable(baseContext
                        , R.drawable.ic_mode_norm)
            }
        }
    }

    private fun sendModeToService(mode: Int) {
        startService(Intent(baseContext, MusicService::class.java)
                .setAction(Constrant.ACTION_MODE_CHANGE)
                .putExtra(Constrant.KEY_MODE, mode))
    }

    private fun starServiceByAction(action: String) {
        startService(Intent(this, MusicService::class.java).setAction(action))
    }

    private fun initReceive() {
        mBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val action = intent?.action
                when (action) {
                    Constrant.ACTION_DISPLAY_MUSIC -> {
                        if (intent.extras != null) {
                            mModePlay = intent.extras.getInt(Constrant.KEY_MODE)
                            mSongCurrent = intent.extras.getParcelable(Constrant.KEY_SONG)
                            val strPosition: Int = intent.extras.getInt(Constrant.KEY_POSITION_MEDIA)
                            displayInfoSong(strPosition)
                        }
                    }
                    Constrant.ACTION_PAUSE_MUSIC -> {
                        btnPlayMusicButtonPlay.setOnClickListener {
                            starServiceByAction(Constrant.ACTION_PAUSE_MUSIC)
                        }
                        btnPlayMusicButtonPlay.background = ContextCompat.getDrawable(this@PlayMusicActivity
                                , R.drawable.bt_playpage_button_pause_normal_new)
                    }
                    Constrant.ACTION_RESUME_MUSIC -> {
                        btnPlayMusicButtonPlay.setOnClickListener {
                            starServiceByAction(Constrant.ACTION_RESUME_MUSIC)
                        }
                        btnPlayMusicButtonPlay.background = ContextCompat.getDrawable(this@PlayMusicActivity
                                , R.drawable.bt_play_press)
                    }
                }
            }
        }
        val intent = IntentFilter()
        intent.addAction(Constrant.ACTION_DISPLAY_MUSIC)
        intent.addAction(Constrant.ACTION_PAUSE_MUSIC)
        intent.addAction(Constrant.ACTION_RESUME_MUSIC)
        registerReceiver(mBroadcastReceiver, intent)
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        displayInfoSong(progress)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        startService(Intent(this, MusicService::class.java)
                .setAction(Constrant.ACTION_SEEKBAR_CHANGED)
                .putExtra(Constrant.KEY_PROGRESS, seekBar?.progress))
    }

    override fun onDestroy() {
        unregisterReceiver(mBroadcastReceiver)
        super.onDestroy()
    }
}
