package com.nextialab.tonali.support;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.nextialab.tonali.AlarmReceiver;
import com.nextialab.tonali.model.Task;

import java.util.Calendar;

/**
 * Created by Nelson on 9/20/2015.
 */
public class TonaliAlarmManager {

    public static final String TONALI_INTENT_ALARM = "com.nextialab.tonali.AlarmReceiver";
    public static final String LIST_NAME = "listName";
    public static final String TASK_NAME = "taskName";
    public static final String TASK_ID = "taskId";

    public static void setAlarmForTask(Context context, Task task, Calendar notification) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(TONALI_INTENT_ALARM);
        intent.putExtra(TASK_ID, task.getId());
        intent.putExtra(LIST_NAME, task.getList());
        intent.putExtra(TASK_NAME, task.getTask());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, task.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, notification.getTimeInMillis(), pendingIntent);
    }

    public static void removeAlarmForTask(Context context, Task task) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(TONALI_INTENT_ALARM);
        intent.putExtra(TASK_ID, task.getId());
        intent.putExtra(LIST_NAME, task.getList());
        intent.putExtra(TASK_NAME, task.getTask());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, task.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

}
