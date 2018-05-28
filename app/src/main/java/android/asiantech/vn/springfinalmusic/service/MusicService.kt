package android.asiantech.vn.springfinalmusic.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.library.adapter.SongsAdapter
import android.asiantech.vn.springfinalmusic.model.ListSong
import android.asiantech.vn.springfinalmusic.model.Song
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.*
import android.util.Log
import android.widget.RemoteViews

import java.util.concurrent.TimeUnit

class MusicService : Service(), MediaPlayer.OnCompletionListener {
    companion object {
        private val TAG = MusicService::class.java.simpleName
        const val ID_NOTIFICATION = 1010
        const val PLAY_MUSIC = "play"
        const val PAUSE_MUSIC = "pause"
        const val ERROR_NULL = "null"
        const val NEXT_MUSIC = "next"
        const val BACK_MUSIC = "back"
    }

    private var mMediaPlayer: MediaPlayer? = null
    private val mHandler = Handler()
    private var mUpdateSongPlaying: UpdateSongPlaying? = null
    private var mRemoteViews: RemoteViews? = null
    private var mNotificationManager: NotificationManager? = null
    private var mNotification: Notification? = null
    private lateinit var mSongList: ListSong
    private var mPositionCurrent: Int = -1
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
                    mSongList = intent.getParcelableExtra(SongsAdapter.KEY_LIST_SONG)
                    mPositionCurrent = mSongList.songCurrent
                    mSongCurrent = mSongList.songList[mPositionCurrent]
                    playMusic(Uri.parse(mSongCurrent?.data))
                }
                PAUSE_MUSIC -> {
                    pauseMusic()
                }
                NEXT_MUSIC -> {
                    next()
                }
                BACK_MUSIC -> {
                    back()
                }
            }
        }
        return Service.START_STICKY
    }

    private fun init() {
        mUpdateSongPlaying = UpdateSongPlaying()
        initMedia()
        initRemoteViews()
        initNotification()
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

    private fun setNoti() {
        mRemoteViews?.setTextViewText(R.id.tvNotificationNameSong, mSongCurrent?.title)
        mRemoteViews?.setTextViewText(R.id.tvNotificationNameSinger, mSongCurrent?.artist)
        mNotificationManager?.notify(ID_NOTIFICATION, mNotification)
    }

    private fun callBackService(): PendingIntent {
        val intent = Intent(this, MusicService::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        return PendingIntent.getActivities(this, 0, arrayOf(intent), 0)
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
        setNoti()
        mMediaPlayer?.reset()
        mHandler.removeCallbacks(mUpdateSongPlaying)
        mMediaPlayer = MediaPlayer.create(applicationContext, uri)
        if (mMediaPlayer?.isPlaying == false) {
            mMediaPlayer?.start()
            mHandler.post(mUpdateSongPlaying)
        }
    }

    private fun next() {
        val nextSong: Int = mPositionCurrent + 1
        if (nextSong >= 0 && nextSong < mSongList.songList.size) {
            mSongCurrent = mSongList.songList[nextSong]
            mPositionCurrent++
            playMusic(Uri.parse(mSongCurrent?.data))
        }
    }

    private fun back() {
        val nextSong: Int = mPositionCurrent - 1
        if (nextSong >= 0 && nextSong < mSongList.songList.size) {
            mSongCurrent = mSongList.songList[nextSong]
            mPositionCurrent--
            playMusic(Uri.parse(mSongCurrent?.data))
        }
    }

    override fun onCompletion(mp: MediaPlayer?) {
        Log.e(TAG, "onCompletiosn")
    }

    private fun pauseMusic() {
        mMediaPlayer!!.pause()
        mHandler.removeCallbacks(mUpdateSongPlaying)
    }

    private fun miliSecondsToString(miliseconds: Int?): String {
        if (miliseconds != null) {
            val minutes = TimeUnit.MILLISECONDS.toMinutes(miliseconds.toLong())
            val seconds = TimeUnit.MILLISECONDS.toSeconds(miliseconds.toLong()) % 60

            return minutes.toString() + ":" + seconds
        }
        return ERROR_NULL
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    internal inner class UpdateSongPlaying : Runnable {
        override fun run() {
            Log.e(TAG, "run: " + miliSecondsToString(mMediaPlayer?.currentPosition))
            upDateRemote()
            mHandler.postDelayed(mUpdateSongPlaying, 1000)
        }

        private fun upDateRemote() {
            mRemoteViews?.setProgressBar(R.id.progressBarNotification
                    , mMediaPlayer!!.duration
                    , mMediaPlayer!!.currentPosition
                    , false)
            mNotificationManager!!.notify(ID_NOTIFICATION, mNotification)
        }
    }
}
