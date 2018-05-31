package android.asiantech.vn.springfinalmusic.library

import android.support.v4.app.Fragment
import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.library.adapter.ArtistAdapter
import android.asiantech.vn.springfinalmusic.library.adapter.SongsAdapter
import android.asiantech.vn.springfinalmusic.manager.ResourcesManager
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_library_page.*

class ArtistFragment : Fragment() {
    private lateinit var mAdapter: ArtistAdapter
    private lateinit var mLayoutManager: RecyclerView.LayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_library_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mLayoutManager = LinearLayoutManager(activity)
        mAdapter = ArtistAdapter(ResourcesManager.getInstance().getListArtist(), context)
        recycleViewLibraryPage.setHasFixedSize(true)
        recycleViewLibraryPage.layoutManager = mLayoutManager
        recycleViewLibraryPage.adapter = mAdapter
    }

    fun setListArtist(data: List<String>) {
        mAdapter.setListArtist(data)
    }

    fun reset() {
        mAdapter.reset()
    }
}
