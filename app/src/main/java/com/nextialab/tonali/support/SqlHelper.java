package com.nextialab.tonali.support;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nigonzalez on 7/11/15.
 */
public class SqlHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Tonali.db";

    public static final String LISTS_TABLE = "lists";
    public static final String TASKS_TABLE = "tasks";

    public SqlHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + LISTS_TABLE + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "list TEXT, " +
                "cleared INTEGER, " +
                "created INTEGER, " +
                "modified INTEGER" +
                ");");
        db.execSQL("CREATE TABLE " + TASKS_TABLE + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "task TEXT, " +
                "list INTEGER, " +
                "done INTEGER, " +
                "cleared INTEGER, " +
                "created INTEGER, " +
                "modified INTEGER" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
