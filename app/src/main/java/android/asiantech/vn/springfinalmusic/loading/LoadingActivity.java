package android.asiantech.vn.springfinalmusic.loading;

import android.app.Activity;
import android.asiantech.vn.springfinalmusic.R;
import android.asiantech.vn.springfinalmusic.home.HomeActivity;
import android.asiantech.vn.springfinalmusic.service.MusicService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

public class LoadingActivity extends Activity {
    Button button;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        button=findViewById(R.id.btnTest);
    }

    @Override
    protected void onStart() {
        super.onStart();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoadingActivity.this, MusicService.class);
                startService(intent);
            }
        });
        //activeHome();
    }

    private void activeHome() {
        Intent intentHome = new Intent(this, HomeActivity.class);
        startActivity(intentHome);
    }
}
