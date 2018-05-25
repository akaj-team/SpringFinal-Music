package android.asiantech.vn.springfinalmusic.playlistchoice

import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.model.Song
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class PlaylistChoiceAdapter(dataset: List<Song>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mListData: List<Song> = dataset
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_item_playlist_choice, parent, false) as View
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

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var mTvNameSong: TextView = view.findViewById(R.id.tvItemNameSong)
        private var mTvNameSinger: TextView = view.findViewById(R.id.tvItemNameSinger)

        fun onBind(song: Song) {
            mTvNameSong.text = song.title
            mTvNameSinger.text = song.artist
        }
    }
}
