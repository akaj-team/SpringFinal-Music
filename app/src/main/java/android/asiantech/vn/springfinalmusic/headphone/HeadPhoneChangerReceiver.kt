package android.asiantech.vn.springfinalmusic.headphone

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

import java.util.Objects

class HeadPhoneChangerReceiver(private val mListenes: IListenesHPhoneChanger?) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_HEADSET_PLUG) {
            Log.e(TAG, "onReceive: " + intent.action!!)
            val state = intent.getIntExtra(KEY_HEADSET_STATE, ERROR)
            mListenes?.onCommand(state)
        }
    }

    companion object {
        private val TAG = HeadPhoneChangerReceiver::class.java.simpleName
        val PHONE_ISDICONNECTED = 0
        val PHONE_ISCONNECTED = 1
        val ERROR = -1
        val KEY_HEADSET_STATE = "state"
    }
}
