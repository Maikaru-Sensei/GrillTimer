package at.GrillTimer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import at.GrillTimer.DB.TimerRepository;

public class TimerService extends Service {

    private final String TAG = "TimerService";
    public static boolean IS_CANCELLED = false;
    private final String CHANNEL_ID = "515";
    private final int SERVICE_ID = 666;
    private String timer_name;
    private boolean timer_repeating;
    private int timer_seconds, timer_id;
    private CountDownTimer countDownTimer;
    private NotificationCompat.Builder timer_notification;
    private TimerRepository timerRepository = new TimerRepository(getApplication());
    private Timer timer;
    private Uri soundUri;
    private int changeCount = 0;

    @Override
    public void onDestroy() {
        Log.e(TAG, "destroy");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "starting service...");

        timer_name = intent.getStringExtra("TIMER_NAME");
        timer_repeating = intent.getBooleanExtra("REPEAT", false);
        timer_seconds = intent.getIntExtra("TIME", 1);
        timer_id = intent.getIntExtra("TIMER_ID", -1);

        timer = timerRepository.getTimer(timer_id);

        timer_notification = showNotification();

        final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"+ getApplicationContext().getPackageName() + "/" + R.raw.bitte_wenden);

        new CountDownTimer((timer.getMinutes() * 60 + timer.getSeconds()) * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                int secondsLeft = (int) (millisUntilFinished / 1000);
                Log.d(TAG, "seconds remaining: " + secondsLeft);

                if (IS_CANCELLED) {
                    Log.d(TAG, "Timer got cancelled.. kill CountDownTimer");
                    IS_CANCELLED = false; // reset cancel flag

                    this.cancel();
                    stopService(intent);
                }
            }

            public void onFinish() {
                Log.d(TAG, "done");


                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bitte_wenden);
                mediaPlayer.start();

                changeCount++;

                timer_notification.setContentTitle(getResources().getString(R.string.time_finished) + " | Count: " + changeCount);
                timer_notification.setContentText(getNextAlarm());

                notificationManager.notify(SERVICE_ID, timer_notification.build());

                if (timer.getRepeating()) {
                    this.start();
                }
                else {
                    Intent cancelIntent = new Intent(getApplicationContext(), TimerCancelBroadcast.class);
                    cancelIntent.putExtra("TIMER_ID", timer_id);
                    cancelIntent.setAction(String.valueOf(timer_id));
                    PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, cancelIntent, 0);

                    sendBroadcast(cancelIntent);

                    this.cancel();
                    stopService(intent);
                }
            }
        }.start();

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private NotificationCompat.Builder showNotification() {
        Intent onClickIntent = new Intent(this, MainActivity.class);
        onClickIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, onClickIntent, 0);


        Intent cancelIntent = new Intent(this, TimerCancelBroadcast.class);
        cancelIntent.putExtra("TIMER_ID", timer_id);
        cancelIntent.setAction(String.valueOf(timer_id));
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(this, 0, cancelIntent, 0);

        createNotificationChannel();

        NotificationCompat.Action cancelAction = new NotificationCompat.Action(R.drawable.ic_baseline_cancel_24, getString(R.string.timer_cancel), cancelPendingIntent);

        String title = "Timer: " + timer_name + " wurde aktiviert";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.bbq)
                .setContentTitle(title)
                .setContentText(getNextAlarm())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .addAction(cancelAction);

        startForeground(SERVICE_ID, builder.build());

        return builder;
    }

    private void createNotificationChannel() {
        String name = "TimerService";
        String description = "Description";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    private String getNextAlarm() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        String[] time = sdf.format(new Date()).split(":");
        int min, sec;
        String min_f, sec_f;

        min = Integer.parseInt(time[1]) + timer.getMinutes();
        sec = Integer.parseInt(time[2]) + timer.getSeconds();

        if (sec > 60) {
            min++;
            sec = sec - 60;
        }

        min_f = min < 10 ? "0" + min : String.valueOf(min);
        sec_f = sec < 10 ? "0" + sec : String.valueOf(sec);

        return "Next Alarm: " + time[0] + ":" + min_f + ":" + sec_f;
    }
}
