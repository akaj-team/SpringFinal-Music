package android.asiantech.vn.springfinalmusic.library

import android.support.v4.app.Fragment
import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.library.adapter.SongsAdapter
import android.asiantech.vn.springfinalmusic.manager.ResourcesManager
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_library_songs.*

class SongsFragment : Fragment(){
    private lateinit var mAdapter: SongsAdapter
    private lateinit var mViewManager: RecyclerView.LayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_library_songs, container, false) as View
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycleView()
    }

    fun initRecycleView() {
        mViewManager = LinearLayoutManager(activity)
        mAdapter = SongsAdapter(ResourcesManager.getInstance().getallSongFromDevice(),context)
        recycleViewSongs.setHasFixedSize(true)
        recycleViewSongs.layoutManager = mViewManager
        recycleViewSongs.adapter = mAdapter
    }
}
