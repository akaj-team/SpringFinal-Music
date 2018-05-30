package android.asiantech.vn.springfinalmusic.playmusic

import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.library.adapter.SongsAdapter
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
import android.util.Log
import android.widget.SeekBar
import kotlinx.android.synthetic.main.fragment_play_music.*
import java.util.concurrent.TimeUnit

class PlayMusicActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {
    companion object {
        const val SEEKBAR_CHANGED = "on_tracking_touch"
        const val PROGRESS = "seekbar_progress"
    }

    private var mBroadcastReceiver: BroadcastReceiver? = null
    private var mSongCurrent: Song? = null
    private var mListSong: List<Song>? = null
    private var mPositionSong: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_play_music)
        extraData()
        startMusic()
        initViewsAndEvent()
    }

    private fun startMusic() {
        if (intent.action == SongsAdapter.ACTION_START_SERVICE) {
            startService(Intent(this, MusicService::class.java)
                    .setAction(MusicService.PLAY_MUSIC)
                    .putExtra(SongsAdapter.KEY_POSITION_SONG, mPositionSong)
                    .putParcelableArrayListExtra(SongsAdapter.KEY_LIST_SONG, mListSong as ArrayList<out Parcelable>))
        }
    }

    private fun extraData() {
        if (this.intent.extras != null) {
            mListSong = this.intent.extras.getParcelableArrayList(SongsAdapter.KEY_LIST_SONG)
            mPositionSong = this.intent.extras.getInt(SongsAdapter.KEY_POSITION_SONG)
            mSongCurrent = mListSong?.get(mPositionSong)
            Log.e("zzz", "$mPositionSong")
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

    private fun initViewsAndEvent() {
        initReceive()
        displayInfoSong(0)
        seekBarPlayMusic.setOnSeekBarChangeListener(this)
        btnPlayMusicButtonPlay.setOnClickListener {
            starServiceByAction(MusicService.PAUSE_MUSIC)
        }
        btnPlayMusicButtonNext.setOnClickListener {
            starServiceByAction(MusicService.NEXT_MUSIC)
        }
        btnPlayMusicButtonPrev.setOnClickListener {
            starServiceByAction(MusicService.BACK_MUSIC)
        }
        btnPlayMusicClose.setOnClickListener {
            onBackPressed()
        }
        btnPlayMusicButtonPlaylist.setOnClickListener {
            startActivity(Intent(this, ListMusicPlayingFragment::class.java)
                    .putParcelableArrayListExtra(MusicService.KEY_SONG_LIST, mListSong as java.util.ArrayList<out Parcelable>))
        }
    }

    private fun starServiceByAction(action: String) {
        startService(Intent(this, MusicService::class.java).setAction(action))
    }

    private fun initReceive() {
        Log.e("xxx", "m")
        mBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val action = intent?.action
                when (action) {
                    MusicService.DISPLAY_MUSIC -> {
                        if (intent.extras != null) {
                            mSongCurrent = intent.extras.getParcelable(MusicService.KEY_SONG)
                            val strPosition: Int = intent.extras.getInt(MusicService.KEY_POSITION_MEDIA)
                            Log.e("xxx", mSongCurrent?.title)
                            displayInfoSong(strPosition)
                        }
                    }
                    MusicService.PAUSE_MUSIC -> {
                        btnPlayMusicButtonPlay.setOnClickListener {
                            starServiceByAction(MusicService.PAUSE_MUSIC)
                        }
                        btnPlayMusicButtonPlay.background = ContextCompat.getDrawable(this@PlayMusicActivity
                                , R.drawable.bt_playpage_button_pause_normal_new)
                    }
                    MusicService.RESUME_MUSIC -> {
                        btnPlayMusicButtonPlay.setOnClickListener {
                            starServiceByAction(MusicService.RESUME_MUSIC)
                        }
                        btnPlayMusicButtonPlay.background = ContextCompat.getDrawable(this@PlayMusicActivity
                                , R.drawable.bt_play_press)
                    }
                }
            }
        }
        val intent = IntentFilter()
        intent.addAction(MusicService.DISPLAY_MUSIC)
        intent.addAction(MusicService.PAUSE_MUSIC)
        intent.addAction(MusicService.RESUME_MUSIC)
        registerReceiver(mBroadcastReceiver, intent)
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        displayInfoSong(progress)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        startService(Intent(this, MusicService::class.java)
                .setAction(SEEKBAR_CHANGED)
                .putExtra(PROGRESS, seekBar?.progress))
    }

    override fun onDestroy() {
        unregisterReceiver(mBroadcastReceiver)
        super.onDestroy()
    }
}
