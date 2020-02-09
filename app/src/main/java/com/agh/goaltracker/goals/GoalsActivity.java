package com.agh.goaltracker.goals;

import android.os.Bundle;

import com.agh.goaltracker.R;
import com.agh.goaltracker.goals.ui.goals.GoalsFragment;

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
