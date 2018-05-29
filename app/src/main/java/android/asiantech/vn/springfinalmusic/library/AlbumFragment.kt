package android.asiantech.vn.springfinalmusic.library

import android.support.v4.app.Fragment

import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.library.adapter.AlbumAdapter
import android.asiantech.vn.springfinalmusic.library.adapter.ArtistAdapter
import android.asiantech.vn.springfinalmusic.manager.ResourcesManager
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_library_page.*

class AlbumFragment : Fragment() {
    private lateinit var mAdapter: AlbumAdapter
    private lateinit var mLayoutManager: RecyclerView.LayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_library_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mLayoutManager = LinearLayoutManager(activity)
        mAdapter = AlbumAdapter(ResourcesManager.getInstance().getListAlbum())
        recycleViewLibraryPage.setHasFixedSize(true)
        recycleViewLibraryPage.layoutManager = mLayoutManager
        recycleViewLibraryPage.adapter = mAdapter
    }
}
