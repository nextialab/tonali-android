package com.nextialab.tonali.adapter;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nextialab.tonali.R;
import com.nextialab.tonali.fragment.ListsFragment;
import com.nextialab.tonali.model.List;

import java.util.ArrayList;

/**
 * Created by nigonzalez on 7/7/15.
 */
public class ListsAdapter extends RecyclerView.Adapter<ListsAdapter.ViewHolder> {

    private ArrayList<List> mLists = new ArrayList<>();
    private ListsFragment mListsFragment;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public View mView;
        private List mList;
        private ListsFragment mListsFragment;

        public ViewHolder(View v) {
            super(v);
            mView = v;
            mView.setOnClickListener(this);
        }

        public void setList(List list) {
            mList = list;
        }

        public void setListsFragment(ListsFragment listsFragment) {
            mListsFragment = listsFragment;
        }

        @Override
        public void onClick(View view) {
            mListsFragment.goToList(mList);
        }

    }

    public ListsAdapter(ListsFragment listsFragment) {
        mListsFragment = listsFragment;
    }

    public void setLists(ArrayList<List> data) {
        mLists = data;
        notifyDataSetChanged();
    }

    public void pushLists(ArrayList<List> data) {
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
        ((TextView) holder.mView.findViewById(R.id.list_name)).setText(mLists.get(position).getListName());
        holder.setList(mLists.get(position));
        holder.setListsFragment(mListsFragment);
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

}
