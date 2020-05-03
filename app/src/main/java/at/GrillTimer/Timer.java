package at.GrillTimer;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "timer_table")
public class Timer {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private int seconds;
    private int active;

    public Timer() {
    }

    public Timer(String name, int seconds, int active) {
        this.name = name;
        this.seconds = seconds;
        this.active = active;
    }

    public String getFormatTime() {
        int min, sec;

        min = this.seconds / 60;
        sec = this.seconds % 60;

        return min + ":" + sec;
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

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}
