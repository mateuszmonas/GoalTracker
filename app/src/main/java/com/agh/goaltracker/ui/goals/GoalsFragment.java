package com.agh.goaltracker.ui.goals;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.agh.goaltracker.GoalTrackerApplication;
import com.agh.goaltracker.R;
import com.agh.goaltracker.AddGoalActivity;
import com.agh.goaltracker.model.Goal;
import com.agh.goaltracker.util.ViewModelFactory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class GoalsFragment extends Fragment {

    private static final String TAG = "GoalsFragment";
    @BindView(R.id.goals_recycler_view)
    RecyclerView goalsRecyclerView;
    private GoalsViewModel goalsViewModel;
    private GoalsAdapter goalsAdapter;
    private Unbinder unbinder;
    private GoalsListListener goalsListListener = new GoalsListListener() {
        @Override
        public void goToGoalDetailsActivity(Goal goal) {
            // TODO: 09/02/20 implement goal details
            Log.d(TAG, "goToGoalDetailsActivity() called with: goal = [" + goal + "]");
        }

        @Override
        public void goToEditGoalDetailsActivity(Goal goal) {
            // TODO: 09/02/20 implement edit goal details
            Log.d(TAG, "goToEditGoalDetailsActivity() called with: goal = [" + goal + "]");
        }
    };

    public static GoalsFragment newInstance() {
        return new GoalsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        goalsAdapter = new GoalsAdapter(goalsListListener);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.goals_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        goalsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        goalsRecyclerView.setAdapter(goalsAdapter);
        helper.attachToRecyclerView(goalsRecyclerView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        goalsViewModel = new ViewModelProvider(this, new ViewModelFactory(
                ((GoalTrackerApplication) getActivity().getApplication()).getGoalRepository()
        )).get(GoalsViewModel.class);
        goalsViewModel.goals.observe(getViewLifecycleOwner(), goals -> goalsAdapter.updateData(goals));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.goals_menu, menu);
        goalsViewModel.filters.observe(getViewLifecycleOwner(), goalsFilterTypes -> {
            menu.findItem(R.id.current_goals).setChecked(goalsFilterTypes.contains(GoalsViewModel.GoalsFilterType.CURRENT_GOALS));
            menu.findItem(R.id.completed_goals).setChecked(goalsFilterTypes.contains(GoalsViewModel.GoalsFilterType.COMPLETED_GOALS));
            menu.findItem(R.id.failed_goals).setChecked(goalsFilterTypes.contains(GoalsViewModel.GoalsFilterType.FAILED_GOALS));
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.current_goals:
                goalsViewModel.setFiltering(GoalsViewModel.GoalsFilterType.CURRENT_GOALS);
                return true;
            case R.id.completed_goals:
                goalsViewModel.setFiltering(GoalsViewModel.GoalsFilterType.COMPLETED_GOALS);
                return true;
            case R.id.failed_goals:
                goalsViewModel.setFiltering(GoalsViewModel.GoalsFilterType.FAILED_GOALS);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    ItemTouchHelper helper = new ItemTouchHelper(
            new ItemTouchHelper.SimpleCallback(0,
                    ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(RecyclerView recyclerView,
                                      RecyclerView.ViewHolder viewHolder,
                                      RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder,
                                     int direction) {
                    int position = viewHolder.getAdapterPosition();
                    Goal goal = goalsAdapter.getItemAt(position);
                    Toast.makeText(getContext(), "Deleting " +
                            goal.getTitle(), Toast.LENGTH_LONG).show();

                    goalsViewModel.delete(goal);
                }});

    @OnClick(R.id.add_goal_fab)
    public void navigateToAddGoalActivity() {
        Intent intent = AddGoalActivity.createIntent(getContext());
        startActivity(intent);
    }

    interface GoalsListListener {
        void goToGoalDetailsActivity(Goal goal);

        void goToEditGoalDetailsActivity(Goal goal);
    }
}
