package android.asiantech.vn.springfinalmusic.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.loading.LoadingActivity
import android.asiantech.vn.springfinalmusic.model.Constant
import android.asiantech.vn.springfinalmusic.model.Song
import android.asiantech.vn.springfinalmusic.playmusic.PlayMusicActivity
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.*
import android.widget.RemoteViews
import java.util.ArrayList

import java.util.*

class MusicService : Service(), MediaPlayer.OnCompletionListener {
    companion object {
        const val ID_NOTIFICATION = 1010
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
    private var mModePlay: Int = Constant.MODE_NORM

    override fun onCreate() {
        super.onCreate()
        init()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val action = intent.action
        if (action != null) {
            when (action) {
                Constant.ACTION_PLAY_MUSIC -> {
                    if (intent.extras != null) {
                        mSongList = intent.extras.getParcelableArrayList(Constant.KEY_LIST_SONG)
                        mPositionSong = intent.extras.getInt(Constant.KEY_POSITION_SONG)
                        mSongCurrent = mSongList[mPositionSong]
                    }
                    playMusic(Uri.parse(mSongCurrent?.data))
                }
                Constant.ACTION_RESUME_MUSIC -> {
                    resumeMusic()
                }
                Constant.ACTION_PAUSE_MUSIC -> {
                    pauseMusic()
                }
                Constant.ACTION_TIMER -> {
                    val minutes = intent.getIntExtra(Constant.KEY_TIME, -1)
                    autoOffAppByTimer(minutes.toLong())
                }
                Constant.ACTION_NEXT_MUSIC -> {
                    next()
                }
                Constant.ACTION_BACK_MUSIC -> {
                    back()
                }
                Constant.ACTION_SEEKBAR_CHANGED -> {
                    if (intent.extras != null) {
                        val progress: Int = intent.extras.getInt(Constant.KEY_PROGRESS)
                        mMediaPlayer?.seekTo(progress)
                    }
                }
                Constant.KEY_SONG_INDEX -> {
                    val positionSelect: Int = intent.extras.getInt(Constant.KEY_POSITION_SELECTED)
                    next(positionSelect)
                }
                Constant.ACTION_MODE_CHANGE -> {
                    if (intent.extras != null) {
                        mModePlay = intent.extras.getInt(Constant.KEY_MODE)
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
                    , setActionEventClick(Constant.ACTION_PAUSE_MUSIC))
            sendBroadcast(Intent()
                    .setAction(Constant.ACTION_PAUSE_MUSIC))
        } else {
            mRemoteViews?.setImageViewResource(R.id.imgNotificationButtonPlay
                    , R.drawable.btn_notificationbar_play)
            mRemoteViews?.setOnClickPendingIntent(R.id.imgNotificationButtonPlay
                    , setActionEventClick(Constant.ACTION_RESUME_MUSIC))
            sendBroadcast(Intent()
                    .setAction(Constant.ACTION_RESUME_MUSIC))
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
        initRemoteViews()
    }

    private fun initRemoteViews() {
        mRemoteViews = RemoteViews(this.packageName, R.layout.notification)
        mRemoteViews?.setOnClickPendingIntent(R.id.imgNotificationButtonPlay
                , setActionEventClick(Constant.ACTION_PAUSE_MUSIC))
        mRemoteViews?.setOnClickPendingIntent(R.id.imgNotificationButtonNext
                , setActionEventClick(Constant.ACTION_NEXT_MUSIC))
        mRemoteViews?.setOnClickPendingIntent(R.id.imgNotificationButtonBack
                , setActionEventClick(Constant.ACTION_BACK_MUSIC))
    }

    private fun setNotification() {
        mRemoteViews?.setTextViewText(R.id.tvNotificationNameSong, mSongCurrent?.title)
        mRemoteViews?.setTextViewText(R.id.tvNotificationNameSinger, mSongCurrent?.artist)
        mNotificationManager?.notify(ID_NOTIFICATION, mNotification)
    }

    private fun callBackActivity(): PendingIntent {
        if (mPositionSong != -1) {
            val intent = Intent(this, PlayMusicActivity::class.java)
                    .putExtra(Constant.KEY_POSITION_SONG, mPositionSong)
                    .putParcelableArrayListExtra(Constant.KEY_LIST_SONG, mSongList as ArrayList<out Parcelable>)
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
                    .setContentIntent(callBackActivity())
                    .setCustomBigContentView(this.mRemoteViews)
                    .build()
        } else {
            mNotification = Notification.Builder(this)
                    .setSmallIcon(R.drawable.ic_music_note_black_24dp)
                    .setContentIntent(callBackActivity())
                    .build()
            this.mNotification?.bigContentView = mRemoteViews
        }
        startForeground(ID_NOTIFICATION, mNotification)
        mNotificationManager!!.notify(ID_NOTIFICATION, mNotification)
    }

    private fun playMusic(uri: Uri) {
        initNotification()
        setNotification()
        mMediaPlayer?.reset()
        mHandler.removeCallbacks(mUpdateSongPlaying)
        mMediaPlayer = MediaPlayer.create(applicationContext, uri)
        mMediaPlayer?.setOnCompletionListener(this)
        if (mMediaPlayer?.isPlaying == false) {
            mMediaPlayer?.start()
            mHandler.post(mUpdateSongPlaying)
        }
        changedImageBtnPlay()
        sendChangerSong()
    }

    private fun sendChangerSong() {
        sendBroadcast(Intent()
                .setAction(Constant.ACTION_SONG_IS_CHANGED)
                .putExtra(Constant.KEY_POSITION_SONG, mPositionSong))
    }

    private fun isLastSong(positionCurrent: Int): Boolean {
        if (positionCurrent >= mSongList.size - 1) {
            return true
        }
        return false
    }

    private fun next() {
        if (isLastSong(mPositionSong)) {
            next(0)
            return
        }
        val nextSong: Int = if (mModePlay == Constant.MODE_RANDOM_ALBUM) {
            Random().nextInt((mSongList.size - 1) - 0) + 0
        } else {
            mPositionSong + 1
        }

        if (nextSong >= 0 && nextSong < mSongList.size) {
            mSongCurrent = mSongList[nextSong]
            mPositionSong = nextSong
            playMusic(Uri.parse(mSongCurrent?.data))
        }
    }

    private fun next(nextSong: Int) {
        if (nextSong >= 0 && nextSong < mSongList.size) {
            mSongCurrent = mSongList[nextSong]
            mPositionSong = nextSong
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
        if (mModePlay == Constant.MODE_REPEAT_SONG) {
            next(mPositionSong)
            return
        }
        if (isLastSong(mPositionSong) && mModePlay == Constant.MODE_NORM) {
            stopMusic()
            return
        }
        if (isLastSong(mPositionSong) && mModePlay == Constant.MODE_REPEAT_ALBUM) {
            next(0)
            return
        }
        next()
    }

    private fun stopMusic() {
        mHandler.removeCallbacks(mUpdateSongPlaying)
        mMediaPlayer?.pause()
        this.stopSelf()
        this.onDestroy()
    }

    private fun pauseMusic() {
        mMediaPlayer?.pause()
        mHandler.removeCallbacks(mUpdateSongPlaying)
        stopForeground(false)
        changedImageBtnPlay()
    }

    private fun resumeMusic() {
        mMediaPlayer?.start()
        startForeground(ID_NOTIFICATION, mNotification)
        mHandler.post(mUpdateSongPlaying)
        changedImageBtnPlay()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        mHandler.removeCallbacks(mUpdateSongPlaying)
        stopForeground(false)
        this.stopSelf()
        mNotificationManager?.cancelAll()
        super.onDestroy()
    }

    internal inner class UpdateSongPlaying : Runnable {
        override fun run() {
            val intCurrPosition: Int? = mMediaPlayer?.currentPosition
            upDateRemote()
            changedImageBtnPlay()
            sendBroadcast(Intent()
                    .setAction(Constant.ACTION_DISPLAY_MUSIC)
                    .putExtra(Constant.KEY_SONG, mSongCurrent)
                    .putExtra(Constant.KEY_MODE, mModePlay)
                    .putExtra(Constant.KEY_POSITION_MEDIA, intCurrPosition))
            mHandler.postDelayed(this, 1000)
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
