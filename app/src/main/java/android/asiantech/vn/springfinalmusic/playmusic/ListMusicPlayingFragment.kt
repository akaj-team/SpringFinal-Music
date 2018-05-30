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
import java.util.*

class ListMusicPlayingFragment : AppCompatActivity() {
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
        Collections.shuffle(list)//xao tron danh sach
        if (list != null) {
            mAdapter = PlayingListAdapter(list, this)
            mAdapter?.notifyDataSetChanged()
        }
        recycleViewListPlaying.adapter = mAdapter
    }
}