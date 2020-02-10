package com.agh.goaltracker.ui.goaldetails;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.agh.goaltracker.GoalTrackerApplication;
import com.agh.goaltracker.R;
import com.agh.goaltracker.model.Goal;
import com.agh.goaltracker.ui.goals.GoalsViewModel;
import com.agh.goaltracker.util.ViewModelFactory;

public class GoalDetailsFragment extends Fragment {
    private static final String EXTRA_GOAL_ID = "GOAL_ID";

    private GoalDetailsViewModel goalDetailsViewModel;
    private Unbinder unbinder;

    @BindView(R.id.goal_name)
    TextView goalName;

    public static GoalDetailsFragment newInstance(int goalId) {
        GoalDetailsFragment fragment = new GoalDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_GOAL_ID, goalId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.goal_details_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        goalDetailsViewModel = new ViewModelProvider(this, new ViewModelFactory(
                ((GoalTrackerApplication) getActivity().getApplication()).getGoalRepository()
        )).get(GoalDetailsViewModel.class);
        goalDetailsViewModel.goal.observe(getViewLifecycleOwner(), this::showGoal);
        goalDetailsViewModel.start(getArguments().getInt(EXTRA_GOAL_ID));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.goal_details_menu, menu);
    }

    void showGoal(Goal goal) {
        goalName.setText(goal.getTitle());
    }

}
