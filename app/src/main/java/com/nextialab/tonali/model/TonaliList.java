package com.nextialab.tonali.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by nigonzalez on 7/19/15.
 */
public class TonaliList implements Parcelable {

    private long mId;
    private long mParent;
    private String mName;
    private String mContent;
    private ListType mType;
    private List<Long> mSequence;
    private ListOrder mOrder;
    private int mPriority;
    private boolean mChecked;
    private Date mAlarm;
    private boolean mNotification;
    private Date mCreated;
    private Date mModified;
    private Date mSynced;

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
        return true;
    }

    public boolean delete() {
        return true;
    }

    public static List<TonaliList> findChildren(long parent) {
        return new ArrayList<>();
    }

}
