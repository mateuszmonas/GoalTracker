package com.agh.goaltracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.agh.goaltracker.ui.goaldetails.GoalDetailsFragment;

import androidx.appcompat.app.AppCompatActivity;

public class GoalDetailsActivity extends AppCompatActivity {

    private static final String EXTRA_GOAL_ID = "GOAL_ID";

    public static Intent createIntent(Context context, int goalId) {
        Intent intent = new Intent(context, GoalDetailsActivity.class);
        intent.putExtra(EXTRA_GOAL_ID, goalId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goal_details_activity);
        if (savedInstanceState == null) {
            int goalId = getIntent().getIntExtra(EXTRA_GOAL_ID, -1);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, GoalDetailsFragment.newInstance(goalId))
                    .commitNow();
        }
    }
}
