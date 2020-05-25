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

    @Ignore
    public Timer() {
    }

    public Timer(String name, int seconds, boolean active, boolean repeating) {
        this.name = name;
        this.seconds = seconds;
        this.active = active;
        this.repeating = repeating;
    }

    public String getFormatTime() {
        int min, sec;

        min = this.seconds / 60;
        sec = this.seconds % 60;

        return min + ":" + sec;
    }

    public int getHours() {
        return this.seconds / 3600;
    }

    public int getMinutes() {
        return this.seconds / 60;
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
