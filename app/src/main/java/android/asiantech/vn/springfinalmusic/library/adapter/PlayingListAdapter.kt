package android.asiantech.vn.springfinalmusic.library.adapter

import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.model.Constant
import android.asiantech.vn.springfinalmusic.model.Song
import android.asiantech.vn.springfinalmusic.service.MusicService
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class PlayingListAdapter(listSong: List<Song>, context: Context, listener: OnAdapterListenes) : RecyclerView.Adapter<PlayingListAdapter.SongPlaying>() {
    companion object {
        const val KEY_POSITION_SELECTED = "positon_song_select"
    }

    private var mListSong: List<Song> = listSong
    private var mContext: Context = context
    private var mListenes: OnAdapterListenes = listener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongPlaying {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_item_song, parent, false) as View
        return SongPlaying(view)
    }

    override fun getItemCount(): Int {
        return mListSong.size
    }

    override fun onBindViewHolder(holder: SongPlaying, position: Int) {
        holder.onBindData(mListSong[position])
    }

    inner class SongPlaying(view: View) : RecyclerView.ViewHolder(view) {
        private var mTvNameSong: TextView = view.findViewById(R.id.tvItemNameSong)
        private var mTvNameSinger: TextView = view.findViewById(R.id.tvItemNameSinger)

        init {
            view.setOnClickListener {
                mContext.startService(Intent(mContext, MusicService::class.java)
                        .setAction(Constant.NEXT_SONG_INDEX)
                        .putExtra(KEY_POSITION_SELECTED, adapterPosition))
                mListenes.onItemSelected()
            }
        }

        fun onBindData(song: Song) {
            mTvNameSong.text = song.title
            mTvNameSinger.text = song.artist
        }
    }

    interface OnAdapterListenes {
        fun onItemSelected()
    }
}