package android.asiantech.vn.springfinalmusic.timercountdown;

import android.annotation.SuppressLint;
import android.asiantech.vn.springfinalmusic.R;
import android.asiantech.vn.springfinalmusic.timercountdown.modle.Timer;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

public class TimerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Timer> mTimerList;
    private IListenerTimer mIListenerTimer;

    TimerAdapter(List<Timer> timerList, IListenerTimer iListenerTimer) {
        this.mTimerList = timerList;
        this.mIListenerTimer = iListenerTimer;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timer, parent, false);
        return new TimerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TimerViewHolder timerViewHolder = (TimerViewHolder) holder;
        timerViewHolder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return mTimerList.size();
    }

    private class TimerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTvTimer;
        private RadioButton mRbChecked;

        TimerViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            mTvTimer = view.findViewById(R.id.tvTimer);
            mRbChecked = view.findViewById(R.id.rbTimerChecked);
        }

        @SuppressLint("SetTextI18n")
        void onBind(int position) {
            mTvTimer.setText("Sau " + mTimerList.get(position).getTimeCount() + " phút");
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (!mTimerList.get(position).isTimeChecked()) {
                mRbChecked.setChecked(true);
                mTimerList.get(position).setTimeChecked(true);
            }
            mIListenerTimer.onCommand(30);
        }
    }
}
