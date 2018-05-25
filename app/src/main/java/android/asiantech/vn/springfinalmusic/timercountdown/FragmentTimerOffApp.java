package android.asiantech.vn.springfinalmusic.timercountdown;

import android.asiantech.vn.springfinalmusic.R;
import android.asiantech.vn.springfinalmusic.service.MusicService;
import android.asiantech.vn.springfinalmusic.timercountdown.modle.Timer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FragmentTimerOffApp extends DialogFragment implements IListenerApdapterTimer {
    public static final String KEY_TIME = "key_time_minutes";
    private RecyclerView mRvTimerOffApp;
    private RecyclerView.Adapter mAdapter;
    private List<Timer> mTimerList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timer_offapp, container, false);
        init(view);
        return view;
    }

    public void init(View view) {
        initViews(view);
        initListTimer();
        initRecyclerView(view);
        mAdapter.notifyDataSetChanged();
    }

    public void initListTimer() {
        mTimerList = new ArrayList<>();
        mTimerList.add(new Timer(15));
        mTimerList.add(new Timer(30));
        mTimerList.add(new Timer(60));
        mTimerList.add(new Timer(90));
        mTimerList.add(new Timer(120));
    }

    private void initViews(View view) {
        mRvTimerOffApp = view.findViewById(R.id.rvTimerOffApp);
    }

    private void initRecyclerView(View view) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        mRvTimerOffApp.setLayoutManager(mLayoutManager);
        mAdapter = new TimerAdapter(mTimerList, this);
        mRvTimerOffApp.setAdapter(mAdapter);
        DividerItemDecoration dividerHorizontal = new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL);
        mRvTimerOffApp.addItemDecoration(dividerHorizontal);
    }

    @Override
    public void onItemClick(int minutes) {
        sendActionCountDownToServeice(minutes);
    }

    private void sendActionCountDownToServeice(int minutes) {
        Intent intent = new Intent(getActivity(), MusicService.class)
                .setAction(MusicService.ACTION_TIMER)
                .putExtra(KEY_TIME, minutes);

        Objects.requireNonNull(getActivity()).startService(intent);
    }
}
