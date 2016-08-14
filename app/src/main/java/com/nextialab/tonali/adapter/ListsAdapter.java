package com.nextialab.tonali.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nextialab.tonali.R;
import com.nextialab.tonali.fragment.ListsFragment;
import com.nextialab.tonali.holder.ListViewHolder;
import com.nextialab.tonali.model.TonaliList;
import com.nextialab.tonali.support.ItemTouchHelperCallback;
import com.nextialab.tonali.support.Persistence;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by nigonzalez on 7/7/15.
 */
public class ListsAdapter extends RecyclerView.Adapter<ListViewHolder> implements ItemTouchHelperCallback.Listener {

    private ArrayList<TonaliList> mLists = new ArrayList<>();
    private ArrayList<Integer> mOrder = new ArrayList<>();
    private ListsFragment mListsFragment;
    private Persistence mPersistence;

    public ListsAdapter(ListsFragment listsFragment) {
        mListsFragment = listsFragment;
    }

    public void setPersistence(Persistence persistence) {
        mPersistence = persistence;
    }

    public void setLists(ArrayList<TonaliList> data, ArrayList<Integer> order) {
        mLists = data;
        mOrder = order;
        notifyDataSetChanged();
    }

    public void addList(TonaliList list, int position) {
        mLists.add(position, list);
        mOrder.add(position, list.getId());
        mPersistence.updateListsOrder(mOrder);
        notifyItemInserted(position);
    }

    public void removeList(TonaliList list) {
        int position = mLists.indexOf(list);
        mLists.remove(position);
        mOrder.remove(position);
        mPersistence.updateListsOrder(mOrder);
        notifyItemRemoved(position);
    }

    public void saveOrder() {
        mPersistence.updateListsOrder(mOrder);
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list, parent, false);
        ListViewHolder vh = new ListViewHolder(parent.getContext(), mPersistence, view);
        vh.setListsAdapter(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        ((TextView) holder.mView.findViewById(R.id.list_name)).setText(mLists.get(position).getListName());
        ((TextView) holder.mView.findViewById(R.id.list_tasks_counter)).setText(Integer.toString(mLists.get(position).getTasksCount()));
        holder.setList(mLists.get(position));
        holder.setListsFragment(mListsFragment);
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    @Override
    public void onMove(int start, int end) {
        Collections.swap(mLists, start, end);
        Collections.swap(mOrder, start, end);
        notifyItemMoved(start, end);
    }
}
