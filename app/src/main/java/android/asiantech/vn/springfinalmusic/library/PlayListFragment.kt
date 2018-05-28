package android.asiantech.vn.springfinalmusic.library

import android.support.v4.app.Fragment
import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.library.adapter.PlaylistAdapter
import android.asiantech.vn.springfinalmusic.library.playlistchoice.PlaylistChoiceActivity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_library_playlist.*
import kotlinx.android.synthetic.main.fragment_library_songs.*

class PlayListFragment : Fragment() {
    private lateinit var mAdapter: PlaylistAdapter
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_library_playlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initRecycleView()
    }

    fun initListeners() {
        btnPlaylistNewPlaylist.setOnClickListener {
            val intent = Intent(activity, PlaylistChoiceActivity::class.java)
            startActivity(intent)
        }
    }

    fun initRecycleView() {
        mLayoutManager = LinearLayoutManager(activity)
        mAdapter = PlaylistAdapter()
        recycleViewPlaylist.setHasFixedSize(true)
        recycleViewPlaylist.layoutManager = mLayoutManager
        recycleViewPlaylist.adapter = mAdapter
    }
}
