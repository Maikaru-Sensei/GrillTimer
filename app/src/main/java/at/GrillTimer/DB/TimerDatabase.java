package at.GrillTimer.DB;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import at.GrillTimer.Timer;

@Database(entities = Timer.class, version = 1, exportSchema = false)
public abstract class TimerDatabase extends RoomDatabase {

    private static TimerDatabase instance;

    public abstract TimerDao timerDao();

    public static synchronized TimerDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), TimerDatabase.class, "timer_db")
                    .addCallback(roomCallback)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private TimerDao timerDao;

        private PopulateDbAsyncTask(TimerDatabase db) {
            timerDao = db.timerDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
