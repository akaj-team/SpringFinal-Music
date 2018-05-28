package android.asiantech.vn.springfinalmusic.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.timercountdown.FragmentTimerOffApp
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.CountDownTimer
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews

import java.util.concurrent.TimeUnit

class MusicService : Service() {
    companion object {
        private val TAG = MusicService::class.java.simpleName
        const val ID_NOTIFICATION = 1010
        const val NOTI_BTN_PLAY_CLICK = "play_on_click"
        const val NOTI_BTN_PAUSE_CLICK = "pause_on_click"
        const val ACTION_TIMER = "timer_auto_off_app"
        const val ERROR_NULL = "null"
    }

    private var mMediaPlayer: MediaPlayer? = null
    private val mHandler = Handler()
    private var mUpdateSongPlaying: UpdateSongPlaying? = null
    private var mRemoteViews: RemoteViews? = null
    private var mNotificationManager: NotificationManager? = null
    private var mNotification: Notification? = null
    private var mCountDownTimer: CountDownTimer? = null

    override fun onCreate() {
        super.onCreate()
        init()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val action = intent.action
        if (action != null) {
            when (action) {
                NOTI_BTN_PLAY_CLICK -> {
                    playMusic()
                    startNotification()
                }
                NOTI_BTN_PAUSE_CLICK -> {
                    pauseMusic()
                }
                ACTION_TIMER -> {
                    val minutes = intent.getIntExtra(FragmentTimerOffApp.KEY_TIME, -1)
                    autoOffAppByTimer(minutes.toLong())
                }
            }
        }
        return Service.START_STICKY
    }

    private fun autoOffAppByTimer(minutes: Long) {
        mCountDownTimer?.cancel()
        mCountDownTimer = object : CountDownTimer(minutes * 1000 * 60, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                Log.e(TAG, "onTick" + miliSecondsToString(millisUntilFinished))
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
        iniitRemoteViews()
        initNotification()
    }

    private fun iniitRemoteViews() {
        mRemoteViews = RemoteViews(this.packageName, R.layout.notification)
        mRemoteViews!!.setOnClickPendingIntent(R.id.imgNotificationButtonPlay, setActionEventClick(NOTI_BTN_PAUSE_CLICK))
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
    }

    private fun startNotification() {
        startForeground(ID_NOTIFICATION, mNotification)
        mNotificationManager!!.notify(ID_NOTIFICATION, mNotification)
    }

    private fun initMedia() {
        mMediaPlayer = MediaPlayer.create(applicationContext, R.raw.cogaim52)
    }

    private fun playMusic() {
        if (!mMediaPlayer!!.isPlaying) {
            mMediaPlayer!!.start()
            mHandler.post(mUpdateSongPlaying)
        }
    }

    private fun pauseMusic() {
        mMediaPlayer!!.pause()
        mHandler.removeCallbacks(mUpdateSongPlaying)
    }

    private fun miliSecondsToString(miliseconds: Long?): String {
        if (miliseconds != null) {
            val minutes = TimeUnit.MILLISECONDS.toMinutes(miliseconds)
            val seconds = TimeUnit.MILLISECONDS.toSeconds(miliseconds) % 60

            return minutes.toString() + ":" + seconds
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
            Log.e(TAG, "run: " + miliSecondsToString((mMediaPlayer?.currentPosition)?.toLong()))
            upDateRemote()
            mHandler.postDelayed(mUpdateSongPlaying, 1000)
        }

        private fun upDateRemote() {
            mRemoteViews?.setProgressBar(R.id.progressBarNotification, mMediaPlayer!!.duration, mMediaPlayer!!.currentPosition, false)
            //mRemoteViews?.setTextViewText(R.id.tvNotificationNameSong, )
            //mRemoteViews?.setTextViewText(R.id.tvNotificationNameSinger, )
            mNotificationManager!!.notify(ID_NOTIFICATION, mNotification)
        }
    }
}
