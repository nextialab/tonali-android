package com.nextialab.tonali.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nextialab.tonali.R;

/**
 * Created by nigonzalez on 7/7/15.
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private String[] mData;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View mView;

        public ViewHolder(View v) {
            super(v);
            mView = v;
        }

    }

    public HomeAdapter(String[] data) {
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_home_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ((TextView) holder.mView.findViewById(R.id.list_name)).setText(mData[position]);
    }

    @Override
    public int getItemCount() {
        return mData.length;
    }

}
