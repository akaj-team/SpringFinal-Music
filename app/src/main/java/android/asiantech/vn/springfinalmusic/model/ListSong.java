package android.asiantech.vn.springfinalmusic.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ListSong implements Parcelable {
    private List<Song> songList;
    private int songCurrent;

    public int getSongCurrent() {
        return songCurrent;
    }

    public void setSongCurrent(int songCurrent) {
        this.songCurrent = songCurrent;
    }

    public List<Song> getSongList() {
        return songList;
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }

    public ListSong(List<Song> songList,int songCur) {
        this.songList = songList;
        this.songCurrent=songCur;
    }

    public static final Creator<ListSong> CREATOR = new Creator<ListSong>() {
        @Override
        public ListSong createFromParcel(Parcel in) {
            List<Song> songList = in.createTypedArrayList(Song.CREATOR);
            int songCur=in.readInt();
            return new ListSong(songList,songCur);
        }

        @Override
        public ListSong[] newArray(int size) {
            return new ListSong[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(songList);
        dest.writeInt(songCurrent);
    }
}
