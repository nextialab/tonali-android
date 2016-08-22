package com.nextialab.tonali.adapter;

import android.graphics.Paint;
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
import java.util.List;

/**
 * Created by nigonzalez on 7/7/15.
 */
public class ListsAdapter extends RecyclerView.Adapter<ListViewHolder> implements ItemTouchHelperCallback.Listener {

    public interface Listener extends ListViewHolder.Listener {

        void onSwap(int from, int to);

    }

    private List<TonaliList> mLists = new ArrayList<>();
    private Listener mListener;

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public void setList(List<TonaliList> lists) {
        mLists = lists;
        notifyDataSetChanged();
    }

    public void addList(TonaliList list, int position) {
        mLists.add(position, list);
        notifyItemInserted(position);
    }

    public void removeList(TonaliList list) {
        int position = mLists.indexOf(list);
        mLists.remove(position);
        notifyItemRemoved(position);
    }

    public TonaliList getItemAtPosition(int position) {
        return mLists.get(position);
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list, parent, false);
        ListViewHolder vh = new ListViewHolder(view);
        vh.setListener(mListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        holder.setPosition(position);
        TonaliList list = mLists.get(position);
        TextView name = (TextView) holder.getView().findViewById(R.id.list_name);
        name.setText(list.getListName());
        holder.setChecked(list.isChecked());
        if (list.isChecked()) {
            name.setPaintFlags(name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            name.setTextColor(name.getContext().getResources().getColor(R.color.tonali_gray));
        } else {
            name.setPaintFlags(name.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            name.setTextColor(name.getContext().getResources().getColor(R.color.tonali_black));
        }
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    @Override
    public void onMove(int start, int end) {
        Collections.swap(mLists, start, end);
        notifyItemMoved(start, end);
        if (mListener != null) mListener.onSwap(start, end);
    }
}
