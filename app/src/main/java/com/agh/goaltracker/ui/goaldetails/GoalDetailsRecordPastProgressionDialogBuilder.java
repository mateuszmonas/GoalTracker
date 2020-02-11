package com.agh.goaltracker.ui.goaldetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.agh.goaltracker.R;
import com.agh.goaltracker.model.Goal;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoalDetailsRecordPastProgressionDialogBuilder {

    private MaterialAlertDialogBuilder builder;
    private Goal goal;

    @BindView(R.id.number_of_events)
    EditText numberOfEvents;
    @BindView(R.id.time_chooser_layout)
    LinearLayout timeChooserLayout;
    @BindView(R.id.hours)
    EditText hoursEditText;
    @BindView(R.id.minutes)
    EditText minutesEditText;

    @BindView(R.id.positive_button)
    Button positiveButton;

    @BindView(R.id.negative_button)
    Button negativeButton;

    GoalDetailsRecordPastProgressionDialogBuilder(Context context, Goal goal) {
        this.goal = goal;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.record_past_progress_dialog, null);
        builder = new MaterialAlertDialogBuilder(context)
                .setTitle("")
                .setView(view);
        ButterKnife.bind(this, view);
        if (goal.isProgressAsMinutes()) {
            numberOfEvents.setVisibility(View.GONE);
            timeChooserLayout.setVisibility(View.VISIBLE);
        }else {
            timeChooserLayout.setVisibility(View.GONE);
            numberOfEvents.setVisibility(View.VISIBLE);
        }
    }

    GoalDetailsRecordPastProgressionDialogBuilder setPositiveButtonListener(OnClickListener onClickListener) {
        positiveButton.setOnClickListener(v -> onClickListener.onClick(getResult()));
        return this;
    }

    GoalDetailsRecordPastProgressionDialogBuilder setNegativeButtonListener(OnClickListener onClickListener) {
        negativeButton.setOnClickListener(v -> onClickListener.onClick(-1));
        return this;
    }

    private int getResult() {
        int result = 0;
        if (goal.isProgressAsMinutes()) {
            String hours = hoursEditText.getText().toString();
            String minutes = minutesEditText.getText().toString();
            if (!hours.equals(""))
                result += 60 * Integer.parseInt(hours);
            if (!minutes.equals(""))
                result += Integer.parseInt(minutes);
        } else if(!numberOfEvents.getText().toString().equals("")){
            result = Integer.parseInt(numberOfEvents.getText().toString());
        }
        return result;
    }

    void show() {
        builder.show();
    }

    interface OnClickListener {
        void onClick(int result);
    }

}
