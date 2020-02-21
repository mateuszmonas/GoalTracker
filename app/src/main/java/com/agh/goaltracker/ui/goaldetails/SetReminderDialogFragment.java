package com.agh.goaltracker.ui.goaldetails;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.agh.goaltracker.R;
import com.agh.goaltracker.model.Goal;
import com.agh.goaltracker.receivers.GoalReminderBroadcastReceiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.content.Context.ALARM_SERVICE;

public class SetReminderDialogFragment extends DialogFragment{

    private static final String EXTRA_GOAL_ID = "GOAL_ID";
    private static final String EXTRA_GOAL_TITLE = "GOAL_TITLE";
    Unbinder unbinder;

    Calendar chosenDateTimeCalendar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.choose_date)
    TextView chooseDateEditText;
    @BindView(R.id.choose_time)
    TextView chooseTimeEditText;
    @BindView(R.id.chosen_date)
    TextView chosenDateTextView;
    @BindView(R.id.chosen_time)
    TextView chosenTimeTextView;

    @BindView(R.id.repeat_checkbox)
    CheckBox repeatCheckbox;
    @BindView(R.id.repeat_layout)
    ViewGroup repeatLayout;
    @BindView(R.id.repeat_interval_edit_text)
    EditText repeatIntervalEditText;
    @BindView(R.id.save_reminder)
    Button saveReminderButton;

    @BindView(R.id.selected_repeat_interval)
    AutoCompleteTextView selectedRepeatInterval;
    int goalId;
    int goalTitle;

    public static SetReminderDialogFragment newInstance(int goalId) {
        final SetReminderDialogFragment fragment = new SetReminderDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_GOAL_ID, goalId);
        bundle.putInt(EXTRA_GOAL_ID, goalId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.set_reminder_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbar.setNavigationOnClickListener(v -> dismiss());
        toolbar.setTitle("Create reminder");


        selectedRepeatInterval.setText(RepeatInterval.getStringValues()[0], false);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getContext(),
                        R.layout.set_reminder_interval_popup_item,
                        RepeatInterval.getStringValues());
        selectedRepeatInterval.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @OnCheckedChanged(R.id.repeat_checkbox)
    void onRepeatCheckboxCheck(CompoundButton buttonView, boolean isChecked) {
        repeatLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        repeatIntervalEditText.setText("");
    }

    @OnClick(R.id.save_reminder)
    void onSaveReminderClick() {
        String toastText;
        if (chosenDateTextView.getText().toString().equals("")) {
            toastText = "reminder date not selected";
        } else if (chosenTimeTextView.getText().toString().equals("")) {
            toastText = "reminder time not selected";
        } else if(repeatCheckbox.isChecked() && repeatIntervalEditText.getText().toString().equals("")) {
            toastText = "reminder interval not selected";
        }else if(chosenDateTimeCalendar.getTime().before(Calendar.getInstance().getTime())){
            toastText = "selected date from the past";
        }else {
            Intent intent = new Intent(getContext(), GoalReminderBroadcastReceiver.class);
            intent.putExtra(GoalReminderBroadcastReceiver.EXTRA_GOAL_ID, goalId);
            intent.putExtra(GoalReminderBroadcastReceiver.EXTRA_GOAL_TITLE, goalTitle);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), goalId, intent, 0);

            AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, chosenDateTimeCalendar.getTimeInMillis(), pendingIntent);
            toastText = "Reminder set for: " + SimpleDateFormat.getDateTimeInstance().format(chosenDateTimeCalendar.getTime());
            dismiss();
        }
        Toast.makeText(getContext(), toastText, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    enum RepeatInterval {
        HOURS("hours"), DAYS("days");

        private String text;

        RepeatInterval(String text) {
            this.text = text;
        }

        public static String[] getStringValues() {
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
