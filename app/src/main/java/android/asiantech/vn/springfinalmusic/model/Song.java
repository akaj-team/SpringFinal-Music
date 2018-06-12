package android.asiantech.vn.springfinalmusic.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable {
    private int id;
    private String artist;
    private String title;
    private String data;
    private String displayName;
    private int duration;
    private String album;
    private int dayModified;

    protected Song(Parcel in) {
        id = in.readInt();
        artist = in.readString();
        title = in.readString();
        data = in.readString();
        displayName = in.readString();
        duration = in.readInt();
        album = in.readString();
        dayModified = in.readInt();
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(artist);
        dest.writeString(title);
        dest.writeString(data);
        dest.writeString(displayName);
        dest.writeInt(duration);
        dest.writeString(album);
        dest.writeInt(dayModified);
    }

    public Song(int id, String artist, String title, String data, String displayName, int duration, String album, int dayModified) {
        this.id = id;
        this.artist = artist;
        this.title = title;
        this.data = data;
        this.displayName = displayName;
        this.duration = duration;
        this.album = album;
        this.dayModified = dayModified;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getDayModified() {
        return dayModified;
    }

    public void setDayModified(int dayAdded) {
        this.dayModified = dayAdded;
    }
}
