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
import com.nextialab.tonali.support.ItemTouchHelperCallback;
import com.nextialab.tonali.support.Persistence;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by nigonzalez on 7/11/15.
 */
@Deprecated
public class TasksAdapter extends RecyclerView.Adapter<TaskViewHolder> implements ItemTouchHelperCallback.Listener {

    private ArrayList<Task> mTasks = new ArrayList<>();
    private ArrayList<Integer> mOrder = new ArrayList<>();
    private Persistence mPersistence;
    private TasksFragment mTasksFragment;
    private int mListId = -1;

    public TasksAdapter(TasksFragment tasksFragment) {
        mTasksFragment = tasksFragment;
    }

    public void setPersistence(Persistence persistence) {
        mPersistence = persistence;
    }

    public void setLists(ArrayList<Task> tasks, ArrayList<Integer> order, int listId) {
        mTasks = tasks;
        mOrder = order;
        mListId = listId;
        notifyDataSetChanged();
    }

    public void addTask(Task task, int position) {
        mTasks.add(position, task);
        mOrder.add(position, task.getId());
        mPersistence.updateListOrder(mListId, mOrder);
        notifyItemInserted(position);
    }

    public void removeTask(Task task) {
        int position = mTasks.indexOf(task);
        mTasks.remove(position);
        mOrder.remove(position);
        mPersistence.updateListOrder(mListId, mOrder);
        notifyItemRemoved(position);
    }

    public void updateTask(Task task) {
        int oldPosition = mTasks.indexOf(task);
        Log.i("TasksAdapter", "Positions 0 " + oldPosition + " " + mTasks.size());
        if (task.isDone()) {
            if (oldPosition != mTasks.size() - 1) {
                mTasks.remove(oldPosition);
                mTasks.add(task);
                mOrder.remove(oldPosition);
                mOrder.add(task.getId());
                notifyItemMoved(oldPosition, mTasks.size() - 1);
            }
        } else {
            if (oldPosition != 0) {
                mTasks.remove(oldPosition);
                mTasks.add(0, task);
                mOrder.remove(oldPosition);
                mOrder.add(0, task.getId());
                notifyItemMoved(oldPosition, 0);
            }
        }
        saveOrder();
    }

    public void saveOrder() {
        mPersistence.updateListOrder(mListId, mOrder);
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

    @Override
    public void onMove(int start, int end) {
        Collections.swap(mTasks, start, end);
        Collections.swap(mOrder, start, end);
        notifyItemMoved(start, end);
    }
}
