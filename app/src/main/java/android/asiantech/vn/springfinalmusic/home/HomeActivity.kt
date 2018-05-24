package android.asiantech.vn.springfinalmusic.home

import android.app.Activity
import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.library.LibraryActivity
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setListeners()
    }

    private fun setListeners(){
        btnHomeButtonLibrary.setOnClickListener{
            val intent = Intent(this@HomeActivity, LibraryActivity::class.java)
            startActivity(intent)
        }
    }
}