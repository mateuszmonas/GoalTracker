package com.agh.goaltracker.ui.goaldetails;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
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
import com.agh.goaltracker.receivers.GoalReminderBroadcastReceiver;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.content.Context.ALARM_SERVICE;

public class SetReminderDialogFragment extends DialogFragment {

    private static final String EXTRA_GOAL_ID = "GOAL_ID";
    private static final String EXTRA_GOAL_TITLE = "GOAL_TITLE";
    Unbinder unbinder;

    Calendar chosenDateTimeCalendar = Calendar.getInstance();

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
    @BindView(R.id.selected_repeat_interval)
    AutoCompleteTextView selectedRepeatInterval;
    int goalId;
    String goalTitle;

    public static SetReminderDialogFragment newInstance(int goalId) {
        final SetReminderDialogFragment fragment = new SetReminderDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_GOAL_ID, goalId);
        bundle.putInt(EXTRA_GOAL_ID, goalId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // to make dialog fullscreen
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.set_reminder_dialog_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbar.setNavigationOnClickListener(v -> dismiss());
        toolbar.setTitle("Create reminder");
        toolbar.inflateMenu(R.menu.set_reminder_toolbar_menu);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_save) {
                saveReminder();
                return true;
            }
            return false;
        });

        goalId = getArguments().getInt(EXTRA_GOAL_ID);
        goalTitle = getArguments().getString(EXTRA_GOAL_TITLE);

        selectedRepeatInterval.setText(RepeatInterval.getStringValues()[0], false);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getContext(),
                        R.layout.set_reminder_interval_popup_item,
                        RepeatInterval.getStringValues());
        selectedRepeatInterval.setAdapter(adapter);
        chosenDateTextView.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(chosenDateTimeCalendar.getTime()));
        chosenTimeTextView.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(chosenDateTimeCalendar.getTime()));

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

    @OnClick({R.id.choose_date, R.id.chosen_date})
    void onChoseDateClick() {
        new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            chosenDateTimeCalendar.set(Calendar.YEAR, year);
            chosenDateTimeCalendar.set(Calendar.MONTH, month);
            chosenDateTimeCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            chosenDateTextView.setText(SimpleDateFormat.getDateInstance().format(chosenDateTimeCalendar.getTime()));
        },
                chosenDateTimeCalendar.get(Calendar.YEAR), chosenDateTimeCalendar.get(Calendar.MONTH),
                chosenDateTimeCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @OnClick({R.id.choose_time, R.id.chosen_time})
    void onChoseTimeClick() {
        new TimePickerDialog(getContext(), (view, hour, minute) -> {
            chosenDateTimeCalendar.set(Calendar.HOUR_OF_DAY, hour);
            chosenDateTimeCalendar.set(Calendar.MINUTE, minute);
            chosenDateTimeCalendar.set(Calendar.SECOND, 0);
            chosenTimeTextView.setText(SimpleDateFormat.getTimeInstance(DateFormat.SHORT).format(chosenDateTimeCalendar.getTime()));
        }, chosenDateTimeCalendar.get(Calendar.HOUR_OF_DAY), chosenDateTimeCalendar.get(Calendar.MINUTE), true).show();

    }

    long getRepeatIntervalInMillis(int interval, RepeatInterval repeatInterval) {
        long intervalInMillis = 0;
        switch (repeatInterval) {
            case HOURS:
                intervalInMillis = TimeUnit.HOURS.toMillis(interval);
                break;
            case DAYS:
                intervalInMillis = TimeUnit.DAYS.toMillis(interval);
                break;
        }
        return intervalInMillis;
    }

    void saveReminder() {
        String toastText;
        if (chosenDateTextView.getText().toString().equals("")) {
            toastText = "reminder date not selected";
        } else if (chosenTimeTextView.getText().toString().equals("")) {
            toastText = "reminder time not selected";
        } else if (repeatCheckbox.isChecked() && repeatIntervalEditText.getText().toString().equals("")) {
            toastText = "reminder interval not selected";
        } else if (chosenDateTimeCalendar.getTime().before(Calendar.getInstance().getTime())) {
            toastText = "selected date from the past";
        } else {
            Intent intent = new Intent(getContext(), GoalReminderBroadcastReceiver.class);
            intent.putExtra(GoalReminderBroadcastReceiver.EXTRA_GOAL_ID, goalId);
            intent.putExtra(GoalReminderBroadcastReceiver.EXTRA_GOAL_TITLE, goalTitle);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), goalId, intent, 0);

            AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);

            if (repeatCheckbox.isChecked()) {
                RepeatInterval repeatInterval = RepeatInterval.getRepeatInterval(selectedRepeatInterval.getText().toString());
                int interval = Integer.valueOf(repeatIntervalEditText.getText().toString());
                long intervalInMillis = getRepeatIntervalInMillis(interval, repeatInterval);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, chosenDateTimeCalendar.getTimeInMillis(), intervalInMillis, pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, chosenDateTimeCalendar.getTimeInMillis(), pendingIntent);
            }

            toastText = "Reminder set for: " + SimpleDateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(chosenDateTimeCalendar.getTime());
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

        static RepeatInterval getRepeatInterval(String repeatInterval){
            int index = Arrays.asList(RepeatInterval.getStringValues()).indexOf(repeatInterval);
            if(index == -1){
                return null;
            }
            return RepeatInterval.values()[Arrays.asList(RepeatInterval.getStringValues()).indexOf(repeatInterval)];
        }

        @Override
        public String toString() {
            return text;
        }
    }

}
