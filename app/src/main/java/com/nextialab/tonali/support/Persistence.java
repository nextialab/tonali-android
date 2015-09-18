package com.nextialab.tonali.support;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.nextialab.tonali.model.List;
import com.nextialab.tonali.model.Task;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by nigonzalez on 7/19/15.
 */
public class Persistence {

    private Context mContext;

    public Persistence(Context context) {
        mContext = context;
    }

    public ArrayList<List> getListsWithCount() {
        ArrayList<List> lists = new ArrayList<>();
        SQLiteDatabase db = new SqlHelper(mContext).getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT l.id id, l.list list, COUNT(t.id) tasks FROM lists l LEFT JOIN tasks t ON l.id = t.list AND t.done = 0 AND t.cleared = 0 GROUP BY l.id", null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String listName = cursor.getString(cursor.getColumnIndex("list"));
            int tasks = cursor.getInt(cursor.getColumnIndex("tasks"));
            List list = new List(listName, id);
            list.setTasksCounter(tasks);
            lists.add(list);
        }
        cursor.close();
        return lists;
    }

    // TODO: find a way to order by order and done in sqlite for Android
    public ArrayList<Task> getTasksForList(int list) {
        ArrayList<Task> tasks = new ArrayList<>();
        ArrayList<Task> tasksDone = new ArrayList<>();
        SQLiteDatabase db = new SqlHelper(mContext).getReadableDatabase();
        String[] columns = {"id", "task", "description", "done", "notification", "created"};
        String[] args = new String[1];
        args[0] = Integer.toString(list);
        Cursor cursor = db.query(SqlHelper.TASKS_TABLE,
                columns,
                "list=? AND cleared=0",
                args,
                null,
                null,
                "created DESC");
        while (cursor.moveToNext()) {
            Task task = new Task();
            task.setId(cursor.getInt(cursor.getColumnIndex("id")));
            task.setTask(cursor.getString(cursor.getColumnIndex("task")));
            task.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            int done = cursor.getInt(cursor.getColumnIndex("done"));
            task.setDone(done != 0);
            task.setNotification(new Date(cursor.getLong(cursor.getColumnIndex("notification"))));
            task.setCreated(new Date(cursor.getLong(cursor.getColumnIndex("created"))));
            if (task.isDone()) {
                tasksDone.add(task);
            } else {
                tasks.add(task);
            }
        }
        tasks.addAll(tasksDone);
        cursor.close();
        return tasks;
    }

    public boolean createList(String name) {
        SQLiteDatabase db = new SqlHelper(mContext).getWritableDatabase();
        Date today = new Date();
        ContentValues entry = new ContentValues();
        entry.put("list", name);
        entry.put("type", 0);
        entry.put("cleared", 0);
        entry.put("created", today.getTime());
        entry.put("modified", today.getTime());
        long id = db.insert(SqlHelper.LISTS_TABLE, null, entry);
        if (id == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Task createNewTask(String name, int list) {
        Task task = null;
        Date today = new Date();
        SQLiteDatabase db = new SqlHelper(mContext).getWritableDatabase();
        ContentValues entry = new ContentValues();
        entry.put("task", name);
        entry.put("description", "");
        entry.put("list", list);
        entry.put("done", 0);
        entry.put("cleared", 0);
        entry.put("notification", 0);
        entry.put("created", today.getTime());
        entry.put("modified", today.getTime());
        long id = db.insert(SqlHelper.TASKS_TABLE, null, entry);
        if (id > -1) {
            task = new Task();
            task.setId((int)id);
            task.setTask(name);
            task.setDone(false);
            task.setCreated(today);
        }
        return task;
    }

    public boolean updateTask(int task, String name) {
        SQLiteDatabase db = new SqlHelper(mContext).getWritableDatabase();
        Date today = new Date();
        ContentValues entry = new ContentValues();
        entry.put("task", name);
        entry.put("modified", today.getTime());
        String[] args = new String[1];
        args[0] = Integer.toString(task);
        int rows = db.update(SqlHelper.TASKS_TABLE, entry, "id=?", args);
        if (rows > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean setTaskDone(int task, boolean done) {
        SQLiteDatabase db = new SqlHelper(mContext).getWritableDatabase();
        Date today = new Date();
        ContentValues entry = new ContentValues();
        entry.put("done", done ? 1 : 0);
        entry.put("modified", today.getTime());
        String[] args = new String[1];
        args[0] = Integer.toString(task);
        int rows = db.update(SqlHelper.TASKS_TABLE, entry, "id=?", args);
        if (rows > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean setTaskCleared(int task) {
        SQLiteDatabase db = new SqlHelper(mContext).getWritableDatabase();
        Date today = new Date();
        ContentValues entry = new ContentValues();
        entry.put("done", 1);
        entry.put("cleared", 1);
        entry.put("modified", today.getTime());
        String[] args = new String[1];
        args[0] = Integer.toString(task);
        int rows = db.update(SqlHelper.TASKS_TABLE, entry, "id=?", args);
        if (rows > 0) {
            return true;
        } else {
            return false;
        }
    }

}
