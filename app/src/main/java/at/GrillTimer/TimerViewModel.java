package at.GrillTimer;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import at.GrillTimer.DB.TimerRepository;

public class TimerViewModel extends AndroidViewModel {
    private TimerRepository repository;
    private LiveData<List<Timer>> allTimers;

    public TimerViewModel(@NonNull Application application) {
        super(application);

        repository = new TimerRepository(application);
        allTimers = repository.getAllTimers();
    }

    public void insert(Timer timer) {
        repository.insert(timer);
    }

    public void update(Timer timer) {
        repository.update(timer);
    }

    public void delete(Timer timer) {
        repository.delete(timer);
    }

    public LiveData<List<Timer>> getAllTimers() {
        return allTimers;
    }

    public Timer getTimer(int id) {
        return repository.getTimer(id);
    }
}
