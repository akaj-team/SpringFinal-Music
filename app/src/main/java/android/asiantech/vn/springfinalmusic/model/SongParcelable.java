package android.asiantech.vn.springfinalmusic.model;

import android.os.Parcel;
import android.os.Parcelable;
public class SongParcelable extends Song implements Parcelable {

    public SongParcelable(Song song) {
        super(song.getId(), song.getArtist(), song.getTitle(), song.getData(), song.getDisplayName()
                , song.getDuration(), song.getAlbum());
    }

    protected SongParcelable(Parcel in) {
        super(in.readInt(), in.readString(), in.readString(), in.readString(), in.readString(), in.readInt(), in.readString());
    }

    public static final Creator<SongParcelable> CREATOR = new Creator<SongParcelable>() {
        @Override
        public SongParcelable createFromParcel(Parcel in) {
            return new SongParcelable(in);
        }

        @Override
        public SongParcelable[] newArray(int size) {
            return new SongParcelable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeString(getArtist());
        dest.writeString(getTitle());
        dest.writeString(getData());
        dest.writeString(getDisplayName());
        dest.writeInt(getDuration());
        dest.writeString(getAlbum());
    }
}
