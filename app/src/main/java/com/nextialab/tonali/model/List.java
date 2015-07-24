package com.nextialab.tonali.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by nigonzalez on 7/19/15.
 */
public class List implements Parcelable {

    private int mId;
    private String mList;
    private Date mCreated;
    private Date mModified;

    protected List(Parcel in) {
        mId = in.readInt();
        mList = in.readString();
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

    public String getList() {
        return mList;
    }

    public void setList(String list) {
        mList = list;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mList);
    }
}
