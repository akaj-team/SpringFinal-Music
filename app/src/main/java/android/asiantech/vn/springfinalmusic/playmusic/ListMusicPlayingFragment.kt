package android.asiantech.vn.springfinalmusic.playmusic

import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.library.adapter.PlayingListAdapter
import android.asiantech.vn.springfinalmusic.model.Constant
import android.asiantech.vn.springfinalmusic.model.Song
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_list_playing.*

class ListMusicPlayingFragment : AppCompatActivity(), PlayingListAdapter.OnAdapterListenes {
    private lateinit var mLayout: RecyclerView.LayoutManager
    private var mAdapter: PlayingListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_playing)
        initRecyclerView()
        extraData()
    }

    private fun initRecyclerView() {
        mLayout = LinearLayoutManager(this)
        recycleViewListPlaying.setHasFixedSize(true)
        recycleViewListPlaying.layoutManager = mLayout
    }

    private fun extraData() {
        val list: List<Song>? = intent?.extras?.getParcelableArrayList(Constant.KEY_LIST_SONG)
        val position: Int? = intent?.extras?.getInt(Constant.KEY_SONG)
        if (list != null && position != null) {
            mAdapter = PlayingListAdapter(list, position, this, this)
            mAdapter?.notifyDataSetChanged()
        }
        recycleViewListPlaying.adapter = mAdapter
        recycleViewListPlaying.scrollToPosition(position as Int)
        showSongPlaying(list?.get(position))
    }

    private fun showSongPlaying(song: Song?) {
        tvPlayingNameSong.text = song?.title
    }

    override fun onItemSelected() {
        onBackPressed()
    }
}
