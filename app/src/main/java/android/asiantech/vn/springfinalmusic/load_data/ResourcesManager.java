package android.asiantech.vn.springfinalmusic.load_data;

import android.asiantech.vn.springfinalmusic.model.Song;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

public class ResourcesManager {
    private List<Song> mListSong = new ArrayList<>();
    private Context mContext;

    ResourcesManager(Context context) {
        mContext = context;
    }

    public void loadAllSong() {
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
        cursor = mContext.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, project, selection, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = Integer.valueOf(cursor.getString(0));
                int duration = Integer.valueOf(cursor.getString(5));
                Song song = new Song(id, cursor.getString(1)
                        , cursor.getString(2), cursor.getString(3), cursor.getString(4)
                        , duration, cursor.getString(6));
                mListSong.add(song);
            }
            cursor.close();
        }
    }

    public List<Song> getAllSongFromDevice() {
        return mListSong;
    }
}
