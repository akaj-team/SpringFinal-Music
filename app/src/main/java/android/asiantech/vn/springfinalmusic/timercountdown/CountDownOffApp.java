package android.asiantech.vn.springfinalmusic.timercountdown;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;

public class CountDownOffApp extends CountDownTimer {
    private static final String TAG = CountDownOffApp.class.getSimpleName();
    private Context mContext;

    public CountDownOffApp(long millisInFuture, long countDownInterval, Context context) {
        super(millisInFuture, countDownInterval);
        this.mContext = context;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        Log.e(TAG, "onTick: "+millisUntilFinished );
        Log.e(TAG, "onTick: "+mContext.getPackageName() );
    }

    @Override
    public void onFinish() {
        System.exit(0);
    }
}
