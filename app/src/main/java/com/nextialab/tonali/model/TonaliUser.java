package com.nextialab.tonali.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.nextialab.tonali.support.Persistence;

import java.util.Date;

/**
 * Created by Nelson on 8/30/2016.
 */
public class TonaliUser {

    private long mId = -1;
    private String mName;
    private String mEmail;
    private String mCloudId = "";
    private Date mCreated;
    private Date mModified;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getCloudId() {
        return mCloudId;
    }

    public void setCloudId(String cloudId) {
        mCloudId = cloudId;
    }

    public Date getCreated() {
        return mCreated;
    }

    public void setCreated(Date created) {
        mCreated = created;
    }

    public Date getModified() {
        return mModified;
    }

    public void setModified(Date modified) {
        mModified = modified;
    }

    public boolean save() {
        ContentValues entry = new ContentValues();
        entry.put(UserColumns.NAME, mName);
        entry.put(UserColumns.EMAIL, mEmail);
        entry.put(UserColumns.CLOUD_ID, mCloudId);
        entry.put(UserColumns.CREATED, mCreated.getTime());
        Persistence.instance().beginTransaction();
        if (mId > 0) {
            entry.put(UserColumns.MODIFIED, (new Date()).getTime());
            boolean result = Persistence.instance().update(UserColumns.USER_TABLE, entry, String.format("%s=?", ListColumns.ID), new String[]{Long.toString(mId)});
            Persistence.instance().endTransaction();
            return result;
        } else {
            Date now = new Date();
            entry.put(UserColumns.CREATED, now.getTime());
            entry.put(UserColumns.MODIFIED, now.getTime());
            long id = Persistence.instance().insert(UserColumns.USER_TABLE, entry);
            Persistence.instance().endTransaction();
            if (id > -1) {
                mId = id;
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean delete() {
        Persistence.instance().beginTransaction();
        boolean result = Persistence.instance().delete(UserColumns.USER_TABLE, String.format("%s=?", UserColumns.ID), new String[]{Long.toString(mId)});
        if (result) {
            mId = -1;
        }
        Persistence.instance().endTransaction();
        return result;
    }

    public static TonaliUser getUserWithId(long id) {
        TonaliUser user = new TonaliUser();
        Persistence.instance().beginTransaction();
        Cursor cursor = Persistence.instance().getRows(UserColumns.USER_TABLE, UserColumns.getColumns(), String.format("%s=?", UserColumns.ID), new String[]{Long.toString(id)});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            user.mId = id;
            user.mName = cursor.getString(cursor.getColumnIndex(UserColumns.NAME));
            user.mEmail = cursor.getString(cursor.getColumnIndex(UserColumns.EMAIL));
            user.mCloudId = cursor.getString(cursor.getColumnIndex(UserColumns.CLOUD_ID));
            user.mCreated = new Date(cursor.getLong(cursor.getColumnIndex(UserColumns.CREATED)));
            user.mModified = new Date(cursor.getLong(cursor.getColumnIndex(UserColumns.MODIFIED)));
        }
        cursor.close();
        Persistence.instance().endTransaction();
        return user;
    }

    public static TonaliUser getUserWithCloudId(String cloudId) {
        TonaliUser user = new TonaliUser();
        Persistence.instance().beginTransaction();
        Cursor cursor = Persistence.instance().getRows(UserColumns.USER_TABLE, UserColumns.getColumns(), String.format("%s=?", UserColumns.CLOUD_ID), new String[]{cloudId});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            user.mId = cursor.getLong(cursor.getColumnIndex(UserColumns.ID));
            user.mName = cursor.getString(cursor.getColumnIndex(UserColumns.NAME));
            user.mEmail = cursor.getString(cursor.getColumnIndex(UserColumns.EMAIL));
            user.mCloudId = cloudId;
            user.mCreated = new Date(cursor.getLong(cursor.getColumnIndex(UserColumns.CREATED)));
            user.mModified = new Date(cursor.getLong(cursor.getColumnIndex(UserColumns.MODIFIED)));
        }
        cursor.close();
        Persistence.instance().endTransaction();
        return user;
    }

    /*public static List<TonaliUser> getUsersWithSharedLists() {

    }*/

}
