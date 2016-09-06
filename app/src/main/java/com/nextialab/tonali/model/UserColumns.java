package com.nextialab.tonali.model;

import android.content.ContentValues;

import java.util.Date;

/**
 * Created by Nelson on 8/30/2016.
 */
public class UserColumns {

    public static final String USER_TABLE = "users";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String CLOUD_ID = "cloud_id";
    public static final String CREATED = "created";
    public static final String MODIFIED = "modified";

    public static String[] getColumns() {
        return new String[]{ID, NAME, EMAIL, CLOUD_ID, CREATED, MODIFIED};
    }

    public static String getCreateQuery() {
        return "CREATE TABLE " + USER_TABLE + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME + " TEXT, " +
                EMAIL + " TEXT, " +
                CLOUD_ID + " TEXT, " +
                CREATED + " INTEGER, " +
                MODIFIED + " INTEGER);";
    }

    public static ContentValues getInitialUser() {
        ContentValues entry = new ContentValues();
        entry.put(NAME, "local");
        entry.put(EMAIL, "user@local.com");
        entry.put(CLOUD_ID, "");
        entry.put(CREATED, (new Date()).getTime());
        entry.put(MODIFIED, (new Date()).getTime());
        return entry;
    }

    public static String getUpdateFrom(int oldVersion, int newVersion) {
        return "";
    }

}
