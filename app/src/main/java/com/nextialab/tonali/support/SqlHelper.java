package com.nextialab.tonali.support;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nigonzalez on 7/11/15.
 */
@Deprecated
public class SqlHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "Tonali.db";

    public static final String LISTS_TABLE = "lists";
    public static final String TASKS_TABLE = "tasks";
    public static final String ORDERS_TABLE = "orders";

    public SqlHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + LISTS_TABLE + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "list TEXT, " +
                "type INTEGER, " +
                "cleared INTEGER, " +
                "created INTEGER, " +
                "modified INTEGER" +
                ");");
        db.execSQL("CREATE TABLE " + TASKS_TABLE + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "task TEXT, " +
                "description TEXT, " +
                "list INTEGER, " +
                "done INTEGER, " +
                "cleared INTEGER, " +
                "notification INTEGER, " +
                "alarm INTEGER, " +
                "created INTEGER, " +
                "modified INTEGER" +
                ");");
        db.execSQL("CREATE TABLE " + ORDERS_TABLE + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "list INTEGER, " +
                "_order TEXT, " +
                "created INTEGER, " +
                "modified INTEGER" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1) {
            db.execSQL("ALTER TABLE " + TASKS_TABLE + " ADD description TEXT ");
            db.execSQL("ALTER TABLE " + TASKS_TABLE + " ADD notification INTEGER ");
            db.execSQL("ALTER TABLE " + TASKS_TABLE + " ADD alarm INTEGER ");
            db.execSQL("UPDATE " + TASKS_TABLE + " SET description='', alarm=0, notification=0");
            db.execSQL("CREATE TABLE " + ORDERS_TABLE + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "list INTEGER, " +
                    "_order TEXT, " +
                    "created INTEGER, " +
                    "modified INTEGER" +
                    ");");
        } else if (oldVersion == 2) {
            db.execSQL("ALTER TABLE " + TASKS_TABLE + " ADD alarm INTEGER ");
            db.execSQL("UPDATE " + TASKS_TABLE + " SET alarm=0");
            db.execSQL("CREATE TABLE " + ORDERS_TABLE + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "list INTEGER, " +
                    "_order TEXT, " +
                    "created INTEGER, " +
                    "modified INTEGER" +
                    ");");
        } else if (oldVersion == 3) {
            db.execSQL("CREATE TABLE " + ORDERS_TABLE + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "list INTEGER, " +
                    "_order TEXT, " +
                    "created INTEGER, " +
                    "modified INTEGER" +
                    ");");
        }
    }
}
