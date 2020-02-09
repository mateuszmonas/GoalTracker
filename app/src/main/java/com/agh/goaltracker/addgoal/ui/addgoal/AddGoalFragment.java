package com.agh.goaltracker.addgoal.ui.addgoal;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.agh.goaltracker.GoalTrackerApplication;
import com.agh.goaltracker.R;
import com.agh.goaltracker.model.Goal;
import com.agh.goaltracker.util.ViewModelFactory;

import java.util.Calendar;
import java.util.Date;


public class AddGoalFragment extends Fragment {

    private AddGoalViewModel addGoalViewModel;
    private Unbinder unbinder;

    @BindView(R.id.goal_name_txt)
    EditText goalName;

    public static AddGoalFragment newInstance() {
        return new AddGoalFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_goal_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addGoalViewModel = new ViewModelProvider(this, new ViewModelFactory(
                ((GoalTrackerApplication) getActivity().getApplication()).getGoalRepository()
        )).get(AddGoalViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.create_goal_btn)
    public void addGoal(){
        if (TextUtils.isEmpty(goalName.getText())) {
            Toast.makeText(getContext(), "Goal name cannot be empty!", Toast.LENGTH_SHORT).show();
        }
        else{
            String name = goalName.getText().toString();
            Date currentTime = Calendar.getInstance().getTime();
            addGoalViewModel.saveGoal(new Goal(name, currentTime));
            Toast.makeText(
                    getContext(),
                    "saved!",
                    Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
    }

}
