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

import java.util.ArrayList;

/**
 * Created by nigonzalez on 7/11/15.
 */
public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {

    private ArrayList<String> mTasks = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {

        public View mView;
        private GestureDetectorCompat mGestureDetectorCompat;

        class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                Log.i("tonali", "going down on task");
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
                Log.i("tonali", "Click on task");
                return true;
            }

            private void onSwipeRight() {
                Log.i("tonali", "Swipe right on task");
                TextView textView = (TextView) mView.findViewById(R.id.task_name);
                textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

            private void onSwipeLeft() {
                Log.i("tonali", "Swipe left on task");
            }

        }

        public ViewHolder(Context context, View v) {
            super(v);
            mView = v;
            mGestureDetectorCompat = new GestureDetectorCompat(context, new GestureListener());
            mView.setOnTouchListener(this);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return mGestureDetectorCompat.onTouchEvent(event);
        }
    }

    public TasksAdapter() {

    }

    public void setLists(ArrayList<String> data) {
        mTasks = data;
        notifyDataSetChanged();
    }

    public void pushLists(ArrayList<String> data) {
        mTasks.addAll(0, data);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_task, parent, false);
        ViewHolder vh = new ViewHolder(parent.getContext(), view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ((TextView) holder.mView.findViewById(R.id.task_name)).setText(mTasks.get(position));
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

}
