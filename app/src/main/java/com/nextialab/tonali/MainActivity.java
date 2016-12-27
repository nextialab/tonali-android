package com.nextialab.tonali;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.nextialab.tonali.fragment.ListsFragment;
import com.nextialab.tonali.model.TonaliList;
import com.nextialab.tonali.support.ActivityListener;
import com.nextialab.tonali.support.ListsListener;
import com.nextialab.tonali.support.MigrationTester;
import com.nextialab.tonali.support.Persistence;
import com.nextialab.tonali.support.UpdateHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.RunnableFuture;

public class MainActivity extends AppCompatActivity implements ActivityListener {

    private static final String TAG = MainActivity.class.getName();
    private static final String PREFERENCES = MainActivity.class.getName();
    private static final String UPGRADED = MainActivity.class.getName() + "upgraded";

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ProgressDialog mProgressDialog;

    private Stack<ListsListener> mListsStack = new Stack<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setSupportActionBar(toolbar);
        List<TonaliList> lists = TonaliList.findChildren(0L);
        if (lists.size() > 0) {
            TonaliList root = lists.get(0);
            Bundle args = new Bundle();
            args.putLong(ListsFragment.PARENT, root.getId());
            ListsFragment fragment = new ListsFragment();
            fragment.setArguments(args);
            mListsStack.push(fragment);
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragment).commit();
        } else {
            Log.e(TAG, "Root list not found");
        }
        checkIfUpgrade();
    }

    private void checkIfUpgrade() {
        SharedPreferences prefs = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        if (!prefs.getBoolean(UPGRADED, false)) {
            ArrayList<TonaliList> prevLists = Persistence.instance().getLists();
            if (prevLists.size() > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("New version");
                builder.setMessage("Should upgrade previous data to the new version?");
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        upgradeLists();
                    }
                });
                builder.show();
            } else {
                // We just set the flag UPGRADED to true
                SharedPreferences.Editor editor = getSharedPreferences(PREFERENCES, MODE_PRIVATE).edit();
                editor.putBoolean(UPGRADED, true);
                editor.apply();
            }
        }
    }

    private void upgradeLists() {
        mProgressDialog = ProgressDialog.show(this, "", "Upgrading...", true);
        UpdateHelper helper = new UpdateHelper(this);
        helper.setListener(new UpdateHelper.Listener() {
            @Override
            public void onPostExecute() {
                SharedPreferences.Editor editor = getSharedPreferences(PREFERENCES, MODE_PRIVATE).edit();
                editor.putBoolean(UPGRADED, true);
                editor.apply();
                if (mListsStack.size() > 0) {
                    mListsStack.peek().reloadList();
                }
                Log.i(TAG, "Updated");
                if (mProgressDialog != null && mProgressDialog.isShowing()) mProgressDialog.dismiss();
            }
        });
        helper.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();
        if (id == R.id.action_add) {
            onFloatingButton(null);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (mListsStack.size() > 0) {
            mListsStack.pop();
        }
        if (mListsStack.size() == 1) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            mDrawerToggle.setDrawerIndicatorEnabled(true);
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
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle args = new Bundle();
        args.putLong(ListsFragment.PARENT, list.getId());
        ListsFragment fragment = new ListsFragment();
        fragment.setArguments(args);
        mListsStack.push(fragment);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.main_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        getSupportActionBar().setDisplayUseLogoEnabled(false);
    }

}
