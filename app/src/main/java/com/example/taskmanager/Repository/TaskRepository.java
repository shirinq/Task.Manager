package com.example.taskmanager.Repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.taskmanager.Database.TaskBaseHelper;
import com.example.taskmanager.model.DaoMaster;
import com.example.taskmanager.model.DaoSession;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.TaskDao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskRepository {

    private Context mContext;
    private TaskDao mDatabase;
    private static TaskRepository instance;
    private static String UserId;

    private TaskRepository(){
    }

    public static TaskRepository getInstance(Context context) {
        if (instance == null) {
            instance = new TaskRepository(context);
        }

        return instance;
    }

    private TaskRepository(Context context) {

        mContext = context.getApplicationContext();

        SQLiteDatabase db = new TaskBaseHelper(mContext).getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        mDatabase = daoSession.getTaskDao();
    }

    public static void setUserId(String Id){
        UserId = Id;
    }

    /**
     * READ
     * @return
     */

    private List<Task> getTasks() {
        return mDatabase.queryBuilder().where(TaskDao.Properties.UserId.eq(UserId)).list();
    }

    /**
     * get based on state
     * @param state
     * @return
     */
    public List<Task> getTasks(String state){
        List<Task> tasks = new ArrayList<>();
        for(Task task : getTasks())
            if(task.getState().equalsIgnoreCase(state.toString()))
                tasks.add(task);
        return tasks;
    }

    /**
     * INSERT
     * @param task
     */
    public void insertTask(Task task) {
        task.setUserId(UserId);
        mDatabase.insert(task);
    }

    /**
     * UPDATE
     * @param task
     */

    public void updateTask(Task task) {
        mDatabase.update(task);
    }

    /**
     * DELETE
     * @param task
     */

    public void deleteTask(Task task){
        mDatabase.delete(task);
    }

    public void deleteTask(UUID userID){
        mDatabase.queryBuilder().where(TaskDao.Properties.UserId.eq(userID.toString()))
        .buildDelete();
    }

}
