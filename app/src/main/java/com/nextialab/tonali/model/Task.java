package com.nextialab.tonali.model;

import java.util.Date;

/**
 * Created by nigonzalez on 7/19/15.
 */
public class Task {

    private int mId;
    private String mTask;
    private List mList;
    private boolean mCompleted;
    private Date mCreated;
    private Date mModified;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getTask() {
        return mTask;
    }

    public List getList() {
        return mList;
    }

    public void setList(List list) {
        mList = list;
    }

    public boolean isCompleted() {
        return mCompleted;
    }

    public void setCompleted(boolean completed) {
        mCompleted = completed;
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

}
