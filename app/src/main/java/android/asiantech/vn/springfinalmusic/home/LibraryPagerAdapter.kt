package android.asiantech.vn.springfinalmusic.home

import android.asiantech.vn.springfinalmusic.R
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

class LibraryPagerAdapter(fm: FragmentManager?, context: Context) : FragmentStatePagerAdapter(fm) {
    private var mContext: Context = context
    override fun getItem(position: Int): Fragment {
        when (position) {
            LibraryType.SONGS -> {
                return SongsFragment()
            }
            LibraryType.PLAYLIST -> {
                return PlayListFragment()
            }
            LibraryType.ARSTIST -> {
                return ArtistFragment()
            }
            LibraryType.ALBUM -> {
                return AlbumFragment()
            }
            else -> {
                return SongsFragment()
            }
        }
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
            else -> {
                return ""
            }
        }
    }

}