package com.agh.goaltracker.ui.goals;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.agh.goaltracker.R;
import com.agh.goaltracker.model.Goal;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.ViewHolder> {

    private final GoalsFragment.GoalsListListener goalsListListener;
    private List<Goal> goals = new ArrayList<>();

    public GoalsAdapter(GoalsFragment.GoalsListListener goalsListListener) {
        this.goalsListListener = goalsListListener;
        setHasStableIds(true);
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
        holder.setGoal(goal);
        holder.goalTitle.setText(goal.getTitle());

        DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy", Locale.ENGLISH);
        Date date = goal.getDueDate();
        if (date == null) {
            holder.dueDate.setText("unlimited");
        } else {
            holder.dueDate.setText(dateFormat.format(goal.getDueDate()));
        }
        holder.itemView.setOnClickListener(v -> goalsListListener.goToGoalDetailsActivity(goal.goalId));
        holder.itemView.setOnLongClickListener(v -> {
            goalsListListener.goToEditGoalDetailsActivity(goal.goalId);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return goals.size();
    }

    void updateData(List<Goal> newGoals) {
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return goals.size();
            }

            @Override
            public int getNewListSize() {
                return newGoals.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return goals.get(oldItemPosition).equals(newGoals.get(newItemPosition));
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return goals.get(oldItemPosition).equals(newGoals.get(newItemPosition));
            }
        });
        this.goals = newGoals;
        result.dispatchUpdatesTo(this);
    }

    public Goal getItemAt(int position) {
        return goals.get(position);
    }

    @Override
    public long getItemId(int position) {
        return goals.get(position).getGoalId();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        Goal goal;
        boolean isContributing = false;
        @BindView(R.id.goal_title)
        TextView goalTitle;
        @BindView(R.id.goal_due_date)
        TextView dueDate;
        @BindView(R.id.plant)
        ImageView plant;
        @BindView(R.id.progress_bar)
        ProgressBar progressBar;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setGoal(Goal goal) {
            this.goal = goal;
            progressBar.setProgress(goal.getCurrentProgress());
            if (goal.getTotalGoal() == 0) {
                progressBar.setVisibility(View.GONE);
            } else {
                progressBar.setMax(goal.totalGoal);
            }
            plant.setImageResource(goal.getProgressDrawable().resource_id);
        }

        @OnClick(R.id.contribution_btn)
        public void startContributionService(FloatingActionButton btn){
            if(isContributing){
                if(goal.isProgressAsTime()){
                    isContributing = false;
                    goalsListListener.stopContributing(goal);
                }
                btn.setImageResource(R.drawable.ic_add_black_24dp);
            }else{
                if(goal.isProgressAsTime()){
                    isContributing = true;
                    btn.setImageResource(R.drawable.ic_close_black_24dp);
                }
                goalsListListener.startContributing(goal);
            }
        }
    }
}
