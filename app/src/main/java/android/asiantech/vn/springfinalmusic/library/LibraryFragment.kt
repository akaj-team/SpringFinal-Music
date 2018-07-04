package android.asiantech.vn.springfinalmusic.library

import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.home.HomeActivity
import android.asiantech.vn.springfinalmusic.library.adapter.LibraryPagerAdapter
import android.asiantech.vn.springfinalmusic.manager.ResourcesManager
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_library.*
import android.view.inputmethod.InputMethodManager

class LibraryFragment : Fragment() {
    private lateinit var mPagerAdapter: LibraryPagerAdapter
    private var mIsShowSearch: Boolean = false
    private var mCurrentPage = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_library, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
        setListeners()
    }

    private fun initViews() {
        mPagerAdapter = LibraryPagerAdapter(activity?.supportFragmentManager, context)
        vpMusicLibrary.adapter = mPagerAdapter
        vpMusicLibrary.offscreenPageLimit = 0
        tabLayoutLibrary.setTabTextColors(ContextCompat.getColor(activity as Context, R.color.colorBlack), ContextCompat.getColor(activity as Context, R.color.colorLightBlue))
        tabLayoutLibrary.setSelectedTabIndicatorColor(ContextCompat.getColor(activity as Context, R.color.colorLightBlue))
        tabLayoutLibrary.setupWithViewPager(vpMusicLibrary)
        showSearch()
    }

    private fun setListeners() {
        btnToolBarButtonBack.setOnClickListener {
            activity?.onBackPressed()
        }
        btnToolBarButtonSearch.setOnClickListener {
            mIsShowSearch = !mIsShowSearch
            showSearch(mIsShowSearch)
            showInputKeyboard(mIsShowSearch)
            if (!mIsShowSearch) {
                mPagerAdapter?.resetPage(mCurrentPage)
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
                    mPagerAdapter?.resetPage(mCurrentPage)
                }
            }
            LibraryType.PLAYLIST -> {
                if (mCurrentPage != LibraryType.PLAYLIST) {
                    mPagerAdapter?.resetPage(mCurrentPage)
                }
            }
            LibraryType.ARSTIST -> {
                if (mCurrentPage != LibraryType.ARSTIST) {
                    mPagerAdapter?.resetPage(mCurrentPage)
                }
            }
            LibraryType.ALBUM -> {
                if (mCurrentPage != LibraryType.ALBUM) {
                    mPagerAdapter?.resetPage(mCurrentPage)
                }
            }
        }
    }

    private fun loadDataSearch(s: CharSequence?) {
        when (vpMusicLibrary.currentItem) {
            LibraryType.SONGS -> {
                mPagerAdapter?.songsFragment?.setListSong(ResourcesManager.getInstance().getDataSearchAll(s.toString()))
            }
            LibraryType.PLAYLIST -> {
                mPagerAdapter?.playlistFragment?.setListPlaylist(ResourcesManager.getInstance().getDataSearchPlayList(s.toString()))
            }
            LibraryType.ARSTIST -> {
                mPagerAdapter?.artistFragment?.setListArtist(ResourcesManager.getInstance().getDataSearchArtist(s.toString()))
            }
            LibraryType.ALBUM -> {
                mPagerAdapter?.albumFragment?.setListAlbum(ResourcesManager.getInstance().getDataSearchAlbum(s.toString()))
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
        val inputManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (isShow) {
            inputManager.showSoftInput(edtToolBarSearch, InputMethodManager.SHOW_IMPLICIT)
        } else {
            inputManager.hideSoftInputFromWindow(edtToolBarSearch.windowToken, 0)
        }
    }

    override fun onDestroyView() {
        val currentActivity = activity
        if (currentActivity is HomeActivity) {
            currentActivity.showViews(true)
        }
        val index = vpMusicLibrary.currentItem
        mPagerAdapter.destroyItem(vpMusicLibrary, index, mPagerAdapter.getItem(index) as Any)
        super.onDestroyView()
    }
}
