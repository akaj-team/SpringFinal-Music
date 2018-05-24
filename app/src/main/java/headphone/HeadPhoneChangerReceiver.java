package headphone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Objects;

public class HeadPhoneChangerReceiver extends BroadcastReceiver {
    private static final String TAG = HeadPhoneChangerReceiver.class.getSimpleName();
    private final IListenesHPhoneChanger mListenes;
    public static final int PHONE_ISDICONNECTED = 0;
    public static final int PHONE_ISCONNECTED = 1;
    public static final int ERROR = -1;
    public static final String KEY_HEADSET_STATE = "state";

    public HeadPhoneChangerReceiver(IListenesHPhoneChanger mListenes) {
        this.mListenes = mListenes;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), Intent.ACTION_HEADSET_PLUG)) {
            Log.e(TAG, "onReceive: " + intent.getAction());
            int state = intent.getIntExtra(KEY_HEADSET_STATE, ERROR);
            if (mListenes != null) {
                mListenes.onCommand(state);//Send command some things
            }
        }
    }
}
