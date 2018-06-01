package android.asiantech.vn.springfinalmusic.library.adapter

import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.model.Constant
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

class CurrentMusicAdapter(dataset: List<Song>,context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mListData: List<Song> = dataset
    private var mContext:Context=context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_item_song, parent, false) as View
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mListData.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ViewHolder
        holder.onBind(mListData[position])
    }

    /*
     *  class viewholder in recycleview
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var mTvSong: TextView = view.findViewById(R.id.tvItemNameSong)
        private var mTvArtist: TextView = view.findViewById(R.id.tvItemNameSinger)

        init {
            view.setOnClickListener {
                mContext.startActivity(Intent(mContext, PlayMusicActivity::class.java)
                        .setAction(Constant.ACTION_START_SERVICE)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra(Constant.KEY_POSITION_SONG, adapterPosition)
                        .putParcelableArrayListExtra(Constant.KEY_LIST_SONG, mListData as ArrayList<out Parcelable>))
            }
        }

        fun onBind(song: Song) {
            mTvSong.text = song.title
            mTvArtist.text = song.artist
        }
    }
}
