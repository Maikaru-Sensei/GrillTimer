package at.GrillTimer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class TimerService extends Service {

    private final String TAG = "TimerService";
    public static boolean IS_RUNNING = false;
    private final String CHANNEL_ID = "515";
    private final int SERVICE_ID = 666;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "starting service...");
        showNotification();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void showNotification() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction("TEST");
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        createNotificationChannel();

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("TutorialsFace Music Player")
                .setTicker("TutorialsFace Music Player")
                .setContentText("My song")
                .setContentIntent(pendingIntent)
                .setOngoing(true).build();

        startForeground(SERVICE_ID,notification);
    }

    private void createNotificationChannel() {
        String name = "TimerService";
        String description = "Description";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}
