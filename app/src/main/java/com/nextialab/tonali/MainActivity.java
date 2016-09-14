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

import com.nextialab.tonali.fragment.DetailsFragment;
import com.nextialab.tonali.fragment.ListsFragment;
import com.nextialab.tonali.fragment.TasksFragment;
import com.nextialab.tonali.model.TonaliList;
import com.nextialab.tonali.model.Task;
import com.nextialab.tonali.support.ActivityListener;
import com.nextialab.tonali.support.ListsListener;

import java.util.List;
import java.util.Stack;

public class MainActivity extends AppCompatActivity implements ActivityListener {

    private static final String TAG = MainActivity.class.getName();

    private Stack<ListsListener> mListsStack = new Stack<>();

    private FloatingActionButton mFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.floating_button);
        setSupportActionBar(toolbar);
        List<TonaliList> lists = TonaliList.findChildren(0L);
        if (lists.size() > 0) {
            TonaliList root = lists.get(0);
            Bundle args = new Bundle();
            args.putParcelable(ListsFragment.PARENT, root);
            ListsFragment fragment = new ListsFragment();
            fragment.setArguments(args);
            mListsStack.push(fragment);
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragment).commit();
        } else {
            Log.e(TAG, "Root list not found");
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
        if (mListsStack.size() > 0) {
            mListsStack.pop();
        }
        super.onBackPressed();
    }

    public void onFloatingButton(View view) {
        if (mListsStack.size() > 0) {
            mListsStack.peek().onNewList();
        }
    }

    @Override
    public void goToList(TonaliList list) {
        Bundle args = new Bundle();
        args.putParcelable(ListsFragment.PARENT, list);
        ListsFragment fragment = new ListsFragment();
        fragment.setArguments(args);
        mListsStack.push(fragment);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.main_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void goToFinal(TonaliList list) {

    }

}
