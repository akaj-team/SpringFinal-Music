package android.asiantech.vn.springfinalmusic.library.adapter

import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.model.ListSong
import android.asiantech.vn.springfinalmusic.model.Song
import android.asiantech.vn.springfinalmusic.service.MusicService
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class SongsAdapter(dataset: List<Song>, context: Context?) : RecyclerView.Adapter<SongsAdapter.ViewHolder>() {
    private var mListData: List<Song> = dataset
    private var mContext: Context? = context

    companion object {
        const val KEY_LIST_SONG = "list_of_song"
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
                val intent: Intent = Intent(mContext, MusicService::class.java)
                        .setAction(MusicService.PLAY_MUSIC)
                        .putExtra(KEY_LIST_SONG, ListSong(mListData, adapterPosition))
                mContext?.startService(intent)
            }
        }

        fun onBind(song: Song) {
            mTvNameSong.text = song.title
            mTvNameSinger.text = song.artist
        }
    }
}
