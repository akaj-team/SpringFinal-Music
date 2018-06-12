package android.asiantech.vn.springfinalmusic.service

import android.app.*
import android.asiantech.vn.springfinalmusic.R
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
import android.asiantech.vn.springfinalmusic.headphone.HeadPhoneChangerReceiver
import android.asiantech.vn.springfinalmusic.home.HomeActivity
import android.asiantech.vn.springfinalmusic.library.LibraryActivity
import android.content.IntentFilter
import android.support.v4.app.NotificationCompat


@Suppress("DEPRECATION")
class MusicService : Service(), MediaPlayer.OnCompletionListener
        , HeadPhoneChangerReceiver.IListenerHPhoneChanger {
    companion object {
        const val ID_NOTIFICATION = 1010
    }

    private var mTaskStackBuilder: TaskStackBuilder? = null
    private var mMediaPlayer: MediaPlayer? = null
    private val mHandler = Handler()
    private var mUpdateSongPlaying: UpdateSongPlaying? = null
    private var mRemoteViewsExtends: RemoteViews? = null
    private var mRemoteViews: RemoteViews? = null
    private var mNotificationManager: NotificationManager? = null
    private var mNotification: Notification? = null
    private var mCountDownTimer: CountDownTimer? = null
    private lateinit var mSongList: List<Song>
    private var mPositionSong: Int = -1
    private var mSongCurrent: Song? = null
    private var mModePlay: Int = Constant.MODE_NORM
    private var mHeadPhoneListener: HeadPhoneChangerReceiver? = null
    private var mCountHPChanger = 0

    override fun onCreate() {
        super.onCreate()
        init()
        initBackStack()
    }

    private fun initBackStack() {
        mTaskStackBuilder?.addNextIntentWithParentStack(Intent(this, HomeActivity::class.java))
        mTaskStackBuilder?.addNextIntentWithParentStack(Intent(this, LibraryActivity::class.java))
    }

    private fun isSongCurrent(song: Song): Boolean {
        return song.data == mSongCurrent?.data
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val action = intent.action
        if (action != null) {
            when (action) {
                Constant.ACTION_PLAY_MUSIC -> {
                    mSongList = intent.extras.getParcelableArrayList(Constant.KEY_LIST_SONG)
                    mPositionSong = intent.extras.getInt(Constant.KEY_POSITION_SONG)
                    if (mPositionSong >= mSongList.size) {
                        mSongCurrent = null
                    }
                    if (!isSongCurrent(mSongList[mPositionSong])) {
                        mSongCurrent = mSongList[mPositionSong]
                        playMusic(Uri.parse(mSongCurrent?.data))
                    } else {
                        if (mMediaPlayer?.isPlaying == false) {
                            resumeMusic()
                        }
                    }
                }
                Constant.ACTION_RESUME_MUSIC -> {
                    resumeMusic()
                }
                Constant.ACTION_PAUSE_MUSIC -> {
                    pauseMusic()
                }
                Constant.ACTION_TIMER_START -> {
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
                    if (positionSelect != mPositionSong) {
                        next(positionSelect)
                    } else {
                        if (mMediaPlayer?.isPlaying == false) {
                            resumeMusic()
                        }
                    }
                }
                Constant.ACTION_MODE_CHANGE -> {
                    if (intent.extras != null) {
                        mModePlay = intent.extras.getInt(Constant.KEY_MODE)
                    }
                }
                Constant.ACTION_CLOSE_MUSIC -> {
                    if (mMediaPlayer?.isPlaying == false) {
                        stopForeground(true)
                    }
                }
                Constant.ACTION_TIMER_STOP -> {
                    mCountDownTimer?.cancel()
                    sendTimerOnActivity(0)
                }
            }
        }
        return Service.START_NOT_STICKY
    }

    private fun changedImageBtnPlayNotification() {
        if (mMediaPlayer?.isPlaying == true) {
            mRemoteViewsExtends?.setImageViewResource(R.id.imgNotificationExtendsButtonPlay
                    , R.drawable.btn_notificationbar_pause)
            mRemoteViewsExtends?.setOnClickPendingIntent(R.id.imgNotificationExtendsButtonPlay
                    , setActionEventClick(Constant.ACTION_PAUSE_MUSIC))
            sendBroadcast(Intent()
                    .setAction(Constant.ACTION_PAUSE_MUSIC))
        } else {
            mRemoteViewsExtends?.setImageViewResource(R.id.imgNotificationExtendsButtonPlay
                    , R.drawable.btn_notificationbar_play)
            mRemoteViewsExtends?.setOnClickPendingIntent(R.id.imgNotificationExtendsButtonPlay
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
                sendBroadcast(Intent().setAction(Constant.ACTION_TIMER_TICK)
                        .putExtra(Constant.KEY_TIME, millisUntilFinished))
            }

            override fun onFinish() {
                sendTimerOnActivity(0)
                if (mMediaPlayer?.isPlaying == true) {
                    pauseMusic()
                }
            }
        }
        mCountDownTimer?.start()
    }

    private fun sendTimerOnActivity(milisUntilFinished: Int?) {
        sendBroadcast(Intent().setAction(Constant.ACTION_TIMER_FINISHED)
                .putExtra(Constant.KEY_TIME, milisUntilFinished))
    }

    private fun init() {
        mUpdateSongPlaying = UpdateSongPlaying()
        mHeadPhoneListener = HeadPhoneChangerReceiver(this)
        mTaskStackBuilder = TaskStackBuilder.create(this)
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_HEADSET_PLUG)
        registerReceiver(mHeadPhoneListener, intentFilter)
    }

    private fun perparMusicNotification(remoteViews: RemoteViews?) {
        remoteViews?.setTextViewText(R.id.tvNotificationNameSong, mSongCurrent?.title)
        remoteViews?.setTextViewText(R.id.tvNotificationNameSinger, mSongCurrent?.artist)
    }

    private fun perparExtendMusicNotification(remoteViews: RemoteViews?) {
        remoteViews?.setOnClickPendingIntent(R.id.imgNotificationExtendsButtonPlay
                , setActionEventClick(Constant.ACTION_PAUSE_MUSIC))
        remoteViews?.setOnClickPendingIntent(R.id.imgNotificationExtendsButtonNext
                , setActionEventClick(Constant.ACTION_NEXT_MUSIC))
        remoteViews?.setOnClickPendingIntent(R.id.imgNotificationExtendsButtonBack
                , setActionEventClick(Constant.ACTION_BACK_MUSIC))
        remoteViews?.setOnClickPendingIntent(R.id.imgNotificationExtendsButtonClose
                , setActionEventClick(Constant.ACTION_CLOSE_MUSIC))

        remoteViews?.setTextViewText(R.id.tvNotificationExtendsNameSong, mSongCurrent?.title)
        remoteViews?.setTextViewText(R.id.tvNotificationExtendsNameSinger, mSongCurrent?.artist)
    }

    private fun callBackDataToActivity(): Intent {
        return Intent(this, PlayMusicActivity::class.java)
                .putExtra(Constant.KEY_POSITION_SONG, mPositionSong)
                .putParcelableArrayListExtra(Constant.KEY_LIST_SONG, mSongList as ArrayList<out Parcelable>)
    }

    private fun setActionEventClick(action: String): PendingIntent {
        val intent = Intent(this, MusicService::class.java).setAction(action)
        return PendingIntent.getService(this, 0, intent, 0)
    }

    private fun initNotification() {
        if ((mTaskStackBuilder?.intentCount as Int) < 3) {
            mTaskStackBuilder?.addNextIntentWithParentStack(callBackDataToActivity())
        } else {
            mTaskStackBuilder?.editIntentAt(2)?.replaceExtras(callBackDataToActivity())
        }

        val pendingIntent = mTaskStackBuilder?.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        mRemoteViews = RemoteViews(this.packageName, R.layout.notification)
        mRemoteViewsExtends = RemoteViews(this.packageName, R.layout.notification_extends)
        perparMusicNotification(mRemoteViews)
        perparExtendMusicNotification(mRemoteViewsExtends)

        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotification = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_music_note_black_24dp)
                .setContentIntent(pendingIntent)
                .setCustomContentView(mRemoteViews)
                .setCustomBigContentView(mRemoteViewsExtends)
                .build()
        startForeground(ID_NOTIFICATION, mNotification)
        mNotificationManager?.notify(ID_NOTIFICATION, mNotification)
    }

    private fun playMusic(uri: Uri) {
        initNotification()
        mMediaPlayer?.reset()
        mHandler.removeCallbacks(mUpdateSongPlaying)
        mMediaPlayer = MediaPlayer.create(applicationContext, uri)
        mMediaPlayer?.setOnCompletionListener(this)
        if (mMediaPlayer?.isPlaying == false) {
            mMediaPlayer?.start()
            mHandler.post(mUpdateSongPlaying)
        }
        changedImageBtnPlayNotification()
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
            pauseMusic()
            return
        }
        if (isLastSong(mPositionSong) && mModePlay == Constant.MODE_REPEAT_ALBUM) {
            next(0)
            return
        }
        next()
    }

    private fun pauseMusic() {
        mMediaPlayer?.pause()
        changedImageBtnPlayNotification()
        sendBroadcast(Intent()
                .setAction(Constant.ACTION_PAUSE_MUSIC))
    }

    private fun resumeMusic() {
        mMediaPlayer?.start()
        startForeground(ID_NOTIFICATION, mNotification)
        refeshHandler()
        changedImageBtnPlayNotification()
        sendBroadcast(Intent()
                .setAction(Constant.ACTION_RESUME_MUSIC))
    }

    private fun refeshHandler() {
        mHandler.removeCallbacks(mUpdateSongPlaying)
        mHandler.post(mUpdateSongPlaying)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        mHandler.removeCallbacks(mUpdateSongPlaying)
        if (mHeadPhoneListener?.isOrderedBroadcast == true) {
            unregisterReceiver(mHeadPhoneListener)
        }
        stopForeground(true)
        this.stopSelf()
        mNotificationManager?.cancelAll()
        super.onDestroy()
    }

    private fun isScreenOn(): Boolean {
        val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
        return pm.isScreenOn
    }

    override fun onCommand(state: Int) {
        when (state) {
            Constant.PHONE_ISCONNECTED -> {
                if (mMediaPlayer?.isPlaying == false) {
                    resumeMusic()
                }
            }
            Constant.PHONE_ISDICONNECTED -> {
                if (mMediaPlayer?.isPlaying == true && mCountHPChanger++ > 0) {
                    pauseMusic()
                }
            }
        }
    }

    internal inner class UpdateSongPlaying : Runnable {
        override fun run() {
            val intCurrPosition: Int? = mMediaPlayer?.currentPosition
            if (isScreenOn()) {
                sendBroadcast(Intent()
                        .setAction(Constant.ACTION_DISPLAY_MUSIC)
                        .putExtra(Constant.KEY_SONG, mSongCurrent)
                        .putExtra(Constant.KEY_PLAYING, mMediaPlayer?.isPlaying)
                        .putExtra(Constant.KEY_MODE, mModePlay)
                        .putExtra(Constant.KEY_POSITION_MEDIA, intCurrPosition)
                        .putExtra(Constant.KEY_SONG_INDEX, mPositionSong))
            }
            mHandler.postDelayed(this, 200)
        }
    }
}
