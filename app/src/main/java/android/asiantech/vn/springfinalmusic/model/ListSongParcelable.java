package android.asiantech.vn.springfinalmusic.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ListSongParcelable implements Parcelable{
    List<Song> songList;

    public ListSongParcelable(List<Song> list){
        songList=list;
    }

    protected ListSongParcelable(Parcel in) {
        songList=new ArrayList<>();
        in.readList(songList,null);
    }

    public static final Creator<ListSongParcelable> CREATOR = new Creator<ListSongParcelable>() {
        @Override
        public ListSongParcelable createFromParcel(Parcel in) {
            return new ListSongParcelable(in);
        }

        @Override
        public ListSongParcelable[] newArray(int size) {
            return new ListSongParcelable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(songList);
    }
}
