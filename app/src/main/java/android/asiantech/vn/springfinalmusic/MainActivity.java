package android.asiantech.vn.springfinalmusic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView TEXT_VIEW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TEXT_VIEW  = findViewById(R.id.tvContent);
        TEXT_VIEW.setText("test CI");
    }
}
