package android.asiantech.vn.springfinalmusic.library

import android.asiantech.vn.springfinalmusic.R
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_library.*

class LibraryActivity : AppCompatActivity() {
    private lateinit var mPagerAdapter: LibraryPagerAdapter
    private var mIsShowSearch: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)
        setListeners()
        initViews()
    }

    private fun initViews() {
        mPagerAdapter = LibraryPagerAdapter(supportFragmentManager, this)
        vpMusicLibrary.adapter = mPagerAdapter
        tabLayoutLibrary.setTabTextColors(ContextCompat.getColor(this, R.color.colorBlack), ContextCompat.getColor(this, R.color.colorLightBlue))
        tabLayoutLibrary.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorLightBlue))
        tabLayoutLibrary.setupWithViewPager(vpMusicLibrary)
    }

    private fun setListeners() {
        showSearch()
        btnToolBarButtonBack.setOnClickListener {
            onBackPressed()
        }
        btnToolBarButtonSearch.setOnClickListener {
            mIsShowSearch = !mIsShowSearch
            showSearch(mIsShowSearch)
        }
    }

    private fun showSearch(isShow: Boolean = false) {
        if (isShow) {
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
}
