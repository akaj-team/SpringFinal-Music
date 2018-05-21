package android.asiantech.vn.springfinalmusic.timercountdown;

import android.annotation.SuppressLint;
import android.asiantech.vn.springfinalmusic.R;
import android.asiantech.vn.springfinalmusic.timercountdown.modle.Timer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class TimerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Timer> mTimerList;

    TimerAdapter(List<Timer> timerList) {
        this.mTimerList = timerList;
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
        timerViewHolder.onBind(mTimerList.get(position));
    }

    @Override
    public int getItemCount() {
        return mTimerList.size();
    }

    private class TimerViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvTimer;

        TimerViewHolder(View itemView) {
            super(itemView);
            mTvTimer = itemView.findViewById(R.id.tvTimer);
        }

        @SuppressLint("SetTextI18n")
        void onBind(Timer timer) {
            mTvTimer.setText("Sau " + timer.getTimer() + " phút");
        }
    }
}
