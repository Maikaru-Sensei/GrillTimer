package at.GrillTimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements TimerListFragment.OnActivateTimerListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onTimerActivated(Timer timer) {
        if (!TimerService.IS_RUNNING) {
            Intent serviceIntent = new Intent(MainActivity.this, TimerService.class);
            serviceIntent.putExtra("TIME", timer.getSeconds());
            serviceIntent.putExtra("REPEAT", timer.getRepeating());

            TimerService.IS_RUNNING = true;

            startService(serviceIntent);
        }
    }
}
