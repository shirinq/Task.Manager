package com.example.taskmanager.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import com.example.taskmanager.Database.TaskBaseHelper;
import com.example.taskmanager.Database.TaskDBSchema;
import com.example.taskmanager.Database.UserCursorWrapper;
import com.example.taskmanager.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserRepository {

    private Context mContext;
    private SQLiteDatabase mDatabase;
    private static UserRepository instance;

    private UserRepository() {
    }

    private UserRepository(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new TaskBaseHelper(context).getWritableDatabase();
    }

    public static UserRepository getInstance(Context context) {
        if (instance == null)
            instance = new UserRepository(context);
        return instance;
    }

    public List<User> getUsers() {
        List<User> users = new ArrayList<>();

        UserCursorWrapper cursor = (UserCursorWrapper) queryUsers(null, null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast() && !cursor.isNull(0)) {
                users.add(cursor.getUser());
                cursor.moveToNext();
            }

        } finally {
            cursor.close();
        }
        return users;
    }

    public User LoggedUser() {
        for(User u : getUsers())
            if(u.isLogged())
                return u;
            return null;
    }


    private CursorWrapper queryUsers(String[] columns, String where, String[] whereArgs) {
        Cursor cursor = mDatabase.query(TaskDBSchema.UserTable.NAME,
                columns,
                where,
                whereArgs,
                null,
                null,
                null);

        return new UserCursorWrapper(cursor);
    }


    private ContentValues getContentValues(User user) {
        ContentValues values = new ContentValues();
        values.put(TaskDBSchema.UserTable.Cols.UUID, user.getID().toString());
        values.put(TaskDBSchema.UserTable.Cols.USERNAME, user.getUsername());
        values.put(TaskDBSchema.UserTable.Cols.PASSWORD, user.getPassword());
        int log = 0 ;
        if(user.isLogged())
            log = 1;
        values.put(TaskDBSchema.UserTable.Cols.LOGGED, log);

        return values;
    }

    public Boolean UserIsExist(User user) {
        for (User u : getUsers())
            if (u.getUsername().equals(user.getUsername()))
                return true;
        return false;
    }

    public Boolean UserValidation(User user) {
        for (User u : getUsers())
            if (u.getUsername().equals(user.getUsername()))
                if (u.getPassword().equals(user.getPassword()))
                    return true;
        return false;
    }

    public User getUser(User user) {
        for (User u : getUsers())
            if (u.getUsername().equals(user.getUsername()))
                if (u.getPassword().equals(user.getPassword()))
                    return u;
        return null;
    }

    public User getUser(UUID id) {
        String where = TaskDBSchema.UserTable.Cols.UUID + " = ?";
        String[] whereArgs = new String[]{id.toString()};

        UserCursorWrapper cursor = (UserCursorWrapper) queryUsers(null, where, whereArgs);

        try {
            cursor.moveToFirst();
            return cursor.getUser();

        } finally {
            cursor.close();
        }
    }


    /**
     * INSERT
     */
    public Boolean insertUser(User user) {
        if (!UserIsExist(user)) {
            ContentValues values = getContentValues(user);
            mDatabase.insert(TaskDBSchema.UserTable.NAME, null, values);
            return true;
        }
        return false;
    }

    /**
     * UPDATE
     *
     * @param user
     */

    public void updateUser(User user) {
        ContentValues values = getContentValues(user);
        String where = TaskDBSchema.UserTable.Cols.UUID + " = ?";
        String[] whereArgs = new String[]{user.getID().toString()};
        mDatabase.update(TaskDBSchema.UserTable.NAME, values, where, whereArgs);
    }

    /**
     * DELETE
     *
     * @param user
     */

    public void deleteUser(User user) {
        ContentValues values = getContentValues(user);
        String where = TaskDBSchema.UserTable.Cols.UUID + " = ?";
        String[] whereArgs = new String[]{user.getID().toString()};
        mDatabase.delete(TaskDBSchema.UserTable.NAME, where, whereArgs);
    }

}
