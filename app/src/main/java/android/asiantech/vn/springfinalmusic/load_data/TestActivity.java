package android.asiantech.vn.springfinalmusic.load_data;

import android.app.Activity;
import android.asiantech.vn.springfinalmusic.R;
import android.asiantech.vn.springfinalmusic.model.Song;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends Activity {
    private String TAG = "123";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        loaddata();
    }

    private void loaddata() {
        String selection = MediaStore.Audio.Media.IS_MUSIC + " !=0";
        String[] project = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM,
        };
        Cursor cursor;
        cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, project, selection, null, null);
        List<Song> songs = new ArrayList<>();
        while (cursor.moveToNext()) {
            Song song = new Song(Integer.valueOf(cursor.getString(0)), cursor.getString(1)
                    , cursor.getString(2), cursor.getString(3), cursor.getString(4)
                    , Integer.valueOf(cursor.getString(5)), cursor.getString(6));
            songs.add(song);
        }
        for (int i = 0; i < songs.size(); i++) {
            Song song = songs.get(i);
            String s = "";
            s+= song.getId() +" | " + song.getDuration() + " | " + song.getArtist() + " | " + song.getDisplayName();
            Log.e(TAG, "loaddata: " + s);
        }
    }
}
