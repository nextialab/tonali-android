package com.nextialab.tonali;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.nextialab.tonali.fragment.DetailsFragment;
import com.nextialab.tonali.fragment.ListsFragment;
import com.nextialab.tonali.fragment.TasksFragment;
import com.nextialab.tonali.model.List;
import com.nextialab.tonali.model.Task;
import com.nextialab.tonali.support.ActivityListener;
import com.nextialab.tonali.support.Persistence;

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements ActivityListener {

    public static final String GENERAL = "general";
    public static final String GENERAL_FIRST_TIME = "firstTime";
    public static final String EDIT_FIELD = "editField";

    enum Section {
        LISTS,
        TASKS,
        DETAILS
    }

    private Section mCurrentSection = Section.LISTS;

    private ListsFragment mListsFragment;
    private TasksFragment mTasksFragment;
    private DetailsFragment mDetailsFragment;

    private FloatingActionButton mFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.floating_button);
        setSupportActionBar(toolbar);
        mListsFragment = new ListsFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.main_container, mListsFragment).commit();
        SharedPreferences prefs = getSharedPreferences(GENERAL, MODE_PRIVATE);
        if (prefs.getBoolean(GENERAL_FIRST_TIME, true)) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(GENERAL_FIRST_TIME, false);
            editor.commit();
            startActivity(new Intent(this, AboutActivity.class));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        switch (mCurrentSection) {
            case DETAILS:
                mFloatingActionButton.setVisibility(View.VISIBLE);
                mCurrentSection = Section.TASKS;
                break;
            case TASKS:
                getSupportActionBar().setTitle(R.string.title_activity_home);
                mCurrentSection = Section.LISTS;
                break;
        }
        super.onBackPressed();
    }

    public void onFloatingButton(View view) {
        switch (mCurrentSection) {
        case LISTS:
            mListsFragment.onNewList();
            break;
        case TASKS:
            mTasksFragment.onNewTask();
            break;
        }
    }

    @Override
    public void goToList(List list) {
        Bundle data = new Bundle();
        data.putParcelable(TasksFragment.LIST, list);
        mTasksFragment = new TasksFragment();
        mTasksFragment.setArguments(data);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.main_container, mTasksFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        mCurrentSection = Section.TASKS;
    }

    @Override
    public void goToTask(Task task) {
        Bundle data = new Bundle();
        data.putParcelable(DetailsFragment.TASK, task);
        mDetailsFragment = new DetailsFragment();
        mDetailsFragment.setArguments(data);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.main_container, mDetailsFragment);
        transaction.addToBackStack(null);
        mFloatingActionButton.setVisibility(View.INVISIBLE);
        transaction.commit();
        mCurrentSection = Section.DETAILS;
    }

}
