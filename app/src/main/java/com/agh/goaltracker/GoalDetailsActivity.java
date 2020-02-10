package com.agh.goaltracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.agh.goaltracker.ui.goaldetails.GoalDetailsFragment;

public class GoalDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goal_details_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, GoalDetailsFragment.newInstance())
                    .commitNow();
        }
    }
}
