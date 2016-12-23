package com.nextialab.tonali.support;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.nextialab.tonali.model.Task;
import com.nextialab.tonali.model.TonaliList;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Nelson on 10/18/2015.
 */
public class UpdateHelper extends AsyncTask<Void, Void, Void> {

    private static final String TAG = UpdateHelper.class.getName();

    public interface Listener {
        void onPostExecute();
    }

    private Context mContext;
    private Listener mListener;

    public UpdateHelper(Context context) {
        mContext = context;
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    @Override
    protected Void doInBackground(Void... params) {
        List<TonaliList> roots = TonaliList.findChildren(0L);
        if (roots.size() > 0) {
            TonaliList root = roots.get(0);
            Persistence persistence = new Persistence(mContext);
            ArrayList<TonaliList> lists = persistence.getLists();
            for (TonaliList list : lists) {
                ArrayList<Task> tasks = persistence.getTasksForList(list.getId());
                list.setParent(1L);
                list.setId(-1);
                list.save();
                root.getSequence().add(list.getId());
                long parentId = list.getId();
                for (Task task : tasks) {
                    TonaliList taskToList = task.toList(parentId);
                    taskToList.save();
                    list.getSequence().add(taskToList.getId());
                }
                list.save();
            }
            root.save();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        if (mListener != null) {
            mListener.onPostExecute();
        }
    }

}
