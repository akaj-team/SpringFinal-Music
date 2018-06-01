package android.asiantech.vn.springfinalmusic.library.dialog

import android.app.Dialog
import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.library.playlistchoice.IEventClosePlaylistChoice
import android.asiantech.vn.springfinalmusic.manager.ResourcesManager
import android.asiantech.vn.springfinalmusic.model.Playlist
import android.asiantech.vn.springfinalmusic.model.Song
import android.content.Context
import android.os.Bundle
import android.view.Window
import kotlinx.android.synthetic.main.dialog_create_new_playlist.*

class NewPlaylistDialog(context: Context, data: MutableList<Song>) : Dialog(context) {
    private var mData = data
    private lateinit var mEventClose: IEventClosePlaylistChoice
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_create_new_playlist)
        initListeners()
    }

    private fun initListeners() {
        btnDialogCancel.setOnClickListener {
            dismiss()
        }
        btnDialogCreate.setOnClickListener {
            createNewPlaylist(edtDialogPlaylistName.text.toString(), mData)
        }
    }

    private fun createNewPlaylist(name: String, data: MutableList<Song>) {
        val playList = Playlist(name, data)
        ResourcesManager.getInstance().updatePlaylist(context, playList)
        mEventClose.onClose()
        dismiss()
    }

    fun setListenerClose(evenClose: IEventClosePlaylistChoice) {
        mEventClose = evenClose
    }

}
