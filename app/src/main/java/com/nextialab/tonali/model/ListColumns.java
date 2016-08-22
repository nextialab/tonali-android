package com.nextialab.tonali.model;

import android.content.ContentValues;

import java.io.PipedReader;
import java.util.Date;

/**
 * Created by Nelson on 8/13/2016.
 */
public class ListColumns {

    public static final String LIST_TABLE = "lists";
    public static final String ID = "id";
    public static final String PARENT = "parent";
    public static final String NAME = "name";
    public static final String CONTENT = "content";
    public static final String TYPE = "type";
    public static final String ORDER = "_order";
    public static final String SEQUENCE = "sequence";
    public static final String PRIORITY = "priority";
    public static final String CHECKED = "checked";
    public static final String ALARM = "alarm";
    public static final String NOTIFICATION = "notification";
    public static final String CREATED = "created";
    public static final String MODIFIED = "modified";
    public static final String SYNCED = "synced";

    public static String[] getColumns() {
        return new String[]{ID, PARENT, NAME, CONTENT, TYPE, ORDER, SEQUENCE, PRIORITY, CHECKED, ALARM, NOTIFICATION, CREATED, MODIFIED, SYNCED};
    }

    public static String getCreateQuery() {
        return "CREATE TABLE " + LIST_TABLE + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PARENT + " INTEGER, " +
                NAME + " TEXT, " +
                CONTENT + " TEXT, " +
                TYPE + " TEXT, " +
                ORDER + " TEXT, " +
                SEQUENCE + " TEXT, " +
                PRIORITY + " INTEGER, " +
                CHECKED + " INTEGER, " +
                ALARM + " INTEGER, " +
                NOTIFICATION + " INTEGER, " +
                CREATED + " INTEGER, " +
                MODIFIED + " INTEGER, " +
                SYNCED + " INTEGER);";
    }

    public static ContentValues getRootList() {
        ContentValues entry = new ContentValues();
        entry.put(PARENT, 0L);
        entry.put(NAME, "root");
        entry.put(CONTENT, "");
        entry.put(TYPE, ListType.LIST.toString());
        entry.put(ORDER, ListOrder.ALPHA.toString());
        entry.put(SEQUENCE, "");
        entry.put(PRIORITY, 0);
        entry.put(CHECKED, 0);
        entry.put(ALARM, 0L);
        entry.put(NOTIFICATION, 0);
        entry.put(CREATED, (new Date()).getTime());
        entry.put(MODIFIED, (new Date()).getTime());
        entry.put(SYNCED, 0L);
        return entry;
    }

    public static String getUpdateFrom(int oldVersion, int newVersion) {
        return "";
    }

}
