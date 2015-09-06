package com.nextialab.tonali.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nigonzalez on 7/19/15.
 */
public class List implements Parcelable {

    private int mId;
    private String mListName;
    private int mTasks = 0;

    public List(String name, int id) {
        mId = id;
        mListName = name;
    }

    protected List(Parcel in) {
        mId = in.readInt();
        mListName = in.readString();
        mTasks = in.readInt();
    }

    public static final Creator<List> CREATOR = new Creator<List>() {
        @Override
        public List createFromParcel(Parcel in) {
            return new List(in);
        }

        @Override
        public List[] newArray(int size) {
            return new List[size];
        }
    };

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getListName() {
        return mListName;
    }

    public void setListName(String list) {
        mListName = list;
    }

    public int getTasksCount() {
        return mTasks;
    }

    public void setTasksCounter(int tasks) {
        mTasks = tasks;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mListName);
        dest.writeInt(mTasks);
    }
}
