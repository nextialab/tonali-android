package com.nextialab.tonali.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nextialab.tonali.R;
import com.nextialab.tonali.model.Task;
import com.nextialab.tonali.support.Persistence;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Nelson on 9/17/2015.
 */
public class DetailsFragment extends Fragment {

    public static final String TASK = "task";

    private Persistence mPersistence = null;

    private Task mTask;

    @Override
    public void onAttach (Activity activity) {
        super.onAttach(activity);
        mPersistence = new Persistence(activity);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        mTask = getArguments().getParcelable(TASK);
        ((TextView) view.findViewById(R.id.task_name)).setText(mTask.getTask());
        if (mTask.getDescription().length() > 0) {
            ((TextView) view.findViewById(R.id.task_description)).setText(mTask.getDescription());
        }
        if (mTask.getNotification().getTime() == 0) {
            GregorianCalendar datetime = new GregorianCalendar();
            StringBuilder today = new StringBuilder();
            today.append(datetime.get(Calendar.DAY_OF_MONTH));
            today.append('/');
            today.append(datetime.get(Calendar.MONTH) + 1);
            today.append('/');
            today.append(datetime.get(Calendar.YEAR));
            ((TextView) view.findViewById(R.id.notification_date)).setText(today.toString());
            ((TextView) view.findViewById(R.id.notification_time)).setText("06:00");
        }
        return view;
    }

}
