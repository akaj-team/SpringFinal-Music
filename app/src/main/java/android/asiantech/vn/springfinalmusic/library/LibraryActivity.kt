package android.asiantech.vn.springfinalmusic.library

import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.library.adapter.LibraryPagerAdapter
import android.asiantech.vn.springfinalmusic.manager.ResourcesManager
import android.content.Context
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import kotlinx.android.synthetic.main.activity_library.*
import android.view.inputmethod.InputMethodManager

class LibraryActivity : AppCompatActivity() {
    companion object {
        var isActive = false
    }
    private lateinit var mPagerAdapter: LibraryPagerAdapter
    private var mIsShowSearch: Boolean = false
    private var mCurrentPage = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)
        setListeners()
        initViews()
        isActive = true
    }

    private fun initViews() {
        mPagerAdapter = LibraryPagerAdapter(supportFragmentManager, this)
        vpMusicLibrary.adapter = mPagerAdapter
        tabLayoutLibrary.setTabTextColors(ContextCompat.getColor(this, R.color.colorBlack), ContextCompat.getColor(this, R.color.colorLightBlue))
        tabLayoutLibrary.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorLightBlue))
        tabLayoutLibrary.setupWithViewPager(vpMusicLibrary)
        showSearch()
    }

    private fun setListeners() {
        btnToolBarButtonBack.setOnClickListener {
            onBackPressed()
        }
        btnToolBarButtonSearch.setOnClickListener {
            mIsShowSearch = !mIsShowSearch
            showSearch(mIsShowSearch)
            showInputKeyboard(mIsShowSearch)
            if (!mIsShowSearch) {
                mPagerAdapter.resetPage(mCurrentPage)
            }
        }
        edtToolBarSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                loadDataSearch(s)
            }
        })
        vpMusicLibrary.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                if (!TextUtils.isEmpty(edtToolBarSearch.text.trim())) {
                    resetPage()
                    edtToolBarSearch.text.clear()
                    mIsShowSearch = false
                    showSearch(mIsShowSearch)
                    showInputKeyboard(false)
                }
                mCurrentPage = vpMusicLibrary.currentItem
            }

        })
    }

    private fun resetPage() {
        when (vpMusicLibrary.currentItem) {
            LibraryType.SONGS -> {
                if (mCurrentPage != LibraryType.SONGS) {
                    mPagerAdapter.resetPage(mCurrentPage)
                }
            }
            LibraryType.PLAYLIST -> {
                if (mCurrentPage != LibraryType.PLAYLIST) {
                    mPagerAdapter.resetPage(mCurrentPage)
                }
            }
            LibraryType.ARSTIST -> {
                if (mCurrentPage != LibraryType.ARSTIST) {
                    mPagerAdapter.resetPage(mCurrentPage)
                }
            }
            LibraryType.ALBUM -> {
                if (mCurrentPage != LibraryType.ALBUM) {
                    mPagerAdapter.resetPage(mCurrentPage)
                }
            }
        }
    }

    private fun loadDataSearch(s: CharSequence?) {
        when (vpMusicLibrary.currentItem) {
            LibraryType.SONGS -> {
                mPagerAdapter.songsFragment.setListSong(ResourcesManager.getInstance().getDataSearchAll(s.toString()))
            }
            LibraryType.PLAYLIST -> {
                mPagerAdapter.playlistFragment.setListPlaylist(ResourcesManager.getInstance().getDataSearchPlayList(s.toString()))
            }
            LibraryType.ARSTIST -> {
                mPagerAdapter.artistFragment.setListArtist(ResourcesManager.getInstance().getDataSearchArtist(s.toString()))
            }
            LibraryType.ALBUM -> {
                mPagerAdapter.albumFragment.setListAlbum(ResourcesManager.getInstance().getDataSearchAlbum(s.toString()))
            }
        }
    }

    private fun showSearch(isShow: Boolean = false) {
        if (isShow) {
            edtToolBarSearch.text.clear()
            edtToolBarSearch.visibility = View.VISIBLE
            tvToolBarName.visibility = View.GONE
            btnToolBarButtonBack.visibility = View.GONE
            btnToolBarButtonSearch.setBackgroundResource(R.drawable.custom_notification_icon_close)
        } else {
            edtToolBarSearch.visibility = View.GONE
            tvToolBarName.visibility = View.VISIBLE
            btnToolBarButtonBack.visibility = View.VISIBLE
            btnToolBarButtonSearch.setBackgroundResource(R.drawable.custom_notification_button_search)
        }
    }

    private fun showInputKeyboard(isShow: Boolean = true) {
        edtToolBarSearch.requestFocus()
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (isShow) {
            inputManager.showSoftInput(edtToolBarSearch, InputMethodManager.SHOW_IMPLICIT)
        } else {
            inputManager.hideSoftInputFromWindow(edtToolBarSearch.windowToken, 0)
        }
    }

    override fun onDestroy() {
        isActive = false
        super.onDestroy()
    }
}
