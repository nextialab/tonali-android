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
public class ListViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperCallback.StateListener {

    public interface Listener {

        void onClick(int which);
        void onOpen(int which);
        void onMark(int which, boolean state);

    }

    private View mView;
    private int mPosition;
    private Listener mListener;

    @Override
    public void onItemSelected() {
        mView.findViewById(R.id.list_container).setBackgroundColor(mView.getContext().getResources().getColor(R.color.md_gray_100));
    }

    @Override
    public void onItemClear() {
        mView.findViewById(R.id.list_container).setBackgroundColor(0);
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
        mView.findViewById(R.id.list_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) mListener.onClick(mPosition);
            }
        });
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
