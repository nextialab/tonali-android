package com.nextialab.tonali.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.nextialab.tonali.adapter.TasksAdapter;
import com.nextialab.tonali.model.TonaliList;
import com.nextialab.tonali.model.Task;
import com.nextialab.tonali.support.ActivityListener;
import com.nextialab.tonali.support.ItemTouchHelperCallback;
import com.nextialab.tonali.support.Persistence;

import java.util.ArrayList;

/**
 * Created by nigonzalez on 7/11/15.
 */
@Deprecated
public class TasksFragment extends Fragment {

    public static final String LIST = "list";

    private TonaliList mList;

    private TasksAdapter mAdapter = new TasksAdapter(this);
    private ActivityListener mListener = null;
    private Persistence mPersistence;

    public TasksFragment() {

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
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);
        Bundle data = getArguments();
        mList = data.getParcelable(LIST);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mList.getListName());
        RecyclerView recycler = (RecyclerView) view.findViewById(R.id.tasks_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(mAdapter);
        ItemTouchHelperCallback callback = new ItemTouchHelperCallback();
        callback.setListener(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recycler);
        loadTasks();
        return view;
    }

    public void loadTasks() {

    }

    private void onNewTask(String taskName) {

    }

    private void onUpdateTask(Task task, String name) {
        if (mPersistence.updateTaskName(task.getId(), name)) {
            task.setTask(name);
            mAdapter.notifyDataSetChanged();
        } else {
            Log.e("TaskFragment", "Could not update task name");
        }
    }

    public void onNewTask() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View input = getActivity().getLayoutInflater().inflate(R.layout.new_task_layout, null);
        final EditText editText = (EditText) input.findViewById(R.id.lists_input_new_task);
        builder.setView(input);
        builder.setPositiveButton(R.string.lists_create_list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String taskName = editText.getText().toString();
                if (taskName.length() > 0) {
                    onNewTask(taskName);
                    dialog.dismiss();
                }
            }
        });
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
        dialog.show();
    }

    public void onUpdateTask(final Task task) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View input = getActivity().getLayoutInflater().inflate(R.layout.dialog_task_update, null);
        final EditText editText = (EditText) input.findViewById(R.id.lists_input_update_task);
        editText.setText(task.getTask());
        editText.setSelection(task.getTask().length());
        builder.setView(input);
        builder.setPositiveButton(R.string.lists_update_list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String taskName = editText.getText().toString();
                if (taskName.length() > 0) {
                    onUpdateTask(task, taskName);
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

    public void goToTask(Task task) {
        task.setList(mList.getListName());
        //mListener.goToTask(task);
    }

}
