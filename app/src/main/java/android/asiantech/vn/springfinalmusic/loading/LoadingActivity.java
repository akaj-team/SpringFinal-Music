package android.asiantech.vn.springfinalmusic.loading;

import android.app.Activity;
import android.asiantech.vn.springfinalmusic.R;
import android.asiantech.vn.springfinalmusic.home.HomeActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

public class LoadingActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
    }

    @Override
    protected void onStart() {
        super.onStart();
        activeHome();
    }

    private void activeHome()
    {
        Intent intentHome = new Intent(this, HomeActivity.class);
        startActivity(intentHome);
    }
}
