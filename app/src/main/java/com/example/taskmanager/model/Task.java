package com.example.taskmanager.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.taskmanager.Database.UUIDconverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import org.greenrobot.greendao.annotation.Generated;

@Entity (nameInDb = "Task")
public class Task {

    @Id(autoincrement = true)
    private Long _id;

    @Property (nameInDb = "UserId")
    private String UserId;

    @Property (nameInDb = "TaskId")
    @Index (unique = true)
    @Convert(converter = UUIDconverter.class, columnType = String.class )
    private UUID Id;

    @Property (nameInDb = "State")
    private String State;

    @Property (nameInDb = "Title")
    private String Title;

    @Property (nameInDb = "Description")
    private String Description;

    @Property (nameInDb = "Date")
    private Date Date;

    public Task(String state, String title, Date date , String description) {
        this.State = state;
        this.Title = title;
        this.Date = date;
        this.Description = description;
        this.Id = UUID.randomUUID();
    }

    public Task(){
        Date = new Date();
    }

    public Task(UUID id){
        Id = id;
    }

    @Generated(hash = 1621319815)
    public Task(Long _id, String UserId, UUID Id, String State, String Title,
            String Description, Date Date) {
        this._id = _id;
        this.UserId = UserId;
        this.Id = Id;
        this.State = State;
        this.Title = Title;
        this.Description = Description;
        this.Date = Date;
    }
    
    public Date getDate() {
        return Date;
    }

    public void setDate(Date date) {
        this.Date = date;
    }

    public String getState() {
        return State.toString();
    }

    public void setState(String state) {
        this.State = state;
    }

    public String getTitle() { return Title; }

    public void setTitle(String title) {
        Title = title;
    }

    public UUID getId() {
        return Id;
    }

    public String getDescription() { return Description; }

    public void setDescription(String mDescription) { this.Description = mDescription; }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return Objects.equals(getId(), task.getId());
    }

    public void setId(UUID Id) {
        this.Id = Id;
    }

}
