package android.asiantech.vn.springfinalmusic.home

import android.support.v4.app.Fragment
import android.asiantech.vn.springfinalmusic.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class SongsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_library_songs, container, false)
    }
}