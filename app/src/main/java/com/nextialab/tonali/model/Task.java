package com.nextialab.tonali.model;

import java.util.Date;

/**
 * Created by nigonzalez on 7/19/15.
 */
public class Task {

    private int mId;
    private String mTask;
    private boolean mDone;

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

}
