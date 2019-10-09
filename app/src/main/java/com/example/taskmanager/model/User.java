package com.example.taskmanager.model;

import com.example.taskmanager.Database.UUIDconverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;

import java.util.UUID;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "User")
public class User {

    @Id(autoincrement = true)
    private Long _id;

    @Property(nameInDb = "Id")
    @Convert(converter = UUIDconverter.class, columnType = String.class)
    @Index(unique = true)
    private UUID ID;

    @Property(nameInDb = "Username")
    private String username;

    @Property(nameInDb = "Password")
    private Integer password;

    @Property(nameInDb = "Logged")
    private boolean logged;

    public User(String username, Integer password) {
        this.username = username;
        this.password = password;
        this.ID = UUID.randomUUID();
        this.logged = false;
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public User(UUID id) {
        ID = id;
    }

    @Generated(hash = 1750441223)
    public User(Long _id, UUID ID, String username, Integer password,
            boolean logged) {
        this._id = _id;
        this.ID = ID;
        this.username = username;
        this.password = password;
        this.logged = logged;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public UUID getID() {
        return ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getPassword() {
        return password;
    }

    public void setPassword(Integer password) {
        this.password = password;
    }

    public void setID(UUID ID) {
        this.ID = ID;
    }

    public boolean getLogged() {
        return this.logged;
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }
}
