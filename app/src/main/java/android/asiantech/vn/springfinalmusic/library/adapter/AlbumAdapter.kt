package android.asiantech.vn.springfinalmusic.library.adapter

import android.app.RemoteInput
import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.library.CurrentMusicPlay
import android.asiantech.vn.springfinalmusic.manager.ResourcesManager
import android.asiantech.vn.springfinalmusic.model.Album
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.util.ArrayList

class AlbumAdapter(dataset: List<Album>, context: Context?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mListData: List<Album> = dataset
    private var mContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_item_singer, parent, false) as View
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
        private var mTvNameAlbum: TextView = view.findViewById(R.id.tvItemSingerName)
        private var mClLayout: ConstraintLayout = view.findViewById(R.id.clAlbumItem)

        init {

        }

        fun onBind(album: Album) {
            mTvNameAlbum.text = album.name
            initListener(album)
        }

        fun initListener(album: Album) {
            mClLayout.setOnClickListener {
                val intent: Intent = Intent(mContext, CurrentMusicPlay::class.java)
                intent.action = CurrentMusicPlay.FILTER_ALBUM
                intent.putParcelableArrayListExtra(CurrentMusicPlay.FILTER_ALBUM,
                        ResourcesManager.getInstance().getAlbum(album.name).listMusic as ArrayList<out Parcelable>)
                mContext?.startActivity(intent)
            }
        }
    }
}
