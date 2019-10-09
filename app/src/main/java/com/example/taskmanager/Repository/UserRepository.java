package com.example.taskmanager.Repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.taskmanager.Database.TaskBaseHelper;
import com.example.taskmanager.model.DaoMaster;
import com.example.taskmanager.model.DaoSession;
import com.example.taskmanager.model.User;
import com.example.taskmanager.model.UserDao;

import java.util.List;

public class UserRepository {

    private Context mContext;
    private UserDao mDatabase;
    private static UserRepository instance;

    private UserRepository() {
    }

    private UserRepository(Context context) {
        mContext = context.getApplicationContext();

        SQLiteDatabase db = new TaskBaseHelper(mContext).getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        mDatabase = daoSession.getUserDao();
    }

    public static UserRepository getInstance(Context context) {
        if (instance == null)
            instance = new UserRepository(context);
        return instance;
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

    public User LoggedUser() {
        return mDatabase.queryBuilder().where(UserDao.Properties.Logged.eq(1)).unique();
    }


    /**
     * READ
     * @return
     */
    public List<User> getUsers() {
        return mDatabase.loadAll();
    }


    /**
     * INSERT
     */
    public Boolean insertUser(User user) {
        if (!UserIsExist(user)) {
            mDatabase.insert(user);
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
       mDatabase.update(user);
    }

    /**
     * DELETE
     *
     * @param user
     */

    public void deleteUser(User user) {
        mDatabase.delete(user);
    }

}
