package android.asiantech.vn.springfinalmusic.timercountdown

import android.asiantech.vn.springfinalmusic.R
import android.asiantech.vn.springfinalmusic.model.Constant
import android.asiantech.vn.springfinalmusic.service.MusicService
import android.asiantech.vn.springfinalmusic.timercountdown.modle.Timer
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import java.util.ArrayList

class FragmentTimerOffApp : DialogFragment(), IListenerApdapterTimer {
    private var mRvTimerOffApp: RecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null
    private lateinit var mTimerList: MutableList<Timer>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_timer_offapp, container, false)
        init(view)
        return view
    }

    private fun init(view: View) {
        initViews(view)
        initListTimer()
        initRecyclerView(view)
        mAdapter?.notifyDataSetChanged()
    }

    private fun initListTimer() {
        mTimerList = ArrayList()
        mTimerList.add(Timer(15))
        mTimerList.add(Timer(30))
        mTimerList.add(Timer(60))
        mTimerList.add(Timer(90))
        mTimerList.add(Timer(120))
    }

    private fun initViews(view: View) {
        mRvTimerOffApp = view.findViewById(R.id.rvTimerOffApp)
    }

    private fun initRecyclerView(view: View) {
        val mLayoutManager = LinearLayoutManager(view.context)
        mRvTimerOffApp?.layoutManager = mLayoutManager
        mAdapter = TimerAdapter(mTimerList, this)
        mRvTimerOffApp?.adapter = mAdapter
        val dividerHorizontal = DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL)
        mRvTimerOffApp?.addItemDecoration(dividerHorizontal)
    }

    override fun onItemClick(minutes: Int) {
        sendActionCountDownToServeice(minutes)
        this.dismiss()
    }

    private fun sendActionCountDownToServeice(minutes: Int) {
        val intent = Intent(activity, MusicService::class.java)
                .setAction(Constant.ACTION_TIMER)
                .putExtra(Constant.KEY_TIME, minutes)

        (activity)?.startService(intent)
    }
}
