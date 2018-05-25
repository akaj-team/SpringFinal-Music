package android.asiantech.vn.springfinalmusic.timercountdown.modle;

public class Timer {
    private int timeCount;
    private boolean timeChecked=false;

    public boolean isTimeChecked() {
        return timeChecked;
    }

    public void setTimeChecked(boolean timeChecked) {
        this.timeChecked = timeChecked;
    }

    public int getTimeCount() {
        return timeCount;
    }

    public Timer(int timeCount) {
        this.timeCount = timeCount;
    }
}
