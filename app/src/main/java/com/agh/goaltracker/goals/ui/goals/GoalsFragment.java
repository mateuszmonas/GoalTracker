package com.agh.goaltracker.goals.ui.goals;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agh.goaltracker.GoalTrackerApplication;
import com.agh.goaltracker.R;
import com.agh.goaltracker.model.Goal;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GoalsFragment extends Fragment {

    @BindView(R.id.goals_recycler_view)
    RecyclerView goalsRecyclerView;
    private GoalsViewModel goalsViewModel;
    private GoalsAdapter goalsAdapter;
    private Unbinder unbinder;

    public static GoalsFragment newInstance() {
        return new GoalsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        goalsAdapter = new GoalsAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.goals_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        goalsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        goalsRecyclerView.setAdapter(goalsAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        goalsViewModel = new ViewModelProvider(this, new GoalsViewModelFactory(
                ((GoalTrackerApplication) getActivity().getApplication()).getGoalRepository()
        )).get(GoalsViewModel.class);
        goalsViewModel.getGoals().observe(getViewLifecycleOwner(), new Observer<List<Goal>>() {
            @Override
            public void onChanged(List<Goal> goals) {
                goalsAdapter.updateData(goals);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
