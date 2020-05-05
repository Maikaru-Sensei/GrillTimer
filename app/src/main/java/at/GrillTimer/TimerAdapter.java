package at.GrillTimer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TimerAdapter extends RecyclerView.Adapter<TimerAdapter.TimerHolder> {
    private List<Timer> listOfTimers = new ArrayList<>();
    private Context context;

    @NonNull
    @Override
    public TimerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_timer, parent, false);

        context = parent.getContext();
        return new TimerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TimerHolder holder, int position) {
        final Timer timer = listOfTimers.get(position);
        holder.textViewTimerName.setText(timer.getName());
        holder.textViewTimerTime.setText(timer.getFormatTime());
        holder.switchActive.setChecked(timer.getActive());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddOrUpdTimerActivity.class);
                intent.putExtra("TIMER_ID", timer.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOfTimers.size();
    }

    public void setTimers(List<Timer> timers) {
        this.listOfTimers = timers;
        notifyDataSetChanged();
    }



    class TimerHolder extends RecyclerView.ViewHolder {
        private TextView textViewTimerName;
        private TextView textViewTimerTime;
        private Switch switchActive;

        public TimerHolder(@NonNull View itemView) {
            super(itemView);
            textViewTimerName = itemView.findViewById(R.id.text_view_timer_name);
            textViewTimerTime = itemView.findViewById(R.id.text_view_timer_time);
            switchActive = itemView.findViewById(R.id.switch_timer_active);
        }
    }
}
