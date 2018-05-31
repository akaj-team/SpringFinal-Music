package android.asiantech.vn.springfinalmusic.library.adapter

import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.library.CurrentMusicPlay
import android.asiantech.vn.springfinalmusic.manager.ResourcesManager
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.util.ArrayList


class ArtistAdapter(dataset: List<String>, context: Context?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mListData: List<String> = dataset
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
        holder.onBind(mListData.get(position))
    }

    fun setListArtist(listArtist: List<String>) {
        mListData = listArtist
        notifyDataSetChanged()
    }

    fun reset() {
        mListData = ResourcesManager.getInstance().getListArtist()
        notifyDataSetChanged()
    }

    /*
     *  class viewholder in recycleview
     */

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var mTvNameSinger: TextView = view.findViewById(R.id.tvItemSingerName)
        private var mClArtist: ConstraintLayout = view.findViewById(R.id.clAlbumItem)

        fun onBind(name: String) {
            mTvNameSinger.text = name
            initListener(name)
        }

        fun initListener(name: String) {
            mClArtist.setOnClickListener {
                val intent = Intent(mContext, CurrentMusicPlay::class.java)
                intent.action = CurrentMusicPlay.FILTER_ARTIST
                intent.putParcelableArrayListExtra(CurrentMusicPlay.FILTER_ARTIST,
                        ResourcesManager.getInstance().getMusicOfArtist(name) as ArrayList<out Parcelable>)
                mContext?.startActivity(intent)
            }
        }
    }
}
