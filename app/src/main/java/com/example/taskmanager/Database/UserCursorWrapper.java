package com.example.taskmanager.Database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.taskmanager.model.User;

import java.util.UUID;

public class UserCursorWrapper extends CursorWrapper {

    public UserCursorWrapper(Cursor cursor) {
        super(cursor);
    }
    public User getUser() {

        String stringUUID = getString(getColumnIndex(TaskDBSchema.UserTable.Cols.UUID));
        String Username = getString(getColumnIndex(TaskDBSchema.UserTable.Cols.USERNAME));
        Integer Password = getInt(getColumnIndex(TaskDBSchema.UserTable.Cols.PASSWORD));
        boolean Logged = getInt(getColumnIndex(TaskDBSchema.UserTable.Cols.LOGGED)) == 1 ;


        UUID uuid = UUID.fromString(stringUUID);

        User user = new User(uuid);
        user.setUsername(Username);
        user.setPassword(Password);
        user.setLogged(Logged);

        return user;
    }
}
