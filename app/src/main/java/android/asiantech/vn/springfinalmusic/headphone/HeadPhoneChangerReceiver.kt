package android.asiantech.vn.springfinalmusic.headphone

import android.asiantech.vn.springfinalmusic.model.Constant
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class HeadPhoneChangerReceiver(listener: IListenerHPhoneChanger?) : BroadcastReceiver() {
    private var mListener = listener
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_HEADSET_PLUG) {
            val state = intent.getIntExtra(Constant.KEY_HEADSET_STATE, -1)
            mListener?.onCommand(state)
        }
    }

    //state=0 phone isDisconnected,state=1 phone isConnected,state =-1 ERROR
    interface IListenerHPhoneChanger {
        fun onCommand(state: Int)
    }
}
