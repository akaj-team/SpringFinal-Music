package android.asiantech.vn.springfinalmusic.home

import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.library.LibraryActivity
import android.content.Context
import android.content.Intent
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class HomeAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            HEADER_OFFLINE, HEADER_ONLINE -> {
                val view = LayoutInflater.from(mContext).inflate(R.layout.fragment_item_header_home, parent, false)
                return ViewholderHeader(view)
            }
            else -> {
                val view = LayoutInflater.from(mContext).inflate(R.layout.fragment_item_home, parent, false)
                return ViewholderItem(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return NUM_OF_ITEM
    }

    override fun getItemViewType(position: Int): Int {
        when (position) {
            0 -> return HEADER_OFFLINE
            1, 2, 3 -> return ITEM_OFFLINE
            4 -> return HEADER_ONLINE
            else -> return ITEM_ONLINE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val type = getItemViewType(position)
        when (type) {
            HEADER_ONLINE, HEADER_OFFLINE -> {
                holder as ViewholderHeader
                holder.onBind(position)
            }
            else -> {
                holder as ViewholderItem
                holder.onBind(position)
            }
        }
    }

    inner class ViewholderItem(view: View) : RecyclerView.ViewHolder(view) {
        private var mImgIconItem = view.findViewById<ImageView>(R.id.imgIconItemHome)
        private var mTvItemHome = view.findViewById<TextView>(R.id.tvItemHome)
        private var mClItemHome = view.findViewById<ConstraintLayout>(R.id.clItemHome)
        fun onBind(position: Int) {
            when (position) {
                ItemHome.LIBRARY.value -> {
                    mImgIconItem.setImageResource(R.drawable.custom_icon_library)
                    mTvItemHome.text = mContext.getString(R.string.home_button_text_library)
                    initListener()
                }
                ItemHome.VIDEO.value -> {
                    mImgIconItem.setImageResource(R.drawable.custom_icon_video)
                    mTvItemHome.text = mContext.getString(R.string.btn_home_text)
                }
                ItemHome.DOWNLOAD.value -> {
                    mImgIconItem.setImageResource(R.drawable.custom_icon_download)
                    mTvItemHome.text = mContext.getString(R.string.btn_home_download_text)
                }
                ItemHome.FIND_SONG.value -> {
                    mImgIconItem.setImageResource(R.drawable.custom_icon_search)
                    mTvItemHome.text = mContext.getString(R.string.btn_home_search_text)
                }
                ItemHome.CHARTS.value -> {
                    mImgIconItem.setImageResource(R.drawable.custom_icon_star)
                    mTvItemHome.text = mContext.getString(R.string.btn_home_ranker_text)
                }
            }
        }

        private fun initListener() {
            mClItemHome.setOnClickListener {
                val intent = Intent(mContext, LibraryActivity::class.java)
                mContext.startActivity(intent)
            }
        }
    }

    inner class ViewholderHeader(view: View) : RecyclerView.ViewHolder(view) {
        private var mTvHeaderText = view.findViewById<TextView>(R.id.tvItemHeaderHome)
        fun onBind(position: Int) {
            if (position == 0) {
                mTvHeaderText.text = mContext.getString(R.string.item_home_offline)
            } else {
                mTvHeaderText.text = mContext.getString(R.string.item_home_online)
            }
        }
    }

    companion object {
        val HEADER_OFFLINE = 0
        val ITEM_OFFLINE = 1
        val HEADER_ONLINE = 2
        val ITEM_ONLINE = 3
        val NUM_OF_ITEM = 7
    }

    enum class ItemHome constructor(val value: Int) {
        LIBRARY(1),
        VIDEO(2),
        DOWNLOAD(3),
        FIND_SONG(5),
        CHARTS(6)
    }

}