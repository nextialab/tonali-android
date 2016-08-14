package com.nextialab.tonali.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import com.nextialab.tonali.R;
import com.nextialab.tonali.adapter.ListsAdapter;
import com.nextialab.tonali.model.TonaliList;
import com.nextialab.tonali.support.ActivityListener;
import com.nextialab.tonali.support.ItemTouchHelperCallback;
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
    public void onAttach (Context activity) {
        super.onAttach(activity);
        mPersistence = new Persistence(activity);
        mAdapter.setPersistence(mPersistence);
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
        ItemTouchHelperCallback callback = new ItemTouchHelperCallback();
        callback.setListener(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recycler);
        loadLists();
        return view;
    }

    private void loadLists() {
        ArrayList<TonaliList> lists = mPersistence.getListsWithCount();
        ArrayList<Integer> order = mPersistence.getListsOrder();
        if (order != null) {
            ArrayList<TonaliList> orderedLists = new ArrayList<>();
            for (Integer id : order) {
                TonaliList toAdd = null;
                for (TonaliList list : lists) {
                    if (list.getId() == id) {
                        toAdd = list;
                        break;
                    }
                }
                if (toAdd != null) orderedLists.add(toAdd);
            }
            mAdapter.setLists(orderedLists, order);

        } else {
            order = new ArrayList<>();
            for (TonaliList list : lists) {
                order.add(list.getId());
            }
            mPersistence.createListsOrder(order);
            mAdapter.setLists(lists, order);
        }
    }

    private void onNewList(String listName) {
        TonaliList list = mPersistence.createNewList(listName);
        if (list != null) {
            mAdapter.addList(list, 0);
        } else {
            Log.e("tonali", "Could not create " + listName);
        }
    }

    private void onUpdateList(TonaliList list, String name) {
        if (mPersistence.updateListName(list.getId(), name)) {
            list.setListName(name);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void onNewList() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View input = getActivity().getLayoutInflater().inflate(R.layout.new_list_layout, null);
        final EditText editText = (EditText) input.findViewById(R.id.lists_input_new_list);
        builder.setView(input);
        builder.setPositiveButton(R.string.lists_create_list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String listName = editText.getText().toString();
                if (listName.length() > 0) {
                    onNewList(listName);
                }
            }
        });
        builder.setNegativeButton(R.string.lists_cancel_list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        final AlertDialog dialog = builder.create();
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });
        dialog.show();
    }

    public void onUpdateList(final TonaliList list) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View input = getActivity().getLayoutInflater().inflate(R.layout.dialog_list_update, null);
        final EditText editText = (EditText) input.findViewById(R.id.lists_input_update_list);
        editText.setText(list.getListName());
        editText.setSelection(list.getListName().length());
        builder.setView(input);
        builder.setPositiveButton(R.string.lists_update_list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String listName = editText.getText().toString();
                if (listName.length() > 0) {
                    onUpdateList(list, listName);
                }
            }
        });
        builder.setNegativeButton(R.string.lists_cancel_list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        final AlertDialog dialog = builder.create();
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });
        dialog.show();
    }

    public void goToList(TonaliList list) {
        mListener.goToList(list);
    }

}
