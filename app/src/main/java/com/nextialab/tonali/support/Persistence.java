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
        Cursor cursor = db.rawQuery("SELECT l.id id, l.list list, l.cleared cleared, COUNT(t.id) tasks FROM lists l LEFT JOIN tasks t ON l.id = t.list AND t.done = 0 GROUP BY l.id", null);
        while (cursor.moveToNext()) {
            if (cursor.getInt(cursor.getColumnIndex("cleared")) == 0) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String listName = cursor.getString(cursor.getColumnIndex("list"));
                int tasks = cursor.getInt(cursor.getColumnIndex("tasks"));
                List list = new List(listName, id);
                list.setTasksCounter(tasks);
                lists.add(list);
            }
        }
        cursor.close();
        return lists;
    }

    public ArrayList<Integer> getListsOrder() {
        ArrayList<Integer> order = null;
        SQLiteDatabase db = new SqlHelper(mContext).getReadableDatabase();
        String[] columns = {"_order"};
        Cursor cursor = db.query(SqlHelper.ORDERS_TABLE,
                columns,
                "list=-1",
                null,
                null,
                null,
                null);
        if (cursor.getCount() > 0) {
            order = new ArrayList<>();
            cursor.moveToFirst();
            String strOrder = cursor.getString(cursor.getColumnIndex("_order"));
            if (strOrder.length() > 0) {
                String[] lists = strOrder.split(",");
                Log.i("Persistence", "Lists order " + strOrder + " size " + lists.length);
                for (String list : lists) {
                    order.add(Integer.parseInt(list));
                }
            }
        }
        cursor.close();
        return order;
    }

    public boolean createListsOrder(ArrayList<Integer> order) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < order.size(); ++i) {
            buffer.append(order.get(i));
            if (i < order.size() - 1) {
                buffer.append(',');
            }
        }
        Date today = new Date();
        ContentValues entry = new ContentValues();
        entry.put("list", -1);
        entry.put("_order", buffer.toString());
        entry.put("created", today.getTime());
        entry.put("modified", today.getTime());
        SQLiteDatabase db = new SqlHelper(mContext).getWritableDatabase();
        long id = db.insert(SqlHelper.ORDERS_TABLE, null, entry);
        if (id > -1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean updateListsOrder(ArrayList<Integer> order) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < order.size(); ++i) {
            buffer.append(order.get(i));
            if (i < order.size() - 1) {
                buffer.append(',');
            }
        }
        Date today = new Date();
        ContentValues entry = new ContentValues();
        entry.put("_order", buffer.toString());
        entry.put("modified", today.getTime());
        SQLiteDatabase db = new SqlHelper(mContext).getWritableDatabase();
        int rows = db.update(SqlHelper.ORDERS_TABLE, entry, "list=-1", null);
        return rows > 0;
    }

    public ArrayList<Task> getTasksWithAlarm() {
        ArrayList<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = new SqlHelper(mContext).getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT t.id id, l.list list, t.task task, t.notification notification FROM tasks t JOIN lists l ON t.list = l.id AND t.alarm = 1 AND t.done = 0", null);
        while (cursor.moveToNext()) {
            long notification = cursor.getLong(cursor.getColumnIndex("notification"));
            Date today = new Date();
            if (notification > today.getTime()) {
                Task task = new Task();
                task.setId(cursor.getInt(cursor.getColumnIndex("id")));
                task.setList(cursor.getString(cursor.getColumnIndex("list")));
                task.setTask(cursor.getString(cursor.getColumnIndex("task")));
                task.setNotification(new Date(notification));
                tasks.add(task);
            }
        }
        return tasks;
    }

    public ArrayList<Task> getTasksForList(int list) {
        ArrayList<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = new SqlHelper(mContext).getReadableDatabase();
        String[] columns = {"id", "task", "description", "done", "alarm", "notification", "created"};
        String[] args = new String[]{Integer.toString(list)};
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
            task.setDone(done > 0);
            int alarm = cursor.getInt(cursor.getColumnIndex("alarm"));
            task.setAlarm(alarm > 0);
            task.setNotification(new Date(cursor.getLong(cursor.getColumnIndex("notification"))));
            task.setCreated(new Date(cursor.getLong(cursor.getColumnIndex("created"))));
            tasks.add(task);
        }
        cursor.close();
        return tasks;
    }

    public ArrayList<Integer> getListOrder(int list) {
        ArrayList<Integer> order = null;
        SQLiteDatabase db = new SqlHelper(mContext).getReadableDatabase();
        String[] columns = {"_order"};
        String[] args = new String[]{Integer.toString(list)};
        Cursor cursor = db.query(SqlHelper.ORDERS_TABLE,
                columns,
                "list=?",
                args,
                null,
                null,
                null);
        if (cursor.getCount() > 0) {
            order = new ArrayList<>();
            cursor.moveToFirst();
            String strOrder = cursor.getString(cursor.getColumnIndex("order"));
            String[] lists = strOrder.split(",");
            for (String tList : lists) {
                order.add(Integer.parseInt(tList));
            }
        }
        cursor.close();
        return order;
    }

    public boolean createListOrder(int list, ArrayList<Integer> order) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < order.size(); ++i) {
            buffer.append(order.get(i));
            if (i < order.size() - 1) {
                buffer.append(',');
            }
        }
        Date today = new Date();
        ContentValues entry = new ContentValues();
        entry.put("list", list);
        entry.put("_order", buffer.toString());
        entry.put("created", today.getTime());
        entry.put("modified", today.getTime());
        SQLiteDatabase db = new SqlHelper(mContext).getWritableDatabase();
        long id = db.insert(SqlHelper.ORDERS_TABLE, null, entry);
        if (id > -1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean updateListOrder(int list, ArrayList<Integer> order) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < order.size(); ++i) {
            buffer.append(order.get(i));
            if (i < order.size() - 1) {
                buffer.append(',');
            }
        }
        Date today = new Date();
        ContentValues entry = new ContentValues();
        entry.put("_order", buffer.toString());
        entry.put("modified", today.getTime());
        SQLiteDatabase db = new SqlHelper(mContext).getWritableDatabase();
        int rows = db.update(SqlHelper.ORDERS_TABLE, entry, "list=?", new String[]{Integer.toString(list)});
        return rows > 0;
    }

    public List createNewList(String name) {
        List list = null;
        SQLiteDatabase db = new SqlHelper(mContext).getWritableDatabase();
        Date today = new Date();
        ContentValues entry = new ContentValues();
        entry.put("list", name);
        entry.put("type", 0);
        entry.put("cleared", 0);
        entry.put("created", today.getTime());
        entry.put("modified", today.getTime());
        long id = db.insert(SqlHelper.LISTS_TABLE, null, entry);
        if (id > -1) {
            list = new List(name, (int)id);
            list.setTasksCounter(0);
        }
        return list;
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
        entry.put("alarm", 0);
        entry.put("notification", 0);
        entry.put("created", today.getTime());
        entry.put("modified", today.getTime());
        long id = db.insert(SqlHelper.TASKS_TABLE, null, entry);
        if (id > -1) {
            task = new Task();
            task.setId((int)id);
            task.setTask(name);
            task.setDescription("");
            task.setDone(false);
            task.setAlarm(false);
            task.setNotification(new Date(0));
            task.setCreated(today);
        }
        return task;
    }

    public boolean updateListName(int list, String name) {
        SQLiteDatabase db = new SqlHelper(mContext).getWritableDatabase();
        Date today = new Date();
        ContentValues entry = new ContentValues();
        entry.put("list", name);
        entry.put("modified", today.getTime());
        String[] args = new String[1];
        args[0] = Integer.toString(list);
        int rows = db.update(SqlHelper.LISTS_TABLE, entry, "id=?", args);
        if (rows > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean updateTaskName(int task, String name) {
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

    public boolean setListCleared(int list) {
        SQLiteDatabase db = new SqlHelper(mContext).getWritableDatabase();
        Date today = new Date();
        ContentValues entry = new ContentValues();
        entry.put("cleared", 1);
        entry.put("modified", today.getTime());
        String[] args = new String[1];
        args[0] = Integer.toString(list);
        int rows = db.update(SqlHelper.LISTS_TABLE, entry, "id=?", args);
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

    public boolean updateTaskDescription(int task, String description) {
        SQLiteDatabase db = new SqlHelper(mContext).getWritableDatabase();
        Date today = new Date();
        ContentValues entry = new ContentValues();
        entry.put("description", description);
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

    public boolean setTaskNotification(int task, Date notification) {
        SQLiteDatabase db = new SqlHelper(mContext).getWritableDatabase();
        Date today = new Date();
        ContentValues entry = new ContentValues();
        entry.put("notification", notification.getTime());
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

    public boolean setTaskAlarm(int task, boolean alarm) {
        SQLiteDatabase db = new SqlHelper(mContext).getWritableDatabase();
        Date today = new Date();
        ContentValues entry = new ContentValues();
        entry.put("alarm", alarm ? 1 : 0);
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
