package android.asiantech.vn.springfinalmusic.library.adapter

import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.library.*
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.util.Log
import javax.security.auth.login.LoginException

class LibraryPagerAdapter(fm: FragmentManager?, context: Context) : FragmentStatePagerAdapter(fm) {
    private var mContext: Context = context
    var songsFragment: SongsFragment
    var playlistFragment: PlayListFragment
    var artistFragment: ArtistFragment
    var albumFragment: AlbumFragment

    init {
        songsFragment = SongsFragment()
        playlistFragment = PlayListFragment()
        artistFragment = ArtistFragment()
        albumFragment = AlbumFragment()

    }

    override fun getItem(position: Int): Fragment? {
        when (position) {
            LibraryType.SONGS -> {
                return songsFragment
            }
            LibraryType.PLAYLIST -> {
                return playlistFragment
            }
            LibraryType.ARSTIST -> {
                return artistFragment
            }
            LibraryType.ALBUM -> {
                return albumFragment
            }
        }
        return null
    }

    override fun getCount(): Int {
        return 4
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            LibraryType.SONGS -> {
                return mContext.resources.getString(R.string.library_text_songs)
            }
            LibraryType.PLAYLIST -> {
                return mContext.resources.getString(R.string.library_text_playlist)
            }
            LibraryType.ARSTIST -> {
                return mContext.resources.getString(R.string.library_text_singer)
            }
            LibraryType.ALBUM -> {
                return mContext.resources.getString(R.string.library_text_album)
            }
        }
        return null
    }

    fun resetPage(page: Int) {
        when (page) {
            LibraryType.SONGS -> songsFragment.reset()
            LibraryType.ALBUM -> albumFragment.reset()
            LibraryType.PLAYLIST -> playlistFragment.reset()
            LibraryType.ARSTIST -> artistFragment.reset()
        }
    }
}
