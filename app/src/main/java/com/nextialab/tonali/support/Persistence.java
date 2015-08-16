package com.nextialab.tonali.support;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nextialab.tonali.model.List;

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

    public ArrayList<List> getLists() {
        ArrayList<List> lists = new ArrayList<>();
        SQLiteDatabase db = new SqlHelper(mContext).getReadableDatabase();
        String[] columns = {"list", "id"};
        Cursor cursor = db.query(SqlHelper.LISTS_TABLE,
                columns,
                null,
                null,
                null,
                null,
                null,
                null);
        while (cursor.moveToNext()) {
            String listName = cursor.getString(cursor.getColumnIndex("list"));
            int listId = cursor.getInt(cursor.getColumnIndex("id"));
            List list = new List(listName, listId);
            lists.add(list);
        }
        cursor.close();
        return lists;
    }


    public ArrayList<String> getTasksForList(int list) {
        ArrayList<String> tasks = new ArrayList<>();
        SQLiteDatabase db = new SqlHelper(mContext).getReadableDatabase();
        String[] columns = {"task"};
        String[] args = new String[1];
        args[0] = Integer.toString(list);
        Cursor cursor = db.query(SqlHelper.TASKS_TABLE,
                columns,
                "list=?",
                args,
                null,
                null,
                null);
        while (cursor.moveToNext()) {
            String task = cursor.getString(cursor.getColumnIndex("task"));
            tasks.add(task);
        }
        cursor.close();
        return tasks;
    }

    public boolean createList(String name) {
        SQLiteDatabase db = new SqlHelper(mContext).getWritableDatabase();
        Date today = new Date(0);
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

    public boolean createTask(String name, int list) {
        SQLiteDatabase db = new SqlHelper(mContext).getWritableDatabase();
        Date today = new Date(0);
        ContentValues entry = new ContentValues();
        entry.put("task", name);
        entry.put("list", list);
        entry.put("done", 0);
        entry.put("cleared", 0);
        entry.put("created", today.getTime());
        entry.put("modified", today.getTime());
        long id = db.insert(SqlHelper.TASKS_TABLE, null, entry);
        if (id == -1) {
            return false;
        } else {
            return true;
        }
    }

}
