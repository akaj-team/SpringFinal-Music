package android.asiantech.vn.springfinalmusic.library

import android.support.v4.app.Fragment
import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.library.adapter.SongsAdapter
import android.asiantech.vn.springfinalmusic.load_data.ResourcesManager
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_library_songs.*

class SongsFragment : Fragment() {
    private lateinit var mAdapter: SongsAdapter
    private lateinit var mViewManager: RecyclerView.LayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_library_songs, container, false) as View
    }

    override fun onStart() {
        super.onStart()
        mViewManager = LinearLayoutManager(activity)
        mAdapter = SongsAdapter(ResourcesManager.getInstance().getallSongFromDevice())
        recycleViewSongs.setHasFixedSize(true)
        recycleViewSongs.layoutManager = mViewManager
        recycleViewSongs.adapter = mAdapter
    }

}
