package com.agh.goaltracker.addgoal.ui.addgoal;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.agh.goaltracker.GoalTrackerApplication;
import com.agh.goaltracker.R;
import com.agh.goaltracker.model.Goal;
import com.agh.goaltracker.util.ViewModelFactory;

import java.util.Calendar;
import java.util.Date;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.Unbinder;


public class AddGoalFragment extends Fragment {

    @BindView(R.id.goal_name_txt)
    EditText goalName;
    private boolean countAsMinutes=true;
    private int goal = 0;
    Date chosenDate=null;

    private AddGoalViewModel addGoalViewModel;
    private Unbinder unbinder;

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
    public void addGoal() {
        if (TextUtils.isEmpty(goalName.getText())) {
            Toast.makeText(getContext(), "Goal name cannot be empty!", Toast.LENGTH_SHORT).show();
        } else {
            String name = goalName.getText().toString();
            addGoalViewModel.saveGoal(new Goal(name, chosenDate, countAsMinutes, goal));
            Toast.makeText(
                    getContext(),
                    "saved!",
                    Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
    }

    @OnClick(R.id.choose_date)
    public void setDueDate() { // TODO choose date from calendar
        Toast.makeText(getContext(), "display calendar", Toast.LENGTH_SHORT).show();
        chosenDate = Calendar.getInstance().getTime();
    }

    @OnClick(R.id.set_goal)
    public void setGoal() { // TODO set goal
        Toast.makeText(getContext(), countAsMinutes ? "display hour and minute chooser popup": "display popup to enter nr of events", Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.radio_as_events, R.id.radio_as_min})
    public void onRadioButtonClicked(RadioButton radioButton) {
        boolean checked = radioButton.isChecked();
        switch (radioButton.getId()) {
            case R.id.radio_as_events:
                if (checked) countAsMinutes = false;
                break;
            case R.id.radio_as_min:
                if (checked) countAsMinutes = true;
                break;
        }
    }

}
