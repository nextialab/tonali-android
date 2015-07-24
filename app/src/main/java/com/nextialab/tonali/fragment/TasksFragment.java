package com.nextialab.tonali.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nextialab.tonali.R;
import com.nextialab.tonali.adapter.TasksAdapter;
import com.nextialab.tonali.support.TonaliPersistence;

import java.util.ArrayList;

/**
 * Created by nigonzalez on 7/11/15.
 */
public class TasksFragment extends Fragment {

    private int mListId;
    private String mListName;

    private TasksAdapter mAdapter;

    private TonaliPersistence mPersistance;

    public TasksFragment() {

    }

    @Override
    public void onAttach (Activity activity) {
        super.onAttach(activity);
        mPersistance = new TonaliPersistence(activity);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        RecyclerView recycler = (RecyclerView) view.findViewById(R.id.tasks_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(mAdapter);
        loadTasks();
        return view;
    }

    private void loadTasks() {
        ArrayList<String> lists = mPersistance.getTasksForList(mListId);
        mAdapter.setLists(lists);
    }

}
