package at.GrillTimer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class TimerCancelBroadcast extends BroadcastReceiver {

    private final String TAG = "TimerCancelBroadcast";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: Cancel timer");
        // back to MainActivity and stop timerService

        if (intent == null) {
            return;
        }

        int timer_id = Integer.parseInt(intent.getAction());

        Intent cancelIntent = new Intent("TIMER_CANCEL");
        cancelIntent.putExtra("TIMER_ID", timer_id);

        context.sendBroadcast(cancelIntent);
    }
}
