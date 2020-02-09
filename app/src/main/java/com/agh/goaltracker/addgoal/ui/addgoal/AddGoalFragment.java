package com.agh.goaltracker.addgoal.ui.addgoal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agh.goaltracker.GoalTrackerApplication;
import com.agh.goaltracker.R;
import com.agh.goaltracker.util.ViewModelFactory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


public class AddGoalFragment extends Fragment {

    private AddGoalViewModel addGoalViewModel;

    public static AddGoalFragment newInstance() {
        return new AddGoalFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_goal_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addGoalViewModel = new ViewModelProvider(this, new ViewModelFactory(
                ((GoalTrackerApplication) getActivity().getApplication()).getGoalRepository()
        )).get(AddGoalViewModel.class);
        // TODO: Use the ViewModel
    }

}
