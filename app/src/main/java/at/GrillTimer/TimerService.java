package at.GrillTimer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import at.GrillTimer.DB.TimerRepository;

public class TimerService extends Service {

    private final String TAG = "TimerService";
    public static boolean IS_RUNNING = false;
    private final String CHANNEL_ID = "515";
    private final int SERVICE_ID = 666;
    private String timer_name;
    private boolean timer_repeating;
    private int timer_seconds, timer_id;
    private CountDownTimer countDownTimer;
    private NotificationCompat.Builder timer_notification;
    private TimerRepository timerRepository = new TimerRepository(getApplication());

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

        timer_notification = showNotification();

        final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        new CountDownTimer(timer_seconds*1000, 1000) {

            public void onTick(long millisUntilFinished) {
                int secondsLeft = (int) (millisUntilFinished / 1000);
                Log.d(TAG, "seconds remaining: " + secondsLeft);

                Timer timer = timerRepository.getTimer(timer_id);

                if (!timer.getActive()) {
                    Log.d(TAG, "Timer got cancelled.. kill CountDownTimer");
                    this.cancel();
                    stopService(intent);
                }
            }

            public void onFinish() {
                Log.d(TAG, "done");

                timer_notification.setContentTitle(getResources().getString(R.string.time_finished));
                notificationManager.notify(SERVICE_ID, timer_notification.build());

                // check timer state ?

                this.start();
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

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Timer: " + timer_name)
                .setContentText("Hello World!")
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
}
