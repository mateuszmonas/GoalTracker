package com.agh.goaltracker.ui.goaldetails;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.agh.goaltracker.R;
import com.agh.goaltracker.model.Goal;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

public class RecordPastProgressionDialogBuilder {

    @BindView(R.id.number_of_events)
    EditText numberOfEvents;
    @BindView(R.id.time_chooser_layout)
    ViewGroup timeChooserLayout;
    @BindView(R.id.hours)
    EditText hoursEditText;
    @BindView(R.id.minutes)
    EditText minutesEditText;
    @BindView(R.id.positive_button)
    Button positiveButton;
    @BindView(R.id.negative_button)
    Button negativeButton;
    private MaterialAlertDialogBuilder builder;
    private Dialog dialog;
    private Goal goal;

    RecordPastProgressionDialogBuilder(Context context, Goal goal) {
        this.goal = goal;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.record_past_progress_dialog, null);
        builder = new MaterialAlertDialogBuilder(context)
                .setTitle("Record past Progression")
                .setView(view);
        ButterKnife.bind(this, view);
        if (goal.isProgressAsTime()) {
            numberOfEvents.setVisibility(View.GONE);
            timeChooserLayout.setVisibility(View.VISIBLE);
        } else {
            timeChooserLayout.setVisibility(View.GONE);
            numberOfEvents.setVisibility(View.VISIBLE);
        }
        positiveButton.setOnClickListener(v -> dialog.dismiss());
        negativeButton.setOnClickListener(v -> dialog.dismiss());
        positiveButton.setEnabled(false);
    }

    RecordPastProgressionDialogBuilder setPositiveButtonListener(OnClickListener onClickListener) {
        positiveButton.setOnClickListener(v -> {
            onClickListener.onClick(getResult());
            dialog.dismiss();
        });
        return this;
    }

    private int getResult() {
        int result = 0;
        if (goal.isProgressAsTime()) {
            String hours = hoursEditText.getText().toString();
            String minutes = minutesEditText.getText().toString();
            if (!hours.equals(""))
                result += 3600 * Integer.parseInt(hours);
            if (!minutes.equals(""))
                result += 60* Integer.parseInt(minutes);
        } else if (!numberOfEvents.getText().toString().equals("")) {
            result = Integer.parseInt(numberOfEvents.getText().toString());
        }
        return result;
    }

    @OnTextChanged(R.id.number_of_events)
    void onNumberOfEventsChange(CharSequence text) {
        positiveButton.setEnabled(text.length() != 0);
    }

    @OnTextChanged({R.id.hours, R.id.minutes})
    void onHourOrMinutesChange() {
        positiveButton.setEnabled(hoursEditText.getText().length() != 0 && minutesEditText.getText().length() != 0);
    }

    void show() {
        dialog = builder.create();
        dialog.show();
    }

    interface OnClickListener {
        void onClick(int result);
    }

}
