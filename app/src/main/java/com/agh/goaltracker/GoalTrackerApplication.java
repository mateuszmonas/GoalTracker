package com.agh.goaltracker;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.agh.goaltracker.model.Goal;
import com.agh.goaltracker.model.source.DefaultGoalRepository;
import com.agh.goaltracker.model.source.GoalRepository;
import com.agh.goaltracker.model.source.local.GoalDatabase;
import com.agh.goaltracker.model.source.local.GoalLocalDataSource;

import androidx.room.Room;

public class GoalTrackerApplication extends Application {

    public static final String CONTRIBUTION_NOTIFICATION_CHANNEL_ID = "CONTRIBUTION_NOTIFICATION_CHANNEL";
    private GoalRepository goalRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        GoalDatabase goalDatabase = Room.databaseBuilder(getApplicationContext(), GoalDatabase.class, "goals").build();
        goalRepository = new DefaultGoalRepository(new GoalLocalDataSource(goalDatabase.goalDao()));
        deleteDatabase("goals");
        goalRepository.saveGoal(new Goal("1", null, 0, 1));
        goalRepository.saveGoal(new Goal("2"));
        goalRepository.saveGoal(new Goal("3", null, 0, 1));
        goalRepository.saveGoal(new Goal("4", null, 0, 1));
        goalRepository.saveGoal(new Goal("5", null, 0, 1));
        goalRepository.saveGoal(new Goal("6", null, 0, 1));
        goalRepository.saveGoal(new Goal("7", null, 0, 1));
        goalRepository.saveGoal(new Goal("8", null, 0, 1));
        goalRepository.saveGoal(new Goal("9", null, 0, 1));
        goalRepository.saveGoal(new Goal("01", null, 0, 1));
        goalRepository.saveGoal(new Goal("02", null, 0, 1));
        createNotificationChannel();
    }


    public GoalRepository getGoalRepository() {
        return goalRepository;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.contribution_notification_channel);
            String description = getString(R.string.contribution_notification_channel_description);
            NotificationChannel channel = new NotificationChannel(CONTRIBUTION_NOTIFICATION_CHANNEL_ID, name, NotificationManager.IMPORTANCE_LOW);
            channel.setSound(null, null);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
