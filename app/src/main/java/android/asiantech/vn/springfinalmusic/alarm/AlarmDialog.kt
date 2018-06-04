package android.asiantech.vn.springfinalmusic.alarm

import android.annotation.SuppressLint
import android.app.Dialog
import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.model.Constant
import android.asiantech.vn.springfinalmusic.service.MusicService
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.SeekBar
import kotlinx.android.synthetic.main.dialog_timer_offapp_new.*

class AlarmDialog(context: Context?, minutes: Int?) : Dialog(context), SeekBar.OnSeekBarChangeListener {
    companion object {
        const val TITLE_TIMER = "Hẹn giờ tắt nhạc"
        const val MESSAGE = "phút ứng dụng sẽ tự đông tắt nhạc"
        const val TIMER_DONVI = "phút"
        const val TIMER_END_BODY = 120
    }

    private var mContext = context
    private var mMinutes = 0
    private var mMinutesUntilFinished = minutes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_timer_offapp_new)
        setTitle(TITLE_TIMER)
        initView()
        initEvent()
        changeButtonStart(0)
    }

    @SuppressLint("SetTextI18n")
    private fun updateTimeMessage(minutesUntilFinished: Int?) {
        if (minutesUntilFinished != 0) {
            tvDialogTimeMessage.visibility = VISIBLE
            tvDialogTimeMessage.text = "Sau $minutesUntilFinished $MESSAGE"
        } else {
            tvDialogTimeMessage.visibility = INVISIBLE
        }
    }

    private fun initView() {
        updateTimeMessage(mMinutesUntilFinished)
        seekBarDialogTime.max = TIMER_END_BODY
    }

    private fun initEvent() {
        btnDialogTimerCancel.setOnClickListener {
            this.cancel()
        }
        btnDialogTimerStart.setOnClickListener {
            sendActionCountDownToServeice(mMinutes)
            this.cancel()
        }
        seekBarDialogTime.setOnSeekBarChangeListener(this)
    }

    private fun initBtnStartCountDown() {
        btnDialogTimerStart.text = mContext?.getString(R.string.dialog_timer_start)
        btnDialogTimerStart.setOnClickListener {
            sendActionCountDownToServeice(mMinutes)
            this.cancel()
        }
    }

    private fun initBtnStopCountDown() {
        btnDialogTimerStart.text = mContext?.getString(R.string.dialog_timer_stop)
        btnDialogTimerStart.setOnClickListener {
            sendActionStopCountDown()
            this.cancel()
        }
    }

    private fun sendActionCountDownToServeice(minutes: Int?) {
        if (minutes != 0) {
            val intent = Intent(mContext, MusicService::class.java)
                    .setAction(Constant.ACTION_TIMER_START)
                    .putExtra(Constant.KEY_TIME, minutes)
            (mContext)?.startService(intent)
        }
    }

    private fun sendActionStopCountDown() {
        (mContext)?.startService(Intent(mContext, MusicService::class.java)
                .setAction(Constant.ACTION_TIMER_STOP))
    }

    private fun changeButtonStart(minutes: Int?) {
        if (mMinutesUntilFinished == 0 || minutes != 0) {
            initBtnStartCountDown()
        } else {
            initBtnStopCountDown()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun progressChange(progress: Int?) {
        mMinutes = progress as Int
        changeButtonStart(progress)
        tvDialogTimerBegin.text = "$mMinutes $TIMER_DONVI"
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        progressChange(progress)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        progressChange(seekBar?.progress)
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        progressChange(seekBar?.progress)
    }
}
