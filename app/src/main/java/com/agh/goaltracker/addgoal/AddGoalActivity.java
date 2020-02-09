package com.agh.goaltracker.addgoal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.agh.goaltracker.R;
import com.agh.goaltracker.addgoal.ui.addgoal.AddGoalFragment;

public class AddGoalActivity extends AppCompatActivity {

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, AddGoalActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_goal_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, AddGoalFragment.newInstance())
                    .commitNow();
        }
    }
}
