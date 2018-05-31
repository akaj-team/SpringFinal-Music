package android.asiantech.vn.springfinalmusic.library.adapter

import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.model.Song
import android.asiantech.vn.springfinalmusic.playmusic.PlayMusicActivity
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.util.ArrayList

class SongsAdapter(dataset: List<Song>, context: Context?) : RecyclerView.Adapter<SongsAdapter.ViewHolder>() {
    private var mListData: List<Song> = dataset
    private var mContext: Context? = context

    companion object {
        const val KEY_LIST_SONG = "list_of_song"
        const val KEY_POSITION_SONG = "position_song"
        const val ACTION_START_SERVICE = "run_service"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_item_song, parent, false) as View
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.onBind(mListData[position])
    }

    override fun getItemCount(): Int {
        return mListData.size
    }

    /*
     *  class viewholder in recycleview
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var mTvNameSong: TextView = view.findViewById(R.id.tvItemNameSong)
        private var mTvNameSinger: TextView = view.findViewById(R.id.tvItemNameSinger)

        init {
            view.setOnClickListener {
                mContext?.startActivity(Intent(mContext, PlayMusicActivity::class.java)
                        .setAction(ACTION_START_SERVICE)
                        .putExtra(KEY_POSITION_SONG, adapterPosition)
                        .putParcelableArrayListExtra(KEY_LIST_SONG, mListData as ArrayList<out Parcelable>))
            }
        }

        fun onBind(song: Song) {
            mTvNameSong.text = song.title
            mTvNameSinger.text = song.artist
        }
    }
}
