package com.agh.goaltracker.ui.goaldetails;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agh.goaltracker.R;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SetReminderDialogFragment extends DialogFragment{

    private static final String EXTRA_GOAL_ID = "GOAL_ID";
    Unbinder unbinder;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
