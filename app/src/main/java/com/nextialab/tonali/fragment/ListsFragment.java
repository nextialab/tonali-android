package com.nextialab.tonali.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.nextialab.tonali.R;
import com.nextialab.tonali.adapter.ListsAdapter;
import com.nextialab.tonali.model.List;
import com.nextialab.tonali.support.ActivityListener;
import com.nextialab.tonali.support.Persistence;

import java.util.ArrayList;

/**
 * Created by nigonzalez on 7/11/15.
 */
public class ListsFragment extends Fragment {

    private ListsAdapter mAdapter = new ListsAdapter(this);
    private Persistence mPersistence = null;
    private ActivityListener mListener = null;

    public ListsFragment() {

    }

    @Override
    public void onAttach (Activity activity) {
        super.onAttach(activity);
        mPersistence = new Persistence(activity);
        if (activity instanceof ActivityListener) {
            mListener = (ActivityListener) activity;
        }
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lists, container, false);
        RecyclerView recycler = (RecyclerView) view.findViewById(R.id.lists_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(mAdapter);
        loadLists();
        return view;
    }

    private void loadLists() {
        ArrayList<List> lists = mPersistence.getLists();
        mAdapter.setLists(lists);
    }

    private void onNewList(String listName) {
        if (mPersistence.createList(listName)) {
            loadLists();
        } else {
            Log.e("tonali", "Could not create " + listName);
        }
    }

    public void onNewList() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View input = getActivity().getLayoutInflater().inflate(R.layout.new_list_layout, null);
        final TextInputLayout editTextWrapper = (TextInputLayout) input.findViewById(R.id.lists_input_new_list_wrapper);
        final EditText editText = (EditText) input.findViewById(R.id.lists_input_new_list);
        builder.setTitle(R.string.lists_new_list_title);
        builder.setView(input);
        builder.setPositiveButton(R.string.lists_create_list, null);
        builder.setNegativeButton(R.string.lists_cancel_list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editTextWrapper.setErrorEnabled(false);
                        String listName = editText.getText().toString();
                        if (listName.length() > 0) {
                            onNewList(listName);
                            dialog.dismiss();
                        } else {
                            editTextWrapper.setError(getString(R.string.lists_new_list_error));
                        }
                    }
                });
            }
        });
        dialog.show();
    }

    public void goToList(List list) {
        if (mListener != null) mListener.goToList(list);
    }

}
