package com.nextialab.tonali.model;

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
    public static final String ORDER = "order";
    public static final String ORDERING = "ordering";
    public static final String PRIORITY = "priority";
    public static final String CHECKED = "checked";
    public static final String ALARM = "alarm";
    public static final String NOTIFICATION = "notification";
    public static final String CREATED = "created";
    public static final String MODIFIED = "modified";
    public static final String SYNCED = "synced";

    public static String getCreateQuery() {
        return "CREATE TABLE " + LIST_TABLE + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PARENT + " INTEGER, " +
                NAME + " TEXT, " +
                CONTENT + " TEXT, " +
                TYPE + " TEXT, " +
                ORDER + " TEXT, " +
                PRIORITY + " INTEGER, " +
                CHECKED + " INTEGER, " +
                ALARM + " INTEGER, " +
                NOTIFICATION + " INTEGER, " +
                CREATED + " INTEGER, " +
                MODIFIED + " INTEGER, " +
                SYNCED + " INTEGER);";
    }

    public static String getUpdateFrom(int oldVersion, int newVersion) {
        return "";
    }

}
