package com.agh.goaltracker.goals.ui.goals;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.agh.goaltracker.R;
import com.agh.goaltracker.model.Goal;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.ViewHolder> {

    private List<Goal> goals = new ArrayList<>();
    private final GoalsFragment.GoalsListListener goalsListListener;

    public GoalsAdapter(GoalsFragment.GoalsListListener goalsListListener) {
        this.goalsListListener = goalsListListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.goal_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Goal goal = goals.get(position);
        holder.goal = goal;
        holder.goalTitle.setText(goal.getTitle());
        holder.itemView.setOnClickListener(v -> goalsListListener.goToGoalDetailsActivity(goal));
        holder.itemView.setOnLongClickListener(v -> {
            goalsListListener.goToEditGoalDetailsActivity(goal);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return goals.size();
    }

    void updateData(List<Goal> goals) {
        this.goals = goals;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        Goal goal;
        @BindView(R.id.goal_title)
        TextView goalTitle;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
