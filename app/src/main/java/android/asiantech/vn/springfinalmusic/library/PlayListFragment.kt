package android.asiantech.vn.springfinalmusic.library

import android.app.Activity
import android.support.v4.app.Fragment
import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.library.adapter.PlaylistAdapter
import android.asiantech.vn.springfinalmusic.library.playlistchoice.PlaylistChoiceActivity
import android.asiantech.vn.springfinalmusic.model.Playlist
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_library_playlist.*

class PlayListFragment : Fragment() {

    private lateinit var mAdapter: PlaylistAdapter
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private var mIsShowCloseButton = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_library_playlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initRecycleView()
    }

    fun initListeners() {
        viewMiniBarPlaylistNew.setOnClickListener {
            val intent = Intent(activity, PlaylistChoiceActivity::class.java)
            mIsShowCloseButton = false
            mAdapter.setIsShowButtonClose(mIsShowCloseButton)
            startActivityForResult(intent, LibraryType.ADD_PLAYLIST)
        }
        viewMiniBarPlaylistEdit.setOnClickListener {
            mIsShowCloseButton = !mIsShowCloseButton
            mAdapter.setIsShowButtonClose(mIsShowCloseButton)
        }

    }

    fun initRecycleView() {
        mLayoutManager = LinearLayoutManager(activity)
        mAdapter = PlaylistAdapter()
        recycleViewPlaylist.setHasFixedSize(true)
        recycleViewPlaylist.layoutManager = mLayoutManager
        recycleViewPlaylist.adapter = mAdapter
    }

    fun setListPlaylist(data: MutableList<Playlist>) {
        mAdapter.setListPlaylist(data)
    }

    fun reset() {
        mAdapter.reset()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == LibraryType.ADD_PLAYLIST && resultCode == Activity.RESULT_OK) {
            mAdapter.reset()
        }
    }

}
