package com.agh.goaltracker.ui.goaldetails;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.agh.goaltracker.R;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SetReminderDialogFragment extends DialogFragment{

    private static final String EXTRA_GOAL_ID = "GOAL_ID";
    Unbinder unbinder;

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

    public static SetReminderDialogFragment newInstance(int goalId) {
        final SetReminderDialogFragment fragment = new SetReminderDialogFragment();
        Bundle bundle = new Bundle();
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

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getContext(),
                        R.layout.set_reminder_interval_popup_item,
                        RepeatInterval.stringValues());
        selectedRepeatInterval.setAdapter(adapter);
        selectedRepeatInterval.setListSelection(0);

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
