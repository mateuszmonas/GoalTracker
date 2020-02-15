package com.agh.goaltracker.ui.goaldetails;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.agh.goaltracker.GoalTrackerApplication;
import com.agh.goaltracker.R;
import com.agh.goaltracker.model.Goal;
import com.agh.goaltracker.receivers.GoalReminderBroadcastReceiver;
import com.agh.goaltracker.util.ViewModelFactory;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.content.Context.ALARM_SERVICE;

public class GoalDetailsFragment extends Fragment {
    private static final String TAG = "GoalDetailsFragment";
    private static final String EXTRA_GOAL_ID = "GOAL_ID";
    @BindView(R.id.goal_name)
    TextView goalName;
    @BindView(R.id.goal_progress_image)
    ImageView goalProgressImageView;
    @BindView(R.id.goal_progress_bar)
    ProgressBar goalProgressBar;
    @BindView(R.id.goal_due_date)
    TextView goalDueDate;
    private GoalDetailsViewModel goalDetailsViewModel;
    private Unbinder unbinder;

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
        goalDetailsViewModel.isDeleted.observe(getViewLifecycleOwner(), this::goalDeleted);
        goalDetailsViewModel.start(getArguments().getInt(EXTRA_GOAL_ID));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.goal_details_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected() called with: item = [" + item + "]");
        switch (item.getItemId()) {
            case R.id.edit_goal:
                // TODO: 09/02/20 implement edit goal details
                return true;
            case R.id.delete_goal:
                goalDetailsViewModel.deleteGoal();
                return true;
            case R.id.cancel_reminder:
                cancelReminder();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.record_progress_button)
    void recordPastProgress() {
        new GoalDetailsRecordPastProgressionDialogBuilder(getContext(), goalDetailsViewModel.goal.getValue())
            .setPositiveButtonListener(goalDetailsViewModel::recordPastProgress)
            .show();
    }

    // TODO: 11/02/20 add+1/start timer with notification
    @OnClick(R.id.contribute_now_button)
    void contributeNow() {
        goalDetailsViewModel.startContributing();
    }

    @OnClick(R.id.set_reminder_button)
    void startSettingReminder() {
        displayDatePicker();
    }

    private void displayDatePicker() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(getContext(), this::displayTimePicker,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void displayTimePicker(DatePicker view, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(getContext(), (view1, hour, minute) -> {
            setReminder(year, month, day, hour, minute);
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }

    private void setReminder(int year, int month, int day, int hour, int minute){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute, 0);
        if (calendar.getTime().before(Calendar.getInstance().getTime())) {
            Toast.makeText(getContext(), "can't select date from the past", Toast.LENGTH_LONG).show();
            return;
        }
        Toast.makeText(getContext(), "Reminder set for: " + SimpleDateFormat.getDateTimeInstance().format(calendar.getTime()), Toast.LENGTH_LONG).show();

        Goal goal = goalDetailsViewModel.goal.getValue();

        Intent intent = new Intent(getContext(), GoalReminderBroadcastReceiver.class);
        intent.putExtra(GoalReminderBroadcastReceiver.EXTRA_GOAL_ID, goal.getGoalId());
        intent.putExtra(GoalReminderBroadcastReceiver.EXTRA_GOAL_TITLE, goal.getTitle());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), goal.getGoalId(), intent, 0);

        AlarmManager alarmManager = (AlarmManager)getContext().getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private void cancelReminder() {
        Goal goal = goalDetailsViewModel.goal.getValue();
        Intent intent = new Intent(getContext(), GoalReminderBroadcastReceiver.class);
        intent.putExtra(GoalReminderBroadcastReceiver.EXTRA_GOAL_ID, goal.getGoalId());
        intent.putExtra(GoalReminderBroadcastReceiver.EXTRA_GOAL_TITLE, goal.getTitle());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), goal.getGoalId(), intent, 0);

        AlarmManager alarmManager = (AlarmManager)getContext().getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        Toast.makeText(getContext(), "Reminder canceled", Toast.LENGTH_LONG).show();
    }

    // TODO: 11/02/20 select current repeat status
    @OnClick(R.id.repeat_button)
    void openChangeRepeatDialog() {
        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Repeat")
                .setSingleChoiceItems(RepeatOption.stringValues(), 1, (dialog, which) -> {
                    changeRepeat(RepeatOption.values()[which]);
                    dialog.dismiss();
                })
                .show();
    }

    // TODO: 10/02/20 handle repeat change
    private void changeRepeat(RepeatOption repeatOption) {
        Log.d(TAG, "changeRepeat() called with: repeatOption = [" + repeatOption + "]");
        switch (repeatOption) {
            case NEVER:
                break;
            case DAILY:
                break;
            case WEEKLY:
                break;
            case MONTHLY:
                break;
        }
    }

    private void goalDeleted(boolean success) {
        if (success) {
            Toast.makeText(getContext(), "deleted!", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
    }

    private void showGoal(Goal goal) {
        if (goal == null) {
            return;
        }
        goalName.setText(goal.getTitle());
        if (goal.getDueDate() == null) {
            goalDueDate.setText("unlimited");
        } else {
            goalDueDate.setText(SimpleDateFormat.getDateInstance().format(goal.getDueDate()));
        }
        if (goal.getTotalGoal() > 0) {
            goalProgressBar.setMax(goal.getTotalGoal());
            goalProgressBar.setProgress(goal.getCurrentProgress());
        }else {
            goalProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    enum RepeatOption {
        NEVER("never"), DAILY("daily"), WEEKLY("weekly"), MONTHLY("monthly");

        private String text;

        RepeatOption(String text) {
            this.text = text;
        }

        // used so we can pass the values to MaterialAlertDialogBuilder
        public static String[] stringValues() {
            String[] result = new String[values().length];
            for (int i = 0; i < values().length; i++) {
                result[i] = values()[i].toString();
            }
            return result;
        }

        @Override
        public String toString() {
            return text;
        }
    }
}
