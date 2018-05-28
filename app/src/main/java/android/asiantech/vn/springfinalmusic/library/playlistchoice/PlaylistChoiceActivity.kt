package android.asiantech.vn.springfinalmusic.library.playlistchoice

import android.app.Activity
import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.manager.ResourcesManager
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_playlist_choice_song.*

class PlaylistChoiceActivity : Activity() {
    private lateinit var mAdapter: PlaylistChoiceAdapter
    private lateinit var mLayoutwManager: RecyclerView.LayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist_choice_song)
        initRecycleView()
        initListeners()
    }

    fun initRecycleView() {
        mLayoutwManager = LinearLayoutManager(this)
        mAdapter = PlaylistChoiceAdapter(ResourcesManager.getInstance().getallSongFromDevice())
        recycleViewPlaylistChoice.setHasFixedSize(true)
        recycleViewPlaylistChoice.layoutManager = mLayoutwManager
        recycleViewPlaylistChoice.adapter = mAdapter
    }

    fun initListeners() {
        btnToolBarButtonDown.setOnClickListener {
            onBackPressed()
        }
    }
}
