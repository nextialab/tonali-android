package com.nextialab.tonali.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import com.nextialab.tonali.R;
import com.nextialab.tonali.adapter.TasksAdapter;
import com.nextialab.tonali.model.List;
import com.nextialab.tonali.model.Task;
import com.nextialab.tonali.support.ActivityListener;
import com.nextialab.tonali.support.Persistence;

import java.util.ArrayList;

/**
 * Created by nigonzalez on 7/11/15.
 */
public class TasksFragment extends Fragment {

    public static final String LIST = "list";

    private List mList;

    private TasksAdapter mAdapter = new TasksAdapter(this);
    private ActivityListener mListener = null;
    private Persistence mPersistence;

    public TasksFragment() {

    }

    @Override
    public void onAttach (Activity activity) {
        super.onAttach(activity);
        mPersistence = new Persistence(activity);
        mAdapter.setPersistence(mPersistence);
        if (activity instanceof ActivityListener) {
            mListener = (ActivityListener) activity;
        }
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);
        Bundle data = getArguments();
        mList = data.getParcelable(LIST);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mList.getListName());
        RecyclerView recycler = (RecyclerView) view.findViewById(R.id.tasks_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(mAdapter);
        loadTasks();
        return view;
    }

    public void loadTasks() {
        ArrayList<Task> lists = mPersistence.getTasksForList(mList.getId());
        mAdapter.setLists(lists);
    }

    private void onNewTask(String taskName) {
        Task task = mPersistence.createNewTask(taskName, mList.getId());
        if (task != null) {
            mAdapter.addTask(task, 0);
        } else {
            Log.e("Tonali", "Could not create task " + taskName);
        }
    }

    public void onNewTask() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View input = getActivity().getLayoutInflater().inflate(R.layout.new_task_layout, null);
        final TextInputLayout editTextWrapper = (TextInputLayout) input.findViewById(R.id.lists_input_new_task_wrapper);
        final EditText editText = (EditText) input.findViewById(R.id.lists_input_new_task);
        builder.setView(input);
        builder.setPositiveButton(R.string.lists_create_list, null);
        builder.setNegativeButton(R.string.lists_cancel_list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
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
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editTextWrapper.setErrorEnabled(false);
                        String taskName = editText.getText().toString();
                        if (taskName.length() > 0) {
                            onNewTask(taskName);
                            dialog.dismiss();
                        } else {
                            editTextWrapper.setError(getString(R.string.tasks_new_task_error));
                        }
                    }
                });
            }
        });
        dialog.show();
    }

    public void goToTask(Task task) {
        mListener.goToTask(task);
    }

}
