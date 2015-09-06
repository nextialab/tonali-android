package com.nextialab.tonali;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.nextialab.tonali.fragment.ListsFragment;
import com.nextialab.tonali.fragment.TasksFragment;
import com.nextialab.tonali.model.List;
import com.nextialab.tonali.support.ActivityListener;

public class MainActivity extends AppCompatActivity implements ActivityListener {

    enum Section {
        LISTS,
        TASKS
    }

    private Section mCurrentSection = Section.LISTS;

    private ListsFragment mListsFragment;
    private TasksFragment mTasksFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        mListsFragment = new ListsFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.main_container, mListsFragment).commit();
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        getSupportActionBar().setTitle(R.string.title_activity_home);
        mCurrentSection = Section.LISTS;
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

}
