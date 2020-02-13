package com.agh.goaltracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.agh.goaltracker.ui.goals.GoalsFragment;

import androidx.appcompat.app.AppCompatActivity;

public class GoalsActivity extends AppCompatActivity {

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, GoalsActivity.class);
        return intent;
    }

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
