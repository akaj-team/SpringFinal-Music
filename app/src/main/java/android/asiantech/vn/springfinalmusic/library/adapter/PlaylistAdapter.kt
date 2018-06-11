package android.asiantech.vn.springfinalmusic.library.adapter

import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.library.CurrentMusicPlay
import android.asiantech.vn.springfinalmusic.library.IEventDeletePlayList
import android.asiantech.vn.springfinalmusic.manager.ResourcesManager
import android.asiantech.vn.springfinalmusic.model.Playlist
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import java.util.ArrayList

class PlaylistAdapter(listdata: MutableList<Playlist>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), IEventDeletePlayList {
    private var mListData: MutableList<Playlist> = listdata
    private var mIsShowButtonClose = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_item_playlist, parent, false) as View
        return ViewHolder(view, parent.context)
    }

    override fun getItemCount(): Int {
        return mListData.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ViewHolder
        holder.onBind(mListData[position], this)
    }

    fun setIsShowButtonClose(close: Boolean) {
        mIsShowButtonClose = close
        notifyDataSetChanged()
    }

    override fun onDeleteComplete() {
        notifyDataSetChanged()
    }

    fun setListPlaylist(listPlaylist: MutableList<Playlist>) {
        mListData = listPlaylist
        notifyDataSetChanged()
    }

    fun reset() {
        mListData = ResourcesManager.getInstance().getListPlaylist()
        notifyDataSetChanged()
    }

    /*
     *  class viewholder in recycleview
     */

    inner class ViewHolder(view: View, context: Context) : RecyclerView.ViewHolder(view) {
        private var mTvNamePlaylist: TextView = view.findViewById(R.id.tvPlaylistItemName)
        private var mTvNumOfSong: TextView = view.findViewById(R.id.tvPlaylistItemNumOfSong)
        private var mContext = context
        private var mClLayout: ConstraintLayout = view.findViewById(R.id.clPlaylistItem)
        private var mBtnClose: ImageButton = view.findViewById(R.id.btnMiniBarButtonClose)
        private lateinit var mIEventClose: IEventDeletePlayList
        fun onBind(playlist: Playlist, eventDelete: IEventDeletePlayList) {
            mTvNamePlaylist.text = playlist.name
            mTvNumOfSong.text = String.format("%s %s", playlist.listSong?.size.toString(), mContext.resources.getString(R.string.library_text_songs))
            if (mIsShowButtonClose) {
                mBtnClose.visibility = View.VISIBLE
            } else {
                mBtnClose.visibility = View.GONE
            }
            mIEventClose = eventDelete
            initListener(playlist)
        }

        private fun initListener(playlist: Playlist) {
            mClLayout.setOnClickListener {
                val intent = Intent(mContext, CurrentMusicPlay::class.java)
                intent.action = CurrentMusicPlay.FILTER_PLAYLIST
                intent.putParcelableArrayListExtra(CurrentMusicPlay.FILTER_PLAYLIST,
                        ResourcesManager.getInstance().getPlaylist(playlist.name).listSong as ArrayList<out Parcelable>)
                mContext.startActivity(intent)
            }
            mBtnClose.setOnClickListener {
                ResourcesManager.getInstance().deletePlaylist(playlist, mContext)
                mIEventClose.onDeleteComplete()
            }
        }
    }
}
