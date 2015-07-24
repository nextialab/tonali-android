package com.nextialab.tonali.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View mView;

        public ViewHolder(View v) {
            super(v);
            mView = v;
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
        ViewHolder vh = new ViewHolder(view);
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
