package com.agh.goaltracker.ui.goaldetails;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.agh.goaltracker.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.Unbinder;

public class GoalDetailsRepeatDialogFragment extends DialogFragment {
    public static String TAG = "GoalDetailsRepeatDialogFragment";

    private Unbinder unbinder;
    static final int RESULT_CODE_NEVER = 0;
    static final int RESULT_CODE_DAILY = 1;
    static final int RESULT_CODE_WEEKLY= 2;
    static final int RESULT_CODE_MONTHLY = 3;

    // TODO: 10/02/20 check default radio button
    public static GoalDetailsRepeatDialogFragment newInstance() {
        GoalDetailsRepeatDialogFragment fragment = new GoalDetailsRepeatDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new MaterialAlertDialogBuilder(getContext()).setView(R.layout.goal_details_repeat_dialog_fragment).create();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.goal_details_repeat_dialog_fragment, container);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @OnCheckedChanged({R.id.repeat_never, R.id.repeat_daily, R.id.repeat_weekly, R.id.repeat_monthly})
    void onRepeatSelected(RadioButton radioButton) {
        int resultCode;
        switch (radioButton.getId()){
            case R.id.repeat_never:
                resultCode = RESULT_CODE_NEVER;
                break;
            case R.id.repeat_daily:
                resultCode = RESULT_CODE_DAILY;
                break;
            case R.id.repeat_weekly:
                resultCode = RESULT_CODE_WEEKLY;
                break;
            case R.id.repeat_monthly:
                resultCode = RESULT_CODE_MONTHLY;
                break;
            default:
                resultCode = -1;
                break;
        }
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, null);
        dismiss();
    }

}
