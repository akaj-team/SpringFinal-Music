package android.asiantech.vn.springfinalmusic.library.adapter

import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.model.Constant
import android.asiantech.vn.springfinalmusic.model.Song
import android.asiantech.vn.springfinalmusic.service.MusicService
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.util.ArrayList

class SongsAdapter(dataset: MutableList<Song>, context: Context?) : RecyclerView.Adapter<SongsAdapter.ViewHolder>() {
    private var mListData: MutableList<Song>
    private var mContext: Context? = context

    init {
        mListData = dataset
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

    fun setListSong(data: MutableList<Song>) {
        mListData = data
        notifyDataSetChanged()
    }

    /*
     *  class viewholder in recycleview
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var mTvNameSong: TextView = view.findViewById(R.id.tvItemNameSong)
        private var mTvNameSinger: TextView = view.findViewById(R.id.tvItemNameSinger)

        init {
            view.setOnClickListener {
                mContext?.startService(Intent(mContext, MusicService::class.java)
                        .setAction(Constant.ACTION_PLAY_MUSIC)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra(Constant.KEY_POSITION_SONG, adapterPosition)
                        .putParcelableArrayListExtra(Constant.KEY_LIST_SONG, mListData as ArrayList<out Parcelable>))
            }
        }

        fun onBind(song: Song) {
            mTvNameSong.text = song.title
            mTvNameSinger.text = song.artist
        }
    }
}
