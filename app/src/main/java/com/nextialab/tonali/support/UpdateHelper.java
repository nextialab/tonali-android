package com.nextialab.tonali.support;

import android.content.Context;
import android.os.AsyncTask;

import com.nextialab.tonali.model.TonaliList;

import java.util.ArrayList;


/**
 * Created by Nelson on 10/18/2015.
 */
public class UpdateHelper extends AsyncTask<Void, Void, Void> {

    private Context mContext;

    public UpdateHelper(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Persistence persistence = new Persistence(mContext);
        ArrayList<TonaliList> lists = persistence.getListsWithCount();
        int prev = -1;
        int i = 0;
        for (TonaliList list : lists) {

            i++;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {

    }

}
