package com.nextialab.tonali.model;

import android.content.ContentValues;
import android.content.PeriodicSync;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.nextialab.tonali.support.Persistence;
import com.nextialab.tonali.support.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by nigonzalez on 7/19/15.
 */
public class TonaliList implements Parcelable {

    private long mId = -1;
    private long mParent;
    private String mName;
    private String mContent = "";
    private ListType mType;
    private List<Long> mSequence = new ArrayList<>();
    private ListOrder mOrder = ListOrder.CUSTOM;
    private int mPriority = -1;
    private boolean mChecked = false;
    private Date mAlarm = new Date(0L);
    private boolean mNotification = false;
    private Date mCreated;
    private Date mModified;
    private Date mSynced = new Date(0L);

    public TonaliList() {

    }

    @Deprecated
    public TonaliList(String name, int id) {
        mId = id;
        mName = name;
    }

    protected TonaliList(Parcel in) {
        mId = in.readLong();
        mParent = in.readLong();
        mName = in.readString();
        mContent = in.readString();
        mType = ListType.valueOf(in.readString());
        mSequence = in.readArrayList(Long.class.getClassLoader());
        mOrder = ListOrder.valueOf(in.readString());
        mPriority = in.readInt();
        mChecked = in.readByte() != 0;
        mAlarm = new Date(in.readLong());
        mNotification = in.readByte() != 0;
        mCreated = new Date(in.readLong());
        mModified = new Date(in.readLong());
        mSynced = new Date(in.readLong());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeLong(mParent);
        dest.writeString(mName);
        dest.writeString(mContent);
        dest.writeString(mType.toString());
        dest.writeList(mSequence);
        dest.writeString(mOrder.toString());
        dest.writeInt(mPriority);
        dest.writeByte((byte) (mChecked ? 1 : 0));
        dest.writeLong(mAlarm.getTime());
        dest.writeByte((byte) (mNotification ? 1 : 0));
        dest.writeLong(mCreated.getTime());
        dest.writeLong(mModified.getTime());
        dest.writeLong(mSynced.getTime());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TonaliList> CREATOR = new Creator<TonaliList>() {
        @Override
        public TonaliList createFromParcel(Parcel in) {
            return new TonaliList(in);
        }

        @Override
        public TonaliList[] newArray(int size) {
            return new TonaliList[size];
        }
    };

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public long getParent() {
        return mParent;
    }

    public void setParent(long parent) {
        mParent = parent;
    }

    public String getListName() {
        return mName;
    }

    public void setListName(String list) {
        mName = list;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public ListType getListType() {
        return mType;
    }

    public void setListType(ListType listType) {
        mType = listType;
    }

    public List<Long> getSequence() {
        return mSequence;
    }

    public void setSequence(List<Long> sequence) {
        mSequence = sequence;
    }

    public ListOrder getListOrder() {
        return mOrder;
    }

    public void setListOrder(ListOrder listOrder) {
        mOrder = listOrder;
    }

    public int getPriority() {
        return mPriority;
    }

    public void setPriority(int priority) {
        mPriority = priority;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setIsChecked(boolean checked) {
        mChecked = checked;
    }

    public Date getAlarm() {
        return mAlarm;
    }

    public void setAlarm(Date alarm) {
        mAlarm = alarm;
    }

    public boolean hasNotification() {
        return mNotification;
    }

    public void setHasNotification(boolean notification) {
        mNotification = notification;
    }

    public Date getCreated() {
        return mCreated;
    }

    public void setCreated(Date cretaed) {
        mCreated = cretaed;
    }

    public Date getModified() {
        return mModified;
    }

    public void setModified(Date modified) {
        mModified = modified;
    }

    public Date getSynced() {
        return mSynced;
    }

    public void setSynced(Date synced) {
        mSynced = synced;
    }

    public boolean save() {
        ContentValues entry = new ContentValues();
        entry.put(ListColumns.PARENT, mParent);
        entry.put(ListColumns.NAME, mName);
        entry.put(ListColumns.CONTENT, mContent);
        entry.put(ListColumns.TYPE, mType.toString());
        entry.put(ListColumns.SEQUENCE, Utils.makeString(",", mSequence));
        entry.put(ListColumns.ORDER, mOrder.toString());
        entry.put(ListColumns.PRIORITY, mPriority);
        entry.put(ListColumns.CHECKED, (mChecked ? 1 : 0));
        entry.put(ListColumns.ALARM, mAlarm.getTime());
        entry.put(ListColumns.NOTIFICATION, (mNotification ? 1 : 0));
        entry.put(ListColumns.SYNCED, mSynced.getTime());
        Persistence.instance().beginTransaction();
        if (mId > 0) {
            entry.put(ListColumns.MODIFIED, (new Date()).getTime());
            boolean result = Persistence.instance().update(ListColumns.LIST_TABLE, entry, String.format("%s=?", ListColumns.ID), new String[]{Long.toString(mId)});
            Persistence.instance().endTransaction();
            return result;
        } else {
            Date now = new Date();
            entry.put(ListColumns.CREATED, now.getTime());
            entry.put(ListColumns.MODIFIED, now.getTime());
            long id = Persistence.instance().insert(ListColumns.LIST_TABLE, entry);
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
        boolean result = Persistence.instance().delete(ListColumns.LIST_TABLE, String.format("%s=?", ListColumns.ID), new String[]{Long.toString(mId)});
        if (result) {
            mId = -1;
        }
        Persistence.instance().endTransaction();
        return result;
    }

    public static List<TonaliList> findChildren(long parent) {
        List<TonaliList> children = new ArrayList<>();
        Persistence.instance().beginTransaction();
        Cursor cursor = Persistence.instance().getRows(ListColumns.LIST_TABLE, ListColumns.getColumns(), String.format("%s=?", ListColumns.PARENT), new String[]{Long.toString(parent)});
        while (cursor.moveToNext()) {
            TonaliList list = new TonaliList();
            list.mId = cursor.getLong(cursor.getColumnIndex(ListColumns.ID));
            list.mParent = parent;
            list.mName = cursor.getString(cursor.getColumnIndex(ListColumns.NAME));
            list.mContent = cursor.getString(cursor.getColumnIndex(ListColumns.CONTENT));
            list.mType = ListType.valueOf(cursor.getString(cursor.getColumnIndex(ListColumns.TYPE)));
            list.mOrder = ListOrder.valueOf(cursor.getString(cursor.getColumnIndex(ListColumns.ORDER)));
            String order = cursor.getString(cursor.getColumnIndex(ListColumns.SEQUENCE));
            if (order.length() > 0) {
                String[] sequence = order.split(",");
                for (int i = 0; i < sequence.length; i++) {
                    list.mSequence.add(Long.parseLong(sequence[i]));
                }
            }
            list.mPriority = cursor.getInt(cursor.getColumnIndex(ListColumns.PRIORITY));
            list.mChecked = cursor.getInt(cursor.getColumnIndex(ListColumns.CHECKED)) == 1;
            list.mAlarm = new Date(cursor.getLong(cursor.getColumnIndex(ListColumns.ALARM)));
            list.mNotification = cursor.getInt(cursor.getColumnIndex(ListColumns.NOTIFICATION)) == 1;
            list.mCreated = new Date(cursor.getLong(cursor.getColumnIndex(ListColumns.CREATED)));
            list.mModified = new Date(cursor.getLong(cursor.getColumnIndex(ListColumns.MODIFIED)));
            list.mSynced = new Date(cursor.getLong(cursor.getColumnIndex(ListColumns.SYNCED)));
            children.add(list);
        }
        cursor.close();
        Persistence.instance().endTransaction();
        return children;
    }

}
