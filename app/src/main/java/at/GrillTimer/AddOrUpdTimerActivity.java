package at.GrillTimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TimePicker;

import java.util.List;

import at.GrillTimer.DB.TimerRepository;

public class AddOrUpdTimerActivity extends AppCompatActivity {
    
    private Timer timer;
    private TimerViewModel timerViewModel;
    private TimerRepository timerRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_upd_timer);

        Toolbar toolbar = findViewById(R.id.toolbar_add_timer);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.inflateMenu(R.menu.menu_add_upd_timer);
        setTitle("Neuer Timer");
        
        Bundle bundle = getIntent().getExtras();
        int timer_id = bundle.getInt("TIMER_ID");

        timerRepository = new TimerRepository(getApplication());
        
        if (timer_id > -1) {
            timer = timerRepository.getTimer(timer_id);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_add_upd_timer, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save_timer) {
            int x = 0;
        }
        else {
            onBackPressed();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
