package com.nextialab.tonali.holder;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.PopupMenu;

import com.nextialab.tonali.R;
import com.nextialab.tonali.support.ItemTouchHelperCallback;

/**
 * Created by Nelson on 9/20/2015.
 */
public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener, ItemTouchHelperCallback.StateListener {

    public interface Listener {

        void onClick(int which);
        void onOpen(int which);
        void onRename(int which);
        void onDelete(int which);
        void onMark(int which, boolean state);

    }

    private View mView;
    private int mPosition;
    private GestureDetectorCompat mGestureDetectorCompat;
    private Listener mListener;

    PopupMenu.OnMenuItemClickListener mOnMenuItemClickListener = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.list_rename:
                    if (mListener != null) mListener.onRename(mPosition);
                    break;
                case R.id.list_delete:
                    if (mListener != null) mListener.onDelete(mPosition);
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
    }

    class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (mListener != null) mListener.onClick(mPosition);
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            PopupMenu menu = new PopupMenu(mView.getContext(), mView);
            menu.setOnMenuItemClickListener(mOnMenuItemClickListener);
            MenuInflater inflater = menu.getMenuInflater();
            inflater.inflate(R.menu.menu_list, menu.getMenu());
            menu.show();
            return true;
        }

    }

    public CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (mListener != null) mListener.onMark(mPosition, isChecked);
        }
    };

    public ListViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mGestureDetectorCompat = new GestureDetectorCompat(mView.getContext(), new GestureListener());
        mView.setOnTouchListener(this);
        ((CheckBox) mView.findViewById(R.id.list_checkbox)).setOnCheckedChangeListener(mOnCheckedChangeListener);
        mView.findViewById(R.id.list_open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) mListener.onOpen(mPosition);
            }
        });
    }

    public void setChecked(boolean state) {
        CheckBox checkbox = (CheckBox) mView.findViewById(R.id.list_checkbox);
        checkbox.setOnCheckedChangeListener(null);
        checkbox.setChecked(state);
        checkbox.setOnCheckedChangeListener(mOnCheckedChangeListener);
    }

    public View getView() {
        return mView;
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

}
