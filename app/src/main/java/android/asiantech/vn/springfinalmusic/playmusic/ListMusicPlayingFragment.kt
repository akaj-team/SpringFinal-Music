package android.asiantech.vn.springfinalmusic.playmusic

import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.library.adapter.PlayingListAdapter
import android.asiantech.vn.springfinalmusic.model.Song
import android.asiantech.vn.springfinalmusic.service.MusicService
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_list_playing.*

class ListMusicPlayingFragment : AppCompatActivity(), PlayingListAdapter.OnAdapterListenes {
    private lateinit var mLayout: RecyclerView.LayoutManager
    private var mAdapter: PlayingListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_list_playing)
        initRecyclerView()
        extraData()
    }

    private fun initRecyclerView() {
        mLayout = LinearLayoutManager(this)
        recycleViewListPlaying.setHasFixedSize(true)
        recycleViewListPlaying.layoutManager = mLayout
    }

    private fun extraData() {
        val list: List<Song>? = intent?.extras?.getParcelableArrayList(MusicService.KEY_SONG_LIST)
        val position: Int? = intent?.extras?.getInt(MusicService.KEY_SONG)
        if (list != null && position != null) {
            mAdapter = PlayingListAdapter(list, this, this)
            mAdapter?.notifyDataSetChanged()
        }
        recycleViewListPlaying.adapter = mAdapter
    }

    override fun onItemSelected() {
        onBackPressed()
    }
}