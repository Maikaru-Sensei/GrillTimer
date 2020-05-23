package at.GrillTimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.List;

import at.GrillTimer.DB.TimerRepository;

public class AddOrUpdTimerActivity extends AppCompatActivity {
    
    private Timer timer;
    private TimerViewModel timerViewModel;
    private TimerRepository timerRepository;
    private final String TAG = "AddOrUpdTimerActivity";
    private String title;
    private EditText editTextHour, editTextMin, editTextSec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_upd_timer);

        Toolbar toolbar = findViewById(R.id.toolbar_add_timer);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.inflateMenu(R.menu.menu_add_upd_timer);
        
        Bundle bundle = getIntent().getExtras();
        int timer_id = bundle.getInt("TIMER_ID");

        Log.d(TAG, "called with TIMER_ID: " + timer_id);

        timerRepository = new TimerRepository(getApplication());
        
        if (timer_id > -1) {
            timer = timerRepository.getTimer(timer_id);
            title = "Edit Timer";
        }
        else {
            timer = null;
            title = "Neuer Timer";
        }

        toolbar.setTitle(title);

        editTextHour = findViewById(R.id.editText_hour);
        editTextMin = findViewById(R.id.editText_min);
        editTextSec = findViewById(R.id.editText_sec);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_add_upd_timer, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (timer == null || item.getItemId() == 16908332 /* back button */) {
            goBackToMainScreen();
        }
        else {
            if (item.getItemId() == R.id.save_timer) {
                // create new timer and insert
                timerRepository.insert(timer);
            }
            else if (item.getItemId() == R.id.delete_timer) {
                Log.d(TAG, "delete timer with Id: " + timer.getId());

                timerRepository.delete(timer);
            }
        }

        goBackToMainScreen();

        return super.onOptionsItemSelected(item);
    }

    private void goBackToMainScreen() {
        onBackPressed();
        finish();
    }
}
