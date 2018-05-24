package android.asiantech.vn.springfinalmusic.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.asiantech.vn.springfinalmusic.R;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.concurrent.TimeUnit;

public class MusicService extends Service {
    private static final String TAG = MusicService.class.getSimpleName();
    public static final int ID_NOTIFICATION = 1010;
    private MediaPlayer mMediaPlayer;
    private Handler mHandler = new Handler();
    private UpdateSongPlaying mUpdateSongPlaying;
    private RemoteViews mRemoteViews;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        playMusic();
        return super.onStartCommand(intent, flags, startId);
    }

    private void init() {
        mUpdateSongPlaying = new UpdateSongPlaying();
        mRemoteViews = new RemoteViews(this.getPackageName(), R.layout.notification);
        initMedia();
        initNotification();
    }

    private PendingIntent callBackService() {
        Intent intent = new Intent(this, MusicService.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivities(this, 0, new Intent[]{intent}, 0);
    }

    private void initNotification() {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification mNotification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.img_logo)
                .setContent(mRemoteViews)
                .setContentIntent(callBackService())
                .build();
        mNotification.bigContentView=mRemoteViews;
        startForeground(ID_NOTIFICATION, mNotification);
        mNotificationManager.notify(ID_NOTIFICATION, mNotification);
    }

    private void initMedia() {
        mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.cogaim52);
    }

    private void playMusic() {
        mMediaPlayer.start();
        mHandler.post(mUpdateSongPlaying);
    }

    private String miliSecondsToString(int miliseconds) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes((long) miliseconds);
        long seconds = (TimeUnit.MILLISECONDS.toSeconds((long) miliseconds)) % 60;
        return minutes + ":" + seconds;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class UpdateSongPlaying implements Runnable {
        @Override
        public void run() {
            Log.e(TAG, "run: " + miliSecondsToString(mMediaPlayer.getCurrentPosition()));
            mHandler.postDelayed(mUpdateSongPlaying, 1000);
        }
    }
}
