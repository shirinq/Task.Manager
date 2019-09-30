package com.example.taskmanager.model;

import android.os.Build;
import android.os.ParcelUuid;

import androidx.annotation.RequiresApi;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class Task {
    private String UserId;
    private UUID mId;
    private State mState;
    private String mTitle;
    private String mDescription;
    private Date mDate;

    public Task(State state, String title, Date date , String description) {
        this.mState = state;
        this.mTitle = title;
        this.mDate = date;
        this.mDescription = description;
        this.mId = UUID.randomUUID();
    }

    public Task(){
        mDate = new Date();
    }

    public Task(UUID id){
        mId = id;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        this.mDate = date;
    }

    public String getState() {
        return mState.toString();
    }

    public void setState(String state) {
        if(state.equalsIgnoreCase("todo"))
            mState = State.TODO;
        else if(state.equalsIgnoreCase("doing"))
            mState = State.DOING;
        else
            mState = State.DONE;
    }

    public String getTitle() { return mTitle; }

    public void setTitle(String title) {
        mTitle = title;
    }

    public UUID getId() {
        return mId;
    }

    public String getDescription() { return mDescription; }

    public void setDescription(String mDescription) { this.mDescription = mDescription; }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return Objects.equals(getId(), task.getId());
    }
}
