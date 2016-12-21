package com.nextialab.tonali;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.nextialab.tonali.fragment.ListsFragment;
import com.nextialab.tonali.model.TonaliList;
import com.nextialab.tonali.support.ActivityListener;
import com.nextialab.tonali.support.ListsListener;
import com.nextialab.tonali.support.UpdateHelper;

import java.util.List;
import java.util.Stack;

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
            args.putParcelable(ListsFragment.PARENT, root);
            ListsFragment fragment = new ListsFragment();
            fragment.setArguments(args);
            mListsStack.push(fragment);
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragment).commit();
        } else {
            Log.e(TAG, "Root list not found");
        }
        SharedPreferences prefs = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        if (!prefs.getBoolean(UPGRADED, false)) {
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
                    if (mProgressDialog != null && mProgressDialog.isShowing()) mProgressDialog.dismiss();
                }
            });
            helper.execute();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
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
        args.putParcelable(ListsFragment.PARENT, list);
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
