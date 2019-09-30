package com.example.taskmanager.model;

import java.util.UUID;

public class User {

    private UUID ID;
    private String username;
    private Integer password;
    private boolean logged;

    public User( String username , Integer password) {
        this.username = username;
        this.password = password;
        this.ID = UUID.randomUUID();
        this.logged = false;
    }

    public boolean isLogged() { return logged; }

    public void setLogged(boolean logged) { this.logged = logged; }

    public User(UUID id){
        ID = id;
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
}
