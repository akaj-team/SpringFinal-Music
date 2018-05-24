package android.asiantech.vn.springfinalmusic.library.adapter

import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.model.Song
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_item_song.*

class SongsAdapter(val dataset: List<Song>) : RecyclerView.Adapter<SongsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_item_song, parent, false) as View
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.onBind(mListData.get(position))
    }

    private var mListData: List<Song> = dataset

    override fun getItemCount(): Int {
        return mListData.size
    }


    /*
     *  class viewholder in recycleview
     */

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var mTvNameSong: TextView = view.findViewById(R.id.tvItemNameSong)
        private var mTvNameSinger: TextView = view.findViewById(R.id.tvItemNameSinger)

        fun onBind(song: Song) {
            mTvNameSong.text = song.title
            mTvNameSinger.text = song.artist
        }
    }
}