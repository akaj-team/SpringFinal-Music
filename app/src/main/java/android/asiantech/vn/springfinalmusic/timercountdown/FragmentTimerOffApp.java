package android.asiantech.vn.springfinalmusic.timercountdown;

import android.asiantech.vn.springfinalmusic.R;
import android.asiantech.vn.springfinalmusic.timercountdown.modle.Timer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FragmentTimerOffApp extends Fragment {
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
        mAdapter = new TimerAdapter(mTimerList);
        mRvTimerOffApp.setAdapter(mAdapter);
        DividerItemDecoration dividerHorizontal = new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL);
        mRvTimerOffApp.addItemDecoration(dividerHorizontal);
    }
}
