package com.nextialab.tonali.support;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nextialab.tonali.model.ListColumns;
import com.nextialab.tonali.model.ListOrder;
import com.nextialab.tonali.model.ListType;

/**
 * Created by Nelson on 8/13/2016.
 */
public class SqlHelper2 extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Tonali2.db";

    public SqlHelper2(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ListColumns.getCreateQuery());
        db.insert(ListColumns.LIST_TABLE, null, ListColumns.getRootList());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
