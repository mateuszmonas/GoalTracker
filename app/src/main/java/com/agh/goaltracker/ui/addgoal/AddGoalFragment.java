package com.agh.goaltracker.ui.addgoal;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.agh.goaltracker.GoalTrackerApplication;
import com.agh.goaltracker.R;
import com.agh.goaltracker.model.Goal;
import com.agh.goaltracker.util.ViewModelFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class AddGoalFragment extends Fragment {

    @BindView(R.id.goal_name_txt)
    EditText goalName;
    @BindView(R.id.chosen_date)
    TextView chosenDateTV;
    @BindView(R.id.event_goal_txt)
    EditText eventGoal;
    @BindView(R.id.timeChoserLayout)
    LinearLayout minutesGoal;
    @BindView(R.id.hour_txt)
    EditText hourET;
    @BindView(R.id.min_txt)
    EditText minET;
    Date chosenDate = null;
    private boolean countAsTime = true;
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
        addGoalViewModel.finishActivity.observe(getViewLifecycleOwner(), this::finishActivity);
        addGoalViewModel.saveTaskErrorMessage.observe(getViewLifecycleOwner(), this::showErrorMessage);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.unset_date)
    public void unsetDate() {
        chosenDate = null;
        chosenDateTV.setText(getString(R.string.unset));
    }

    @OnClick(R.id.create_goal_btn)
    public void addGoal() {
        String name = goalName.getText().toString();
        Goal goal = null;
        if (countAsTime) {
            int hours = hourET.getText().length() == 0 ? 0 : Integer.valueOf(hourET.getText().toString());
            int minutes = minET.getText().length() == 0 ? 0 : Integer.valueOf(minET.getText().toString());
            goal = new Goal(name, chosenDate, hours, minutes);
        } else if (!"".equals(eventGoal.getText().toString())) {
            int goalAmount = Integer.parseInt(eventGoal.getText().toString());
            goal = new Goal(name, chosenDate, goalAmount);
        }
        addGoalViewModel.saveGoal(goal);

    }

    private void showErrorMessage(AddGoalViewModel.SaveGoalError error) {
        String message = "";
        switch (error) {
            case EMPTY_TITLE:
                message = "Goal name cannot be empty!";
                break;
        }
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }


    private void finishActivity(boolean success) {
        if (success) {
            Toast.makeText(
                    getContext(),
                    "saved!",
                    Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
    }

    @OnClick(R.id.choose_date)
    public void setDueDate() {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        DatePickerDialog dialog = new DatePickerDialog(getContext(), this::onDateSet,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();

    }

    @OnClick({R.id.radio_as_events, R.id.radio_as_min})
    public void onRadioButtonClicked(RadioButton radioButton) {
        boolean checked = radioButton.isChecked();
        switch (radioButton.getId()) {
            case R.id.radio_as_events:
                if (checked) {
                    countAsTime = false;
                    minutesGoal.setVisibility(View.GONE);
                    eventGoal.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.radio_as_min:
                if (checked) {
                    countAsTime = true;
                    minutesGoal.setVisibility(View.VISIBLE);
                    eventGoal.setVisibility(View.GONE);
                }
                break;
        }
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        try {
            chosenDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse("" + day + "/" + (month + 1) + "/" + year);

            DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.ENGLISH);
            chosenDateTV.setText(dateFormat.format(chosenDate));
            if (chosenDate.compareTo(new Date()) < 0)
                Toast.makeText(getContext(), "you chose a date from the past", Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
