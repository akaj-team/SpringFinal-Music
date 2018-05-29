package android.asiantech.vn.springfinalmusic.timercountdown.modle;

public class Timer {
    private int minute;
    private boolean timeChecked = false;

    public boolean isTimeChecked() {
        return timeChecked;
    }

    public void setTimeChecked(boolean timeChecked) {
        this.timeChecked = timeChecked;
    }

    public int getMinute() {
        return minute;
    }

    public Timer(int minute) {
        this.minute = minute;
    }
}
