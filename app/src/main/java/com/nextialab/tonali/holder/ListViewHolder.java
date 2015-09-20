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
import com.nextialab.tonali.model.List;
import com.nextialab.tonali.support.Persistence;

/**
 * Created by Nelson on 9/20/2015.
 */
public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {

    public View mView;
    private List mList;
    private ListsAdapter mListsAdapter;
    private ListsFragment mListsFragment;
    private GestureDetectorCompat mGestureDetectorCompat;
    private Persistence mPersistence;

    PopupMenu.OnMenuItemClickListener mOnMenuItemClickListener = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.list_delete:
                    if (mPersistence.setListCleared(mList.getId())) {
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
        public void onLongPress(MotionEvent e) {
            PopupMenu menu = new PopupMenu(mListsFragment.getActivity(), mView);
            menu.setOnMenuItemClickListener(mOnMenuItemClickListener);
            MenuInflater inflater = menu.getMenuInflater();
            inflater.inflate(R.menu.menu_list, menu.getMenu());
            menu.show();
        }

    }

    public ListViewHolder(Context context, Persistence persistence, View itemView) {
        super(itemView);
        mPersistence = persistence;
        mView = itemView;
        mGestureDetectorCompat = new GestureDetectorCompat(context, new GestureListener());
        mView.setOnTouchListener(this);
    }

    public void setList(List list) {
        mList = list;
    }

    public void setListsAdapter(ListsAdapter listAdapter) {
        mListsAdapter = listAdapter;
    }

    public void setListsFragment(ListsFragment listsFragment) {
        mListsFragment = listsFragment;
    }

}
