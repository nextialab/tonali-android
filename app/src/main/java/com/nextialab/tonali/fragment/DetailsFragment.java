package com.nextialab.tonali.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.nextialab.tonali.EditActivity;
import com.nextialab.tonali.MainActivity;
import com.nextialab.tonali.R;
import com.nextialab.tonali.model.Task;
import com.nextialab.tonali.support.Persistence;
import com.nextialab.tonali.support.TonaliAlarmManager;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Nelson on 9/17/2015.
 */
@Deprecated
public class DetailsFragment extends Fragment {

    public static final String TASK = "task";
    public static final int REQUEST_CODE = 0;

    private Persistence mPersistence = null;
    private GregorianCalendar mNotification = null;
    private ImageView mAlarm = null;
    private TextView mDate = null;
    private TextView mTime = null;
    private TextView mDescription = null;

    private Task mTask;

    @Override
    public void onAttach (Activity activity) {
        super.onAttach(activity);
        mPersistence = new Persistence(activity);
    }

    View.OnClickListener mOnDateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDatePicker(mNotification.get(Calendar.YEAR), mNotification.get(Calendar.MONTH), mNotification.get(Calendar.DAY_OF_MONTH));
        }
    };

    View.OnClickListener mOnTimeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showTimePicker(mNotification.get(Calendar.HOUR_OF_DAY), mNotification.get(Calendar.MINUTE));
        }
    };

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        mTask = getArguments().getParcelable(TASK);
        ((TextView) view.findViewById(R.id.task_name)).setText(mTask.getTask());
        mAlarm = (ImageView) view.findViewById(R.id.notification_active);
        mDate = (TextView) view.findViewById(R.id.notification_date);
        mTime = (TextView) view.findViewById(R.id.notification_time);
        mDescription = (TextView) view.findViewById(R.id.task_description);
        if (mTask.getDescription().length() > 0) {
            mDescription.setText(mTask.getDescription());
            mDescription.setTextColor(getResources().getColor(R.color.tonali_black));
        }
        mNotification = new GregorianCalendar();
        if (mTask.getNotification().getTime() == 0) {
            mNotification.set(Calendar.HOUR_OF_DAY, 6);
            mNotification.set(Calendar.MINUTE, 0);
        } else {
            mNotification.setTime(mTask.getNotification());
        }
        setDateTime();
        if (mTask.hasAlarm()) {
            mAlarm.setImageResource(R.mipmap.ic_notifications_active_black_24dp);
            mDate.setTextColor(getResources().getColor(R.color.tonali_black));
            mTime.setTextColor(getResources().getColor(R.color.tonali_black));
        } else {
            mDate.setOnClickListener(mOnDateClickListener);
            mTime.setOnClickListener(mOnTimeClickListener);
        }
        mDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditDescription();
            }
        });
        mAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarm(!mTask.hasAlarm());
            }
        });
        return view;
    }

    private void setDateTime() {
        StringBuilder today = new StringBuilder();
        today.append(mNotification.get(Calendar.DAY_OF_MONTH));
        today.append('/');
        today.append(mNotification.get(Calendar.MONTH) + 1);
        today.append('/');
        today.append(mNotification.get(Calendar.YEAR));
        mDate.setText(today.toString());
        StringBuilder time = new StringBuilder();
        int hour = mNotification.get(Calendar.HOUR);
        if (hour > 9) {
            time.append(hour);
        } else {
            time.append('0');
            time.append(hour);
        }
        time.append(':');
        int minute = mNotification.get(Calendar.MINUTE);
        if (minute > 9) {
            time.append(minute);
        } else {
            time.append('0');
            time.append(minute);
        }
        switch (mNotification.get(Calendar.AM_PM)) {
            case Calendar.AM:
                time.append(" AM");
                break;
            case Calendar.PM:
                time.append(" PM");
                break;
        }
        mTime.setText(time.toString());
    }

    private void saveNotification() {
        if (!mPersistence.setTaskNotification(mTask.getId(), mNotification.getTime())) {
            Log.e("Details", "Could not set notification time");
        }
    }

    private void setAlarm(boolean alarm) {
        if (mPersistence.setTaskAlarm(mTask.getId(), alarm)) {
            mTask.setAlarm(alarm);
            if (alarm) {
                mAlarm.setImageResource(R.mipmap.ic_notifications_active_black_24dp);
                mDate.setTextColor(getResources().getColor(R.color.tonali_black));
                mTime.setTextColor(getResources().getColor(R.color.tonali_black));
                setNotificationAlarm();
                mDate.setOnClickListener(null);
                mTime.setOnClickListener(null);
            } else {
                mAlarm.setImageResource(R.mipmap.ic_notifications_none_black_24dp);
                mDate.setTextColor(getResources().getColor(R.color.tonali_gray));
                mTime.setTextColor(getResources().getColor(R.color.tonali_gray));
                removeNotificationAlarm();
                mDate.setOnClickListener(mOnDateClickListener);
                mTime.setOnClickListener(mOnTimeClickListener);
            }
        } else {
            Log.e("Details", "Could not set alarm");
        }
    }

    private void setNotificationAlarm() {
        TonaliAlarmManager.setAlarmForTask(getActivity(), mTask, mNotification);
        String message = getString(R.string.notification_set) + " " + mDate.getText().toString() + " " + mTime.getText().toString();
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void removeNotificationAlarm() {
        TonaliAlarmManager.removeAlarmForTask(getActivity(), mTask);
        Toast.makeText(getActivity(), getString(R.string.notification_removed), Toast.LENGTH_SHORT).show();
    }

    private void onEditDescription() {
        Intent intent = new Intent(getActivity(), EditActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
        }
    }

    private DatePickerDialog.OnDateSetListener mDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mNotification.set(Calendar.YEAR, year);
            mNotification.set(Calendar.MONTH, monthOfYear);
            mNotification.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setDateTime();
            saveNotification();
        }
    };

    private void showDatePicker(int year, int month, int day) {
        DatePickerDialog datePicker = new DatePickerDialog(getActivity(), mDateListener, year, month, day);
        datePicker.show();
    }

    private TimePickerDialog.OnTimeSetListener mTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mNotification.set(Calendar.HOUR_OF_DAY, hourOfDay);
            mNotification.set(Calendar.MINUTE, minute);
            setDateTime();
            saveNotification();
        }
    };

    private void showTimePicker(int hour, int minute) {
        TimePickerDialog timePicker = new TimePickerDialog(getActivity(), mTimeListener, hour, minute, false);
        timePicker.show();
    }

}
