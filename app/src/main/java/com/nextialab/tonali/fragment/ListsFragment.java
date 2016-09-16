package com.nextialab.tonali.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.nextialab.tonali.DetailsActivity;
import com.nextialab.tonali.R;
import com.nextialab.tonali.adapter.ListsAdapter;
import com.nextialab.tonali.dialog.NewListDialog;
import com.nextialab.tonali.model.ListType;
import com.nextialab.tonali.model.TonaliList;
import com.nextialab.tonali.support.ActivityListener;
import com.nextialab.tonali.support.ItemTouchHelperCallback;
import com.nextialab.tonali.support.ListsListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by nigonzalez on 7/11/15.
 */
public class ListsFragment extends Fragment implements ListsListener {

    public static final String PARENT = "parent";

    @Override
    public void onNewList() {
        NewListDialog dialog = new NewListDialog();
        dialog.setListener(new NewListDialog.Listener() {
            @Override
            public void onAccept(String name) {
                TonaliList newList = new TonaliList();
                newList.setParent(mParent.getId());
                newList.setOwner(1L);
                newList.setListName(name);
                newList.setContent("");
                if (newList.save()) {
                    mParent.getSequence().add(0, newList.getId());
                    if (mParent.save()) {
                        mAdapter.addList(newList, 0);
                    }
                }
            }
        });
        dialog.show(getFragmentManager(), null);
    }

    @Override
    public void onSwap(int from, int to) {
        Collections.swap(mParent.getSequence(), from, to);
        if (mParent.save()) {
            Log.i("ListFragment", "Items swap");
        }
    }

    @Override
    public void onClick(int which) {
        TonaliList list = mAdapter.getItemAtPosition(which);
        Intent intent = new Intent(getActivity(), DetailsActivity.class);
        intent.putExtra(DetailsActivity.LIST, list);
        startActivity(intent);
    }

    @Override
    public void onOpen(int which) {
        if (mListener != null) {
            TonaliList list = mAdapter.getItemAtPosition(which);
            mListener.goToList(list);
        }
    }

    @Override
    public void onMark(int which, boolean state) {
        TonaliList list = mAdapter.getItemAtPosition(which);
        list.setIsChecked(state);
        if (list.save()) {
            mAdapter.notifyDataSetChanged();
        }
    }

    private ActivityListener mListener;
    private ListsAdapter mAdapter = new ListsAdapter();
    private TonaliList mParent;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ActivityListener) {
            mListener = (ActivityListener) context;
        }
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lists, container, false);
        mAdapter.setListener(this);
        RecyclerView recycler = (RecyclerView) view.findViewById(R.id.lists_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(mAdapter);
        ItemTouchHelperCallback callback = new ItemTouchHelperCallback();
        callback.setListener(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recycler);
        mParent = getArguments().getParcelable(PARENT);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mParent != null) {
            loadLists();
        }
    }

    private void loadLists() {
        List<TonaliList> children = TonaliList.findChildren(mParent.getId());
        List<Long> sequence = mParent.getSequence();
        List<TonaliList> ordered = new ArrayList<>(children.size());
        for (Long index : sequence) {
            for (TonaliList child : children) {
                if (child.getId() == index) {
                    ordered.add(child);
                    break;
                }
            }
        }
        mAdapter.setList(ordered);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }
        return false;
    }

}
