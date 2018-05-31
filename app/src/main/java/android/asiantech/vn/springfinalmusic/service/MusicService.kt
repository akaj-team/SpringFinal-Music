package android.asiantech.vn.springfinalmusic.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.timercountdown.FragmentTimerOffApp
import android.asiantech.vn.springfinalmusic.library.adapter.SongsAdapter
import android.asiantech.vn.springfinalmusic.loading.LoadingActivity
import android.asiantech.vn.springfinalmusic.model.Song
import android.asiantech.vn.springfinalmusic.playmusic.PlayMusicActivity
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.*
import android.widget.RemoteViews
import java.util.ArrayList

import java.util.concurrent.TimeUnit

class MusicService : Service(), MediaPlayer.OnCompletionListener {
    companion object {
        const val ID_NOTIFICATION = 1010
        const val PLAY_MUSIC = "play"
        const val PAUSE_MUSIC = "pause"
        const val ERROR_NULL = "null"
        const val NEXT_MUSIC = "next"
        const val BACK_MUSIC = "back"
        const val ACTION_TIMER = "timer_auto_off_app"
        const val RESUME_MUSIC = "continue"
        const val DISPLAY_MUSIC = "show_info_music"
        const val KEY_SONG = "song_current"
        const val KEY_POSITION_MEDIA = "media_current_positon"
    }

    private var mMediaPlayer: MediaPlayer? = null
    private val mHandler = Handler()
    private var mUpdateSongPlaying: UpdateSongPlaying? = null
    private var mRemoteViews: RemoteViews? = null
    private var mNotificationManager: NotificationManager? = null
    private var mNotification: Notification? = null
    private var mCountDownTimer: CountDownTimer? = null
    private lateinit var mSongList: List<Song>
    private var mPositionSong: Int = -1
    private var mSongCurrent: Song? = null

    override fun onCreate() {
        super.onCreate()
        init()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val action = intent.action
        if (action != null) {
            when (action) {
                PLAY_MUSIC -> {
                    if (intent.extras != null) {
                        mSongList = intent.extras.getParcelableArrayList(SongsAdapter.KEY_LIST_SONG)
                        mPositionSong = intent.extras.getInt(SongsAdapter.KEY_POSITION_SONG)
                        mSongCurrent = mSongList[mPositionSong]
                    }
                    playMusic(Uri.parse(mSongCurrent?.data))
                }
                RESUME_MUSIC -> {
                    resumeMusic()
                }
                PAUSE_MUSIC -> {
                    pauseMusic()
                }
                ACTION_TIMER -> {
                    val minutes = intent.getIntExtra(FragmentTimerOffApp.KEY_TIME, -1)
                    autoOffAppByTimer(minutes.toLong())
                }
                NEXT_MUSIC -> {
                    next()
                }
                BACK_MUSIC -> {
                    back()
                }
                PlayMusicActivity.SEEKBAR_CHANGED -> {
                    if (intent.extras != null) {
                        val progress: Int = intent.extras.getInt(PlayMusicActivity.PROGRESS)
                        mMediaPlayer?.seekTo(progress)
                    }
                }
            }
        }
        return Service.START_STICKY
    }

    private fun changedImageBtnPlay() {
        if (mMediaPlayer?.isPlaying == true) {
            mRemoteViews?.setImageViewResource(R.id.imgNotificationButtonPlay
                    , R.drawable.btn_notificationbar_pause)
            mRemoteViews?.setOnClickPendingIntent(R.id.imgNotificationButtonPlay
                    , setActionEventClick(PAUSE_MUSIC))
            sendBroadcast(Intent()
                    .setAction(PAUSE_MUSIC))
        } else {
            mRemoteViews?.setImageViewResource(R.id.imgNotificationButtonPlay
                    , R.drawable.btn_notificationbar_play)
            mRemoteViews?.setOnClickPendingIntent(R.id.imgNotificationButtonPlay
                    , setActionEventClick(RESUME_MUSIC))
            sendBroadcast(Intent()
                    .setAction(RESUME_MUSIC))
        }
        mNotificationManager?.notify(ID_NOTIFICATION, mNotification)
    }

    private fun autoOffAppByTimer(minutes: Long) {
        mCountDownTimer?.cancel()
        mCountDownTimer = object : CountDownTimer(minutes * 1000 * 60, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                if (mMediaPlayer?.isPlaying == true) {
                    mMediaPlayer?.pause()
                }
            }
        }
        mCountDownTimer?.start()
    }

    private fun init() {
        mUpdateSongPlaying = UpdateSongPlaying()
        initMedia()
        initRemoteViews()
    }

    private fun initRemoteViews() {
        mRemoteViews = RemoteViews(this.packageName, R.layout.notification)
        mRemoteViews?.setOnClickPendingIntent(R.id.imgNotificationButtonPlay
                , setActionEventClick(PAUSE_MUSIC))
        mRemoteViews?.setOnClickPendingIntent(R.id.imgNotificationButtonNext
                , setActionEventClick(NEXT_MUSIC))
        mRemoteViews?.setOnClickPendingIntent(R.id.imgNotificationButtonBack
                , setActionEventClick(BACK_MUSIC))
    }

    private fun setNotification() {
        mRemoteViews?.setTextViewText(R.id.tvNotificationNameSong, mSongCurrent?.title)
        mRemoteViews?.setTextViewText(R.id.tvNotificationNameSinger, mSongCurrent?.artist)
        mNotificationManager?.notify(ID_NOTIFICATION, mNotification)
    }

    private fun callBackService(): PendingIntent {
        if (mPositionSong != -1) {
            val intent = Intent(this, PlayMusicActivity::class.java)
                    .putExtra(SongsAdapter.KEY_POSITION_SONG, mPositionSong)
                    .putParcelableArrayListExtra(SongsAdapter.KEY_LIST_SONG, mSongList as ArrayList<out Parcelable>)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            return PendingIntent.getActivities(this, 0, arrayOf(intent), 0)
        }
        return PendingIntent.getActivities(this, 0
                , arrayOf(Intent(this, LoadingActivity::class.java)), 0)
    }

    private fun setActionEventClick(action: String): PendingIntent {
        val intent = Intent(this, MusicService::class.java).setAction(action)
        return PendingIntent.getService(this, 0, intent, 0)
    }

    private fun initNotification() {
        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mNotification = Notification.Builder(this)
                    .setSmallIcon(R.drawable.img_logo)
                    .setContentIntent(callBackService())
                    .setCustomBigContentView(this.mRemoteViews)
                    .build()
        } else {
            mNotification = Notification.Builder(this)
                    .setSmallIcon(R.drawable.ic_music_note_black_24dp)
                    .setContentIntent(callBackService())
                    .build()
            this.mNotification?.bigContentView = mRemoteViews
        }
        startForeground(ID_NOTIFICATION, mNotification)
        mNotificationManager!!.notify(ID_NOTIFICATION, mNotification)
    }

    private fun initMedia() {
        mMediaPlayer = MediaPlayer.create(applicationContext, R.raw.cogaim52)
        mMediaPlayer?.setOnCompletionListener(this)
    }

    private fun playMusic() {
        if (!mMediaPlayer!!.isPlaying) {
            mMediaPlayer!!.start()
            mHandler.post(mUpdateSongPlaying)
        }
    }

    private fun playMusic(uri: Uri) {
        initNotification()
        setNotification()
        mMediaPlayer?.reset()
        mHandler.removeCallbacks(mUpdateSongPlaying)
        mMediaPlayer = MediaPlayer.create(applicationContext, uri)
        if (mMediaPlayer?.isPlaying == false) {
            mMediaPlayer?.start()
            mHandler.post(mUpdateSongPlaying)
        }
        changedImageBtnPlay()
    }

    private fun next() {
        val nextSong: Int = mPositionSong + 1
        if (nextSong >= 0 && nextSong < mSongList.size) {
            mSongCurrent = mSongList[nextSong]
            mPositionSong++
            playMusic(Uri.parse(mSongCurrent?.data))
        }
    }

    private fun back() {
        val nextSong: Int = mPositionSong - 1
        if (nextSong >= 0 && nextSong < mSongList.size) {
            mSongCurrent = mSongList[nextSong]
            mPositionSong--
            playMusic(Uri.parse(mSongCurrent?.data))
        }
    }

    override fun onCompletion(mp: MediaPlayer?) {
    }

    private fun pauseMusic() {
        mMediaPlayer?.pause()
        mHandler.removeCallbacks(mUpdateSongPlaying)
        changedImageBtnPlay()
    }

    private fun resumeMusic() {
        mMediaPlayer?.start()
        mHandler.post(mUpdateSongPlaying)
        changedImageBtnPlay()
    }

    private fun miliSecondsToString(miliseconds: Long?): String {
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
        return ERROR_NULL
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        this.stopSelf()
        mHandler.removeCallbacks(mUpdateSongPlaying)
        mNotificationManager?.cancelAll()
        super.onDestroy()
    }

    internal inner class UpdateSongPlaying : Runnable {
        override fun run() {
            val intCurrPosition: Int? = mMediaPlayer?.currentPosition
            upDateRemote()
            changedImageBtnPlay()
            sendBroadcast(Intent()
                    .setAction(DISPLAY_MUSIC)
                    .putExtra(KEY_SONG, mSongCurrent)
                    .putExtra(KEY_POSITION_MEDIA, intCurrPosition))
            mHandler.postDelayed(mUpdateSongPlaying, 1000)
        }

        private fun upDateRemote() {
            mRemoteViews?.setProgressBar(R.id.progressBarNotification
                    , mMediaPlayer!!.duration
                    , mMediaPlayer!!.currentPosition
                    , false)
            mNotificationManager?.notify(ID_NOTIFICATION, mNotification)
        }
    }
}
