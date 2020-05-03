package at.GrillTimer.DB;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import at.GrillTimer.Timer;

public class TimerRepository {

    private TimerDao timerDao;
    private LiveData<List<Timer>> allTimers;

    public TimerRepository(Application application) {
        TimerDatabase timerDatabase = TimerDatabase.getInstance(application);
        timerDao = timerDatabase.timerDao();
        allTimers = timerDao.getAllTimers();
    }

    public void insert(Timer timer) {
        new InsertAsyncTask(timerDao).execute(timer);
    }

    public void update(Timer timer) {
        new UpdateAsyncTask(timerDao).execute(timer);
    }

    public void delete(Timer timer) {
        new DeleteAsyncTask(timerDao).execute(timer);
    }

    public LiveData<List<Timer>> getAllTimers() {
        return allTimers;
    }

    private static class InsertAsyncTask extends AsyncTask<Timer, Void, Void> {

        private TimerDao timerDao;

        public InsertAsyncTask(TimerDao dishDao) {
            this.timerDao = dishDao;
        }

        @Override
        protected Void doInBackground(Timer... timers) {
            timerDao.insert(timers[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Timer, Void, Void> {

        private TimerDao timerDao;

        public UpdateAsyncTask(TimerDao dishDao) {
            this.timerDao = dishDao;
        }

        @Override
        protected Void doInBackground(Timer... timers) {
            timerDao.update(timers[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Timer, Void, Void> {

        private TimerDao timerDao;

        public DeleteAsyncTask(TimerDao dishDao) {
            this.timerDao = dishDao;
        }

        @Override
        protected Void doInBackground(Timer... timers) {
            timerDao.delete(timers[0]);
            return null;
        }
    }
}