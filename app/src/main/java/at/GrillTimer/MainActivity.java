package at.GrillTimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import at.GrillTimer.DB.TimerRepository;

public class MainActivity extends AppCompatActivity implements TimerListFragment.OnActivateTimerListener{

    private final String TAG = "MainActivity";
    private TimerRepository timerRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerReceiver(timerCancelBroadcastReceiver, new IntentFilter("TIMER_CANCEL"));
    }

    BroadcastReceiver timerCancelBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int timer_id = intent.getIntExtra("TIMER_ID", -1);
            Log.d(TAG, "back to Mainactivity: " + timer_id);

            timerRepository = new TimerRepository(getApplication());
            Timer timer = timerRepository.getTimer(timer_id);

            timer.setActive(false);
            timerRepository.update(timer);

            TimerService.IS_CANCELLED = true;
        }
    };

    @Override
    public void onTimerActivated(Timer timer) {
        Intent serviceIntent = new Intent(MainActivity.this, TimerService.class);
        serviceIntent.putExtra("TIMER_ID", timer.getId());
        serviceIntent.putExtra("TIME", timer.getSeconds());
        serviceIntent.putExtra("REPEAT", timer.getRepeating());
        serviceIntent.putExtra("TIMER_NAME", timer.getName());

        Log.d(TAG, "start Timer: " + timer.getName());

        timer.setActive(true);

        timerRepository = new TimerRepository(getApplication());
        timerRepository.update(timer);

        startService(serviceIntent);
    }
}
