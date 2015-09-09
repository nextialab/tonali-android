package com.nextialab.tonali.model;

import java.util.Date;

/**
 * Created by nigonzalez on 7/19/15.
 */
public class Task implements Comparable<Task> {

    private int mId;
    private String mTask;
    private boolean mDone;
    private Date mCreated;

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

    public boolean isDone() {
        return mDone;
    }

    public void setDone(boolean done) {
        mDone = done;
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
}
