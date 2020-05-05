package at.GrillTimer;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class TimerListFragment extends Fragment {

    private TimerViewModel timerViewModel;
    private FloatingActionButton fabAddTimer;

    public static TimerListFragment newInstance() {
        return new TimerListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_timers, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.timer_list_recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setHasFixedSize(true);

        final TimerAdapter timerAdapter = new TimerAdapter();
        recyclerView.setAdapter(timerAdapter);

        timerViewModel = new ViewModelProvider(this).get(TimerViewModel.class);
        timerViewModel.getAllTimers().observe(getViewLifecycleOwner(), new Observer<List<Timer>>() {
            @Override
            public void onChanged(List<Timer> timers) {
                timerAdapter.setTimers(timers);
            }
        });

        fabAddTimer = rootView.findViewById(R.id.fab_add_timer);
        fabAddTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddOrUpdTimerActivity.class);
                intent.putExtra("TIMER_ID", -1); // pass -1 because a new timer will be created
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
