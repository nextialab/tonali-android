package com.nextialab.tonali.support;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.nextialab.tonali.model.TonaliList;
import com.nextialab.tonali.model.Task;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by nigonzalez on 7/19/15.
 */
public class Persistence {

    private static Persistence sInstance;

    private Persistence() {

    }

    public static Persistence instance() {
        if (sInstance == null) {
            sInstance = new Persistence();
        }
        return sInstance;
    }

    // Instance

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public void setContext(Context context) {
        mContext = context;
    }

    public void beginTransaction() {
        mDatabase = new SqlHelper2(mContext).getWritableDatabase();
    }

    public void endTransaction() {
        if (mDatabase != null) {
            mDatabase.close();
        }
        mDatabase = null;
    }

    public long insert(String table, ContentValues row) {
        return mDatabase.insert(table, null, row);
    }

    public boolean update(String table, ContentValues row, String where, String[] args) {
        int rows = mDatabase.update(table, row, where, args);
        return rows > 0;
    }

    public boolean delete(String table, String where, String[] args) {
        int rows = mDatabase.delete(table, where, args);
        return rows > 0;
    }

    public Cursor getRows(String table, String[] columns, String where, String[] args) {
        return mDatabase.query(table, columns, where, args, null, null, null);
    }

    // Deprecated

    @Deprecated
    public Persistence(Context context) {
        mContext = context;
    }

    @Deprecated
    public ArrayList<TonaliList> getLists() {
        ArrayList<TonaliList> lists = new ArrayList<>();
        SQLiteDatabase db = new SqlHelper(mContext).getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM lists", null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String listName = cursor.getString(cursor.getColumnIndex("list"));
            TonaliList list = new TonaliList(listName, id);
            list.setIsChecked(cursor.getInt(cursor.getColumnIndex("cleared")) == 1);
            lists.add(list);
        }
        cursor.close();
        db.close();
        return lists;
    }

    @Deprecated
    public ArrayList<TonaliList> getListsWithCount() {
        ArrayList<TonaliList> lists = new ArrayList<>();
        SQLiteDatabase db = new SqlHelper(mContext).getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT l.id id, l.list list, l.cleared cleared, COUNT(t.id) tasks FROM lists l LEFT JOIN tasks t ON l.id = t.list AND t.done = 0 GROUP BY l.id", null);
        while (cursor.moveToNext()) {
            if (cursor.getInt(cursor.getColumnIndex("cleared")) == 0) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String listName = cursor.getString(cursor.getColumnIndex("list"));
                int tasks = cursor.getInt(cursor.getColumnIndex("tasks"));
                TonaliList list = new TonaliList(listName, id);
                lists.add(list);
            }
        }
        cursor.close();
        db.close();
        return lists;
    }

    @Deprecated
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
        db.close();
        return order;
    }

    @Deprecated
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
        db.close();
        if (id > -1) {
            return true;
        } else {
            return false;
        }
    }

    @Deprecated
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
        db.close();
        return rows > 0;
    }

    @Deprecated
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
        db.close();
        return tasks;
    }

    @Deprecated
    public ArrayList<Task> getTasksForList(long list) {
        ArrayList<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = new SqlHelper(mContext).getReadableDatabase();
        String[] columns = {"id", "task", "description", "done", "alarm", "notification", "created"};
        String[] args = new String[]{Long.toString(list)};
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
            task.setListId((int)list);
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
        db.close();
        return tasks;
    }

    @Deprecated
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
            String strOrder = cursor.getString(cursor.getColumnIndex("_order"));
            if (strOrder.length() > 0) {
                String[] lists = strOrder.split(",");
                for (String tList : lists) {
                    order.add(Integer.parseInt(tList));
                }
            }
        }
        cursor.close();
        db.close();
        return order;
    }

    @Deprecated
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
        db.close();
        if (id > -1) {
            return true;
        } else {
            return false;
        }
    }

    @Deprecated
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
        db.close();
        return rows > 0;
    }

    @Deprecated
    public TonaliList createNewList(String name) {
        TonaliList list = null;
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
            list = new TonaliList(name, (int)id);
        }
        db.close();
        return list;
    }

    @Deprecated
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
            task.setListId(list);
            task.setDescription("");
            task.setDone(false);
            task.setAlarm(false);
            task.setNotification(new Date(0));
            task.setCreated(today);
        }
        db.close();
        return task;
    }

    @Deprecated
    public boolean updateListName(int list, String name) {
        SQLiteDatabase db = new SqlHelper(mContext).getWritableDatabase();
        Date today = new Date();
        ContentValues entry = new ContentValues();
        entry.put("list", name);
        entry.put("modified", today.getTime());
        String[] args = new String[1];
        args[0] = Integer.toString(list);
        int rows = db.update(SqlHelper.LISTS_TABLE, entry, "id=?", args);
        db.close();
        if (rows > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Deprecated
    public boolean updateTaskName(int task, String name) {
        SQLiteDatabase db = new SqlHelper(mContext).getWritableDatabase();
        Date today = new Date();
        ContentValues entry = new ContentValues();
        entry.put("task", name);
        entry.put("modified", today.getTime());
        String[] args = new String[1];
        args[0] = Integer.toString(task);
        int rows = db.update(SqlHelper.TASKS_TABLE, entry, "id=?", args);
        db.close();
        if (rows > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Deprecated
    public boolean setTaskDone(int task, boolean done) {
        SQLiteDatabase db = new SqlHelper(mContext).getWritableDatabase();
        Date today = new Date();
        ContentValues entry = new ContentValues();
        entry.put("done", done ? 1 : 0);
        entry.put("modified", today.getTime());
        String[] args = new String[1];
        args[0] = Integer.toString(task);
        int rows = db.update(SqlHelper.TASKS_TABLE, entry, "id=?", args);
        db.close();
        if (rows > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Deprecated
    public boolean setListCleared(int list) {
        SQLiteDatabase db = new SqlHelper(mContext).getWritableDatabase();
        Date today = new Date();
        ContentValues entry = new ContentValues();
        entry.put("cleared", 1);
        entry.put("modified", today.getTime());
        String[] args = new String[1];
        args[0] = Integer.toString(list);
        int rows = db.update(SqlHelper.LISTS_TABLE, entry, "id=?", args);
        db.close();
        if (rows > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Deprecated
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
        db.close();
        if (rows > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Deprecated
    public boolean updateTaskDescription(int task, String description) {
        SQLiteDatabase db = new SqlHelper(mContext).getWritableDatabase();
        Date today = new Date();
        ContentValues entry = new ContentValues();
        entry.put("description", description);
        entry.put("modified", today.getTime());
        String[] args = new String[1];
        args[0] = Integer.toString(task);
        int rows = db.update(SqlHelper.TASKS_TABLE, entry, "id=?", args);
        db.close();
        if (rows > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Deprecated
    public boolean setTaskNotification(int task, Date notification) {
        SQLiteDatabase db = new SqlHelper(mContext).getWritableDatabase();
        Date today = new Date();
        ContentValues entry = new ContentValues();
        entry.put("notification", notification.getTime());
        entry.put("modified", today.getTime());
        String[] args = new String[1];
        args[0] = Integer.toString(task);
        int rows = db.update(SqlHelper.TASKS_TABLE, entry, "id=?", args);
        db.close();
        if (rows > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Deprecated
    public boolean setTaskAlarm(int task, boolean alarm) {
        SQLiteDatabase db = new SqlHelper(mContext).getWritableDatabase();
        Date today = new Date();
        ContentValues entry = new ContentValues();
        entry.put("alarm", alarm ? 1 : 0);
        entry.put("modified", today.getTime());
        String[] args = new String[1];
        args[0] = Integer.toString(task);
        int rows = db.update(SqlHelper.TASKS_TABLE, entry, "id=?", args);
        db.close();
        if (rows > 0) {
            return true;
        } else {
            return false;
        }
    }

}
