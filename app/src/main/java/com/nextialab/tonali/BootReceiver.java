package com.nextialab.tonali;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.nextialab.tonali.model.Task;
import com.nextialab.tonali.support.Persistence;
import com.nextialab.tonali.support.TonaliAlarmManager;

import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * Created by Nelson on 9/20/2015.
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Persistence persistence = new Persistence(context);
        ArrayList<Task> tasksWithAlarm = persistence.getTasksWithAlarm();
        for (Task task : tasksWithAlarm) {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(task.getNotification());
            TonaliAlarmManager.setAlarmForTask(context, task, calendar);
        }
    }

}
