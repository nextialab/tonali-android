package com.nextialab.tonali.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nextialab.tonali.R;
import com.nextialab.tonali.fragment.TasksFragment;
import com.nextialab.tonali.holder.TaskViewHolder;
import com.nextialab.tonali.model.Task;
import com.nextialab.tonali.support.Persistence;

import java.util.ArrayList;

/**
 * Created by nigonzalez on 7/11/15.
 */
public class TasksAdapter extends RecyclerView.Adapter<TaskViewHolder> {

    private ArrayList<Task> mTasks = new ArrayList<>();
    private Persistence mPersistence;
    private TasksFragment mTasksFragment;

    public TasksAdapter(TasksFragment tasksFragment) {
        mTasksFragment = tasksFragment;
    }

    public void setPersistence(Persistence persistence) {
        mPersistence = persistence;
    }

    public void setLists(ArrayList<Task> data) {
        mTasks = data;
        notifyDataSetChanged();
    }

    public void addTask(Task task, int position) {
        mTasks.add(position, task);
        notifyItemInserted(position);
    }

    public void removeTask(Task task) {
        int position = mTasks.indexOf(task);
        mTasks.remove(position);
        notifyItemRemoved(position);
    }

    public void updateTask(Task task) {
        int oldPosition = mTasks.indexOf(task);
        mTasks.remove(oldPosition);
        int newPosition = 0;
        for (Task mTask : mTasks) {
            if (mTask.compareTo(task) > 0) {
                break;
            } else {
                newPosition++;
            }
        }
        mTasks.add(newPosition, task);
        notifyItemMoved(oldPosition, newPosition);
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_task, parent, false);
        TaskViewHolder vh = new TaskViewHolder(parent.getContext(), view, mTasksFragment, mPersistence);
        vh.setTasksAdapter(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task task = mTasks.get(position);
        holder.setTask(task);
        TextView view = (TextView) holder.getView().findViewById(R.id.task_name);
        view.setText(task.getTask());
        if (task.isDone()) {
            view.setPaintFlags(view.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            view.setTextColor(view.getContext().getResources().getColor(R.color.tonali_gray));
        } else {
            view.setPaintFlags(view.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            view.setTextColor(view.getContext().getResources().getColor(R.color.tonali_black));
        }
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

}
