package android.asiantech.vn.springfinalmusic.library

import android.asiantech.vn.springfinalmusic.model.Constant
import android.asiantech.vn.springfinalmusic.model.Song
import android.asiantech.vn.springfinalmusic.playmusic.PlayMusicActivity
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import java.util.ArrayList

class SendRequestPlayMusic {

    companion object {
        private val instances = SendRequestPlayMusic()
        fun getInstances(): SendRequestPlayMusic {
            return instances
        }
    }

    fun stratAcivityPlayByAdapter(context: Context, list: List<Song>, adapterPosition: Int) {
        context.startActivity(Intent(context, PlayMusicActivity::class.java)
                .setAction(Constant.ACTION_START_SERVICE)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra(Constant.KEY_POSITION_SONG, adapterPosition)
                .putParcelableArrayListExtra(Constant.KEY_LIST_SONG, list as ArrayList<out Parcelable>))
    }
}
