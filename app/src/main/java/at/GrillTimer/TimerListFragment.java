package at.GrillTimer;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class TimerListFragment extends Fragment {

    private TimerViewModel timerViewModel;

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

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
