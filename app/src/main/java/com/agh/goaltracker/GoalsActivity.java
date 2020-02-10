package com.agh.goaltracker;

import android.os.Bundle;

import com.agh.goaltracker.ui.goals.GoalsFragment;

import androidx.appcompat.app.AppCompatActivity;

public class GoalsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goals_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, GoalsFragment.newInstance())
                    .commitNow();
        }
    }
}
