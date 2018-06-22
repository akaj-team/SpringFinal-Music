package android.asiantech.vn.springfinalmusic.library.playlistchoice

import android.app.Activity
import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.library.adapter.PlaylistChoiceAdapter
import android.asiantech.vn.springfinalmusic.library.dialog.NewPlaylistDialog
import android.asiantech.vn.springfinalmusic.manager.ResourcesManager
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_playlist_choice_song.*

class PlaylistChoiceActivity : Activity(), IEventClosePlaylistChoice {
    private lateinit var mAdapter: PlaylistChoiceAdapter
    private lateinit var mLayoutwManager: RecyclerView.LayoutManager
    private var mIsChoiceAll = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist_choice_song)
        initRecycleView()
        initListeners()
    }

    private fun initRecycleView() {
        mLayoutwManager = LinearLayoutManager(this)
        mAdapter = PlaylistChoiceAdapter(ResourcesManager.getInstance().getAllSongFromDevice())
        recycleViewPlaylistChoice.setHasFixedSize(true)
        recycleViewPlaylistChoice.layoutManager = mLayoutwManager
        recycleViewPlaylistChoice.adapter = mAdapter
    }

    private fun initListeners() {
        btnToolBarButtonDown.setOnClickListener {
            onBackPressed()
        }
        viewChoiceAll.setOnClickListener {
            mIsChoiceAll = !mIsChoiceAll
            if (!mIsChoiceAll) {
                tvPlaylistChoiceAddAll.text = getString(R.string.playlistchoice_text_add_all)
            } else {
                tvPlaylistChoiceAddAll.text = getString(R.string.playlist_choice_remove_add_all)
            }
            mAdapter.choiceAllItem(mIsChoiceAll)
        }
        viewAdd.setOnClickListener {
            val dialog = NewPlaylistDialog(this, mAdapter.getListItemChoice())
            dialog.setListenerClose(this)
            dialog.show()
        }
    }

    override fun onClose() {
        setResult(Activity.RESULT_OK)
        onBackPressed()
    }
}
