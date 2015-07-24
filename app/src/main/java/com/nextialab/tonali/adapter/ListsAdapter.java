package com.nextialab.tonali.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nextialab.tonali.R;

import java.util.ArrayList;

/**
 * Created by nigonzalez on 7/7/15.
 */
public class ListsAdapter extends RecyclerView.Adapter<ListsAdapter.ViewHolder> {

    private ArrayList<String> mLists = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View mView;

        public ViewHolder(View v) {
            super(v);
            mView = v;
        }

    }

    public ListsAdapter() {

    }

    public void setLists(ArrayList<String> data) {
        mLists = data;
        notifyDataSetChanged();
    }

    public void pushLists(ArrayList<String> data) {
        mLists.addAll(0, data);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ((TextView) holder.mView.findViewById(R.id.list_name)).setText(mLists.get(position));
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

}
