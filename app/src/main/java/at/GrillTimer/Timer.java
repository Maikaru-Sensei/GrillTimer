package at.GrillTimer;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "timer_table")
public class Timer {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private int seconds;
    private boolean active;
    private boolean repeating;
    private int minutes;

    @Ignore
    public Timer() {
    }

    public Timer(String name, int minutes, int seconds, boolean active, boolean repeating) {
        this.name = name;
        this.seconds = seconds;
        this.active = active;
        this.repeating = repeating;
        this.minutes = minutes;
    }

    public String getFormatTime() {
        int min, sec;

        min = this.minutes;
        sec = this.seconds;

        String min_f = min < 10 ? "0" + min : String.valueOf(min);
        String sec_f = sec < 10 ? "0" + sec : String.valueOf(sec);

        return  min_f + ":" + sec_f;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getMinutes() {
        return this.minutes;
    }

    public int getSecs() {
        return this.seconds%60;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean getRepeating() {
        return repeating;
    }

    public void setRepeating(boolean repeating) {
        this.repeating = repeating;
    }
}
