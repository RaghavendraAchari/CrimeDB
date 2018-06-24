package com.raghav.android.crimedb;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Vighnesh on 17-04-2018.
 */

public class Crime {
    private Date mDate;
    private String mTitle;
    private boolean mSolved;

    public Crime(){
        mId=UUID.randomUUID();
        mDate=new Date();
    }

    public UUID getId() {
        return mId;
    }

    private UUID mId;

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }
}
