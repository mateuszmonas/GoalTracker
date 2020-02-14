package com.agh.goaltracker.ui.goaldetails;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.agh.goaltracker.GoalTrackerApplication;
import com.agh.goaltracker.R;
import com.agh.goaltracker.model.Goal;
import com.agh.goaltracker.util.ViewModelFactory;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

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
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.record_progress_button)
    void recordPastProgress() {
        new GoalDetailsRecordPastProgressionDialogBuilder(getContext(), goalDetailsViewModel.goal.getValue())
            .setPositiveButtonListener(result -> Toast.makeText(getContext(), Integer.toString(result), Toast.LENGTH_SHORT).show())
            .show();
    }

    // TODO: 11/02/20 add+1/start timer with notification
    @OnClick(R.id.contribute_now_button)
    void contributeNow() {

    }
    // TODO: 11/02/20 open calendar and time picker and create notification
    @OnClick(R.id.set_notification_button)
    void setNotification() {

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
    void changeRepeat(RepeatOption repeatOption) {
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

    void showGoal(Goal goal) {
        if (goal == null) {
            return;
        }
        goalName.setText(goal.getTitle());
        DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy", Locale.ENGLISH);
        if (goal.getDueDate() == null) {
            goalDueDate.setText("unlimited");
        } else {
            goalDueDate.setText(dateFormat.format(goal.getDueDate()));
        }
        goalProgressBar.setProgress(goal.getCurrentProgress());
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
