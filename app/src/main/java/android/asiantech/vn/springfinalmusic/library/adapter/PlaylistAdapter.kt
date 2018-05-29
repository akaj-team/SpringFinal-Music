package android.asiantech.vn.springfinalmusic.library.adapter

import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.manager.ResourcesManager
import android.asiantech.vn.springfinalmusic.model.Playlist
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class PlaylistAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mListData: MutableList<Playlist> = mutableListOf()

    init {
        ResourcesManager.getInstance().setPlaylist(mListData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_item_playlist, parent, false) as View
        return ViewHolder(view, parent.context)
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

    class ViewHolder(view: View, context: Context) : RecyclerView.ViewHolder(view) {
        private var mTvNamePlaylist: TextView = view.findViewById(R.id.tvPlaylistItemName)
        private var mTvNumOfSong: TextView = view.findViewById(R.id.tvPlaylistItemNumOfSong)
        private var mContext = context
        fun onBind(playlist: Playlist) {
            mTvNamePlaylist.text = playlist.name
            mTvNumOfSong.text = String.format("%s %s", playlist.listSong?.size.toString(), mContext.resources.getString(R.string.library_text_songs))
        }
    }
}
