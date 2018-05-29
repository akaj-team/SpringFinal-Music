package android.asiantech.vn.springfinalmusic.timercountdown

import android.annotation.SuppressLint
import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.timercountdown.modle.Timer
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView

class TimerAdapter internal constructor(private val mTimerList: List<Timer>, private val mIListener: IListenerApdapterTimer) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_timer, parent, false)
        return TimerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val timerViewHolder = holder as TimerViewHolder
        timerViewHolder.onBind(position)
    }

    override fun getItemCount(): Int {
        return mTimerList.size
    }

    private inner class TimerViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private val mTvTimer: TextView
        private val mRbChecked: RadioButton

        init {
            view.setOnClickListener(this)
            mTvTimer = view.findViewById(R.id.tvTimer)
            mRbChecked = view.findViewById(R.id.rbTimerChecked)
        }

        @SuppressLint("SetTextI18n")
        internal fun onBind(position: Int) {
            mTvTimer.text = "Sau " + mTimerList[position].minute + " phút"
        }

        override fun onClick(v: View) {
            val position = adapterPosition
            if (!mTimerList[position].isTimeChecked) {
                mRbChecked.isChecked = true
                mTimerList[position].isTimeChecked = true
            }
            mIListener.onItemClick(mTimerList[position].minute)
        }
    }
}
