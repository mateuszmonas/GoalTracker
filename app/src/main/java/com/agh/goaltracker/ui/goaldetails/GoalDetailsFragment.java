package com.agh.goaltracker.ui.goaldetails;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.agh.goaltracker.GoalTrackerApplication;
import com.agh.goaltracker.R;
import com.agh.goaltracker.model.Goal;
import com.agh.goaltracker.receivers.GoalReminderBroadcastReceiver;
import com.agh.goaltracker.services.GoalContributionService;
import com.agh.goaltracker.util.ViewModelFactory;

import java.text.SimpleDateFormat;

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
    @BindView(R.id.stop_contributing_button)
    Button stopContributingButton;
    @BindView(R.id.start_contributing_button)
    Button startContributingButton;
    @BindView(R.id.goal_progress_text)
    TextView goalProgressText;
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
        goalDetailsViewModel.isGoalContributing.observe(getViewLifecycleOwner(), this::changeContributionButton);
        goalDetailsViewModel.startGoalContributing.observe(getViewLifecycleOwner(), this::startContributionService);
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
        new RecordPastProgressionDialogBuilder(getContext(), goalDetailsViewModel.goal.getValue())
                .setPositiveButtonListener(goalDetailsViewModel::recordPastProgress)
                .show();
    }

    // TODO: 11/02/20 add+1/start timer with notification
    @OnClick(R.id.start_contributing_button)
    void onStartContributingButtonClick() {
        goalDetailsViewModel.startContributing();
    }

    @OnClick(R.id.stop_contributing_button)
    void onStopContributingButtonClick() {
        goalDetailsViewModel.stopContributing();
    }

    private void cancelReminder() {
        Goal goal = goalDetailsViewModel.goal.getValue();
        Intent intent = new Intent(getContext(), GoalReminderBroadcastReceiver.class);
        intent.putExtra(GoalReminderBroadcastReceiver.EXTRA_GOAL_ID, goal.getGoalId());
        intent.putExtra(GoalReminderBroadcastReceiver.EXTRA_GOAL_TITLE, goal.getTitle());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), goal.getGoalId(), intent, 0);

        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        Toast.makeText(getContext(), "Reminder canceled", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.set_reminder_button)
    void openChangeRepeatDialog() {
        SetReminderDialogFragment.newInstance(goalDetailsViewModel.goal.getValue().getGoalId())
                .show(getActivity().getSupportFragmentManager(), "");
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
            displayPlantProgress((double)goal.getCurrentProgress()/goal.getTotalGoal());
        } else {
            goalProgressBar.setVisibility(View.INVISIBLE);
        }
        goalProgressText.setText(goal.progressToString());
    }

    void displayPlantProgress(double completedRatio){
        if (completedRatio<0.2){
            goalProgressImageView.setImageResource(R.drawable.plant1);
        }else if(completedRatio <0.5){
            goalProgressImageView.setImageResource(R.drawable.plant2);
        }else if(completedRatio <0.9){
            goalProgressImageView.setImageResource(R.drawable.plant3);
        }else {
            goalProgressImageView.setImageResource(R.drawable.plant4);
        }
    }

    private void changeContributionButton(boolean contributing) {
        if (contributing) {
            startContributingButton.setVisibility(View.GONE);
            stopContributingButton.setVisibility(View.VISIBLE);
        } else {
            startContributingButton.setVisibility(View.VISIBLE);
            stopContributingButton.setVisibility(View.GONE);
        }
    }

    void startContributionService(boolean contributing) {
        Intent intent;
        if (contributing) {
            intent = GoalContributionService.createStartContributingIntent(getContext(), goalDetailsViewModel.goal.getValue().getGoalId());
        } else {
            intent = GoalContributionService.createStopContributingIntent(getContext(), goalDetailsViewModel.goal.getValue().getGoalId());
        }
        getContext().startService(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
