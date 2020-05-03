package at.GrillTimer.DB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import at.GrillTimer.Timer;

@Dao
public interface TimerDao {

    @Insert
    void insert(Timer timer);

    @Delete
    void delete(Timer timer);

    @Update
    void update(Timer timer);

    @Query("SELECT * FROM timer_table ORDER BY id desc")
    LiveData<List<Timer>> getAllTimers();
}
