package com.nextialab.tonali.holder;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupMenu;

import com.nextialab.tonali.R;
import com.nextialab.tonali.adapter.ListsAdapter;
import com.nextialab.tonali.fragment.ListsFragment;
import com.nextialab.tonali.model.TonaliList;
import com.nextialab.tonali.model.Task;
import com.nextialab.tonali.support.ItemTouchHelperCallback;
import com.nextialab.tonali.support.Persistence;
import com.nextialab.tonali.support.TonaliAlarmManager;

import java.util.ArrayList;

/**
 * Created by Nelson on 9/20/2015.
 */
public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener, ItemTouchHelperCallback.StateListener {

    public View mView;
    private TonaliList mList;
    private ListsAdapter mListsAdapter;
    private ListsFragment mListsFragment;
    private GestureDetectorCompat mGestureDetectorCompat;
    private Persistence mPersistence;

    PopupMenu.OnMenuItemClickListener mOnMenuItemClickListener = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.list_rename:
                    mListsFragment.onUpdateList(mList);
                    break;
                case R.id.list_delete:
                    if (mPersistence.setListCleared(mList.getId())) {
                        if (mList.getTasksCount() > 0) {
                            ArrayList<Task> tasks = mPersistence.getTasksForList(mList.getId());
                            for (Task task : tasks) {
                                if (task.hasAlarm()) {
                                    TonaliAlarmManager.removeAlarmForTask(mListsFragment.getActivity(), task);
                                }
                            }
                        }
                        mListsAdapter.removeList(mList);
                    } else {
                        Log.e("ListHolder", "Could not set list as cleared");
                    }
                    break;
            }
            return true;
        }
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetectorCompat.onTouchEvent(event);
    }

    @Override
    public void onItemSelected() {
        mView.findViewById(R.id.list_container).setBackgroundColor(mView.getContext().getResources().getColor(R.color.md_gray_100));
    }

    @Override
    public void onItemClear() {
        mView.findViewById(R.id.list_container).setBackgroundColor(0);
        mListsAdapter.saveOrder();
    }

    class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            mListsFragment.goToList(mList);
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            PopupMenu menu = new PopupMenu(mListsFragment.getActivity(), mView);
            menu.setOnMenuItemClickListener(mOnMenuItemClickListener);
            MenuInflater inflater = menu.getMenuInflater();
            inflater.inflate(R.menu.menu_list, menu.getMenu());
            menu.show();
            return true;
        }

    }

    public ListViewHolder(Context context, Persistence persistence, View itemView) {
        super(itemView);
        mPersistence = persistence;
        mView = itemView;
        mGestureDetectorCompat = new GestureDetectorCompat(context, new GestureListener());
        mView.setOnTouchListener(this);
    }

    public void setList(TonaliList list) {
        mList = list;
    }

    public void setListsAdapter(ListsAdapter listAdapter) {
        mListsAdapter = listAdapter;
    }

    public void setListsFragment(ListsFragment listsFragment) {
        mListsFragment = listsFragment;
    }

}
