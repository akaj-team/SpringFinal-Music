package android.asiantech.vn.springfinalmusic.loading;

import android.asiantech.vn.springfinalmusic.R;
import android.asiantech.vn.springfinalmusic.home.HomeActivity;
import android.asiantech.vn.springfinalmusic.service.MusicService;
import android.asiantech.vn.springfinalmusic.timercountdown.FragmentTimerOffApp;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class LoadingActivityTest extends AppCompatActivity {
    Button button;
    Button btnTestTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        button = findViewById(R.id.btnTest);
        btnTestTimer = findViewById(R.id.btnTestTimer);
    }

    @Override
    protected void onStart() {
        super.onStart();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoadingActivityTest.this, MusicService.class).setAction(MusicService.NOTI_BTN_PLAY_CLICK);
                startService(intent);
            }
        });
        //activeHome();

        btnTestTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMenuimer();
            }
        });
    }

    private void loadMenuimer() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTimerOffApp userInfoDialog = new FragmentTimerOffApp();
        userInfoDialog.showNow(fm, "Hen gio");
    }

    private void activeHome() {
        Intent intentHome = new Intent(this, HomeActivity.class);
        startActivity(intentHome);
    }
}
