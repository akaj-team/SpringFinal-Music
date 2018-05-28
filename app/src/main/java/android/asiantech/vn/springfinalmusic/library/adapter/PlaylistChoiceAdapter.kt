package android.asiantech.vn.springfinalmusic.library.adapter

import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.library.playlistchoice.IEventClickPlaylistChoice
import android.asiantech.vn.springfinalmusic.model.Song
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView

class PlaylistChoiceAdapter(dataset: List<Song>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), IEventClickPlaylistChoice {
    private var mListData: List<Song> = dataset
    private var mListBoolChoice = mutableListOf<Boolean>()
    private var mListChoice = mutableListOf<Song>()

    init {
        for (index in mListData.indices) {
            mListBoolChoice.add(false)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_item_playlist_choice, parent, false) as View
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mListData.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ViewHolder
        holder.onBind(mListData[position], mListBoolChoice[position], this)
    }

    override fun onItemPlaylistChoice(position: Int, isChoice: Boolean) {
        if (isChoice)
            mListChoice.add(mListData[position])
        else
            mListChoice.remove(mListData[position])
    }

    fun getListItemChoice(): MutableList<Song> {
        return mListChoice
    }

    fun choiceAllItem(isChoice: Boolean) {
        if (isChoice) {
            mListChoice.addAll(mListData)
            for (index in mListBoolChoice.indices) {
                mListBoolChoice[index] = true
            }
        } else {
            mListChoice.clear()
            for (index in mListBoolChoice.indices) {
                mListBoolChoice[index] = false

            }
        }
        notifyDataSetChanged()
    }

    /*
     *  class viewholder in recycleview
     */

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var mTvNameSong: TextView = view.findViewById(R.id.tvItemNameSong)
        private var mTvNameSinger: TextView = view.findViewById(R.id.tvItemNameSinger)
        private var mClLayout: ConstraintLayout = view.findViewById(R.id.clPlaylistChoice)
        private var mRbChoice: RadioButton = view.findViewById(R.id.rbItemPlaylistChoice)
        private lateinit var mEventChoice: IEventClickPlaylistChoice

        fun onBind(song: Song, isChoice: Boolean, eventChoice: IEventClickPlaylistChoice) {
            mTvNameSong.text = song.title
            mTvNameSinger.text = song.artist
            mRbChoice.isChecked = isChoice
            mEventChoice = eventChoice
            setListener()
        }

        fun setListener() {
            mClLayout.setOnClickListener {
                val isChoice = !mRbChoice.isChecked
                mRbChoice.isChecked = isChoice
                mEventChoice.onItemPlaylistChoice(adapterPosition, isChoice)
            }
        }

    }
}
