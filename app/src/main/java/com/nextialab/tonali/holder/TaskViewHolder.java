package com.nextialab.tonali.holder;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.nextialab.tonali.R;
import com.nextialab.tonali.adapter.TasksAdapter;
import com.nextialab.tonali.fragment.TasksFragment;
import com.nextialab.tonali.model.Task;
import com.nextialab.tonali.support.Persistence;
import com.nextialab.tonali.support.TonaliAlarmManager;

/**
 * Created by Nelson on 9/8/2015.
 */
public class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {

    private View mView;
    private Task mTask;
    private GestureDetectorCompat mGestureDetectorCompat;
    private Persistence mPersistence;
    private TasksFragment mTasksFragment;
    private TasksAdapter mTasksAdapter;

    class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        onSwipeRight();
                    }
                }
            }
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (!mTask.isDone()) {
                mTasksFragment.goToTask(mTask);
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        private void onSwipeRight() {
            if (mPersistence.setTaskDone(mTask.getId(), !mTask.isDone())) {
                mTask.setDone(!mTask.isDone());
                TextView view = (TextView) mView.findViewById(R.id.task_name);;
                if (mTask.isDone()) {
                    view.setPaintFlags(view.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    view.setTextColor(view.getContext().getResources().getColor(R.color.tonali_gray));
                    if (mTask.hasAlarm()) {
                        TonaliAlarmManager.removeAlarmForTask(mTasksFragment.getActivity(), mTask);
                    }
                } else {
                    view.setPaintFlags(view.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    view.setTextColor(view.getContext().getResources().getColor(R.color.tonali_black));
                }
                mTasksAdapter.updateTask(mTask);
            } else {
                Log.e("TaskAdapter", "Could not set task as done");
            }
        }

        private void onSwipeLeft() {
            Log.i("TaskAdapter", "Swipe left");
            if (mPersistence.setTaskCleared(mTask.getId())) {
                mTasksAdapter.removeTask(mTask);
            } else {
                Log.e("TaskAdapter", "Could not clear task");
            }
        }

    }

    public TaskViewHolder(Context context, View v, TasksFragment tasksFragment, Persistence persistence) {
        super(v);
        mTasksFragment = tasksFragment;
        mPersistence = persistence;
        mView = v;
        mGestureDetectorCompat = new GestureDetectorCompat(context, new GestureListener());
        mView.setOnTouchListener(this);
    }

    public void setTasksAdapter(TasksAdapter tasksAdapter) {
        mTasksAdapter = tasksAdapter;
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
