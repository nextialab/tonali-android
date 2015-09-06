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
import com.nextialab.tonali.model.Task;
import com.nextialab.tonali.support.Persistence;

import java.util.ArrayList;

/**
 * Created by nigonzalez on 7/11/15.
 */
public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {

    private ArrayList<Task> mTasks = new ArrayList<>();
    private Persistence mPersistence;
    private TasksFragment mTasksFragment;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {

        private View mView;
        private Task mTask;
        private GestureDetectorCompat mGestureDetectorCompat;
        private Persistence mPersistence;
        private TasksFragment mTasksFragment;

        class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight();
                            } else {
                                onSwipeLeft();
                            }
                        }
                        result = true;
                    }
                    result = true;
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                mTasksFragment.onEditTask(mTask.getId(), mTask.getTask());
            }

            private void onSwipeRight() {
                if (mPersistence.setTaskDone(mTask.getId(), !mTask.isDone())) {
                    mTasksFragment.loadTasks();
                } else {
                    Log.e("TaskAdapter", "Could not set task as done");
                }
            }

            private void onSwipeLeft() {
                Log.i("TaskAdapter", "Swipe left");
                if (mPersistence.setTaskCleared(mTask.getId())) {
                    mTasksFragment.loadTasks();
                } else {
                    Log.e("TaskAdapter", "Could not clear task");
                }
            }

        }

        public ViewHolder(Context context, View v, TasksFragment tasksFragment, Persistence persistence) {
            super(v);
            mTasksFragment = tasksFragment;
            mPersistence = persistence;
            mView = v;
            mGestureDetectorCompat = new GestureDetectorCompat(context, new GestureListener());
            mView.setOnTouchListener(this);
        }

        public void setTask(Task task) {
            mTask = task;
        }

        public View getView() {
            return mView;
        }

        public void setView(View view) {
            mView = view;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return mGestureDetectorCompat.onTouchEvent(event);
        }
    }

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

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_task, parent, false);
        ViewHolder vh = new ViewHolder(parent.getContext(), view, mTasksFragment, mPersistence);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
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
