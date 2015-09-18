package com.nextialab.tonali.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by nigonzalez on 7/19/15.
 */
public class Task implements Comparable<Task>, Parcelable {

    private int mId;
    private String mTask;
    private String mDescription;
    private boolean mDone;
    private Date mNotification;
    private Date mCreated;

    public Task() {

    }

    protected Task(Parcel in) {
        mId = in.readInt();
        mTask = in.readString();
        mDescription = in.readString();
        mNotification = new Date(in.readLong());
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getTask() {
        return mTask;
    }

    public void setTask(String task) {
        mTask = task;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public boolean isDone() {
        return mDone;
    }

    public void setDone(boolean done) {
        mDone = done;
    }

    public Date getNotification() {
        return mNotification;
    }

    public void setNotification(Date notification) {
        mNotification = notification;
    }

    public Date getCreated() {
        return mCreated;
    }

    public void setCreated(Date created) {
        mCreated = created;
    }

    @Override
    public int compareTo(Task another) {
        if (mDone == another.isDone()) {
            if (mCreated.before(another.getCreated())) {
                return 1;
            } else {
                return -1;
            }
        } else if (mDone) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mTask);
        dest.writeString(mDescription);
        dest.writeLong(mNotification.getTime());
    }
}
