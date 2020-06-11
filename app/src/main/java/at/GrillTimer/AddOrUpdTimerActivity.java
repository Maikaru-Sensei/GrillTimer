package at.GrillTimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.List;

import at.GrillTimer.DB.TimerRepository;

public class AddOrUpdTimerActivity extends AppCompatActivity {
    
    private Timer timer;
    private TimerViewModel timerViewModel;
    private TimerRepository timerRepository;
    private final String TAG = "AddOrUpdTimerActivity";
    private String title;
    private EditText editTextMin, editTextSec, editTextName;
    private CheckBox checkBoxRepeat;
    private TextView textViewError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_upd_timer);

        Toolbar toolbar = findViewById(R.id.toolbar_add_timer);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.inflateMenu(R.menu.menu_add_upd_timer);

        editTextMin = findViewById(R.id.editText_min);
        editTextSec = findViewById(R.id.editText_sec);
        editTextName = findViewById(R.id.editText_name);
        checkBoxRepeat = findViewById(R.id.checkBox_repeat);
        textViewError = findViewById(R.id.textViewError);
        
        Bundle bundle = getIntent().getExtras();
        int timer_id = bundle.getInt("TIMER_ID");

        Log.d(TAG, "called with TIMER_ID: " + timer_id);

        timerRepository = new TimerRepository(getApplication());
        
        if (timer_id > -1) {
            timer = timerRepository.getTimer(timer_id);
            title = "Edit Timer";
            initValues(timer);
        }
        else {
            timer = null;
            title = "Neuer Timer";
        }

        toolbar.setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_add_upd_timer, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == 16908332 /* back button */) {
            goBackToMainScreen();
        }
        else {
            if (item.getItemId() == R.id.save_timer) {
                // validate values
                if (validValues()) {
                    boolean isNewTimer = false;

                    if (timer == null) {
                        timer = new Timer();
                        isNewTimer = true;
                    }

                    timer.setName(editTextName.getText().toString());
                    timer.setMinutes(Integer.parseInt(editTextMin.getText().toString()));
                    timer.setSeconds(Integer.parseInt(editTextSec.getText().toString()));
                    timer.setActive(false);
                    timer.setRepeating(checkBoxRepeat.isChecked());

                    Log.d(TAG, timer.getName() + ";" + timer.getSeconds());

                    // create new timer object and insert/update
                    if (isNewTimer) {
                        timerRepository.insert(timer);
                    }
                    else {
                        timerRepository.update(timer);
                    }

                    goBackToMainScreen();
                }
                else {
                    return false;
                }
            }
            else if (item.getItemId() == R.id.delete_timer) {
                Log.d(TAG, "delete timer with Id: " + timer.getId());

                timerRepository.delete(timer);
            }
        }

        goBackToMainScreen();

        return super.onOptionsItemSelected(item);
    }

    private boolean validValues() {
        int min = Integer.parseInt(editTextMin.getText().toString());
        int sec = Integer.parseInt(editTextSec.getText().toString());

        if (min > 59) {
            textViewError.setText("Minuten zu hoch");
            return false;
        }
        else if (sec > 59 || sec == 0) {
            textViewError.setText("Sekunden nicht gültig");
            return false;
        }

        return true;
    }

    private void goBackToMainScreen() {
        onBackPressed();
        finish();
    }

    private void initValues(Timer timer) {
        String min, sec;

        min = String.valueOf(timer.getMinutes());
        sec = String.valueOf(timer.getSecs());

        min = min.length() < 2 ? "0" + min : min;
        sec = sec.length() < 2 ? "0" + sec : sec;

        editTextMin.setText(min);
        editTextSec.setText(sec);
        editTextName.setText(timer.getName());
        checkBoxRepeat.setChecked(timer.getRepeating());
    }

    private int calculateSeconds() {
        int min = Integer.parseInt(editTextMin.getText().toString());
        int sec = Integer.parseInt(editTextSec.getText().toString());

        return min * 60 + sec;
    }
}
