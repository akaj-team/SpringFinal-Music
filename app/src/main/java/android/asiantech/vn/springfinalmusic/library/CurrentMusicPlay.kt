package android.asiantech.vn.springfinalmusic.library

import android.app.Activity
import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.library.adapter.CurrentMusicAdapter
import android.asiantech.vn.springfinalmusic.model.Song
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_play_music.*

class CurrentMusicPlay : Activity() {
    private lateinit var mAdapter: CurrentMusicAdapter
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private lateinit var data: ArrayList<Song>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_music)
        getData()
        initListeners()
        initRecycleView()
    }

    fun getData() {
        val action = intent.action
        when (action) {
            FILTER_ARTIST -> {
                data = intent.extras.getParcelableArrayList<Song>(FILTER_ARTIST)
            }

            FILTER_ALBUM -> {
                data = intent.extras.getParcelableArrayList<Song>(FILTER_ALBUM)
            }
        }
    }

    fun initListeners() {
        btnToolBarMusicClose.setOnClickListener {
            onBackPressed()
        }
        val action = intent.action
        when (action) {
            FILTER_ARTIST -> {
                tvToolBarMusic.text = data[0].artist
            }
            FILTER_ALBUM -> {
                tvToolBarMusic.text = data[0].album
            }
        }
    }

    fun initRecycleView() {
        mLayoutManager = LinearLayoutManager(this)
        mAdapter = CurrentMusicAdapter(data)
        recycleViewMusic.setHasFixedSize(true)
        recycleViewMusic.layoutManager = mLayoutManager
        recycleViewMusic.adapter = mAdapter
    }


    companion object {
        val FILTER_ARTIST = "Artist"
        val FILTER_ALBUM = "Album"
    }
}