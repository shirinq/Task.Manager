package com.example.taskmanager.Repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.taskmanager.Database.TaskBaseHelper;
import com.example.taskmanager.model.DaoMaster;
import com.example.taskmanager.model.DaoSession;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.TaskDao;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskRepository {

    private Context mContext;
    private TaskDao mDatabase;
    private static TaskRepository instance;
    private static String UserId;
    private File temp_directory;
    private File main_directory;

    private TaskRepository() {
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

        temp_directory = new File(mContext.getFilesDir(), "temp");
        main_directory = new File(mContext.getFilesDir(), "main_images");

        if (!temp_directory.exists())
            temp_directory.mkdirs();
        if (!main_directory.exists())
            main_directory.mkdirs();

    }

    public static void setUserId(String Id) {
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
     *
     * @param state
     * @return
     */
    public List<Task> getTasks(String state) {
        List<Task> tasks = new ArrayList<>();
        for (Task task : getTasks())
            if (task.getState().equalsIgnoreCase(state))
                tasks.add(task);
        return tasks;
    }
    public List<Task> findTask(String string){
        return mDatabase.queryBuilder()
                .whereOr(TaskDao.Properties.Title.like(string),
                        TaskDao.Properties.Description.like(string),
                        TaskDao.Properties.State.like(string)).list();
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

    public void deleteTask(Task task) {
        mDatabase.delete(task);
    }

    public void deleteTask(UUID userID) {
        mDatabase.queryBuilder().where(TaskDao.Properties.UserId.eq(userID.toString()))
                .buildDelete();
    }

    public File getPhotoFile(Task task) {
        return new File(main_directory, task.getPhotoName());
    }

    public File getTempPhotoFile(Task task) {
        return new File(temp_directory, task.getPhotoName());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void savePermanent(Task task) {
        File temp = new File(temp_directory, task.getPhotoName());
        File main = new File(main_directory, task.getPhotoName());
        Path path = null;
        try {
            path = Files.move(temp.toPath(),main.toPath());
        } catch (IOException e) {
            Log.d("move files", "savePermanent: " + path);
        }
    }

    public void deletePhotoFile(Task task) {
        File file = new File(main_directory, task.getPhotoName());
        file.deleteOnExit();
    }

    public void deleteTempDir(){
        DeleteTemp deleteTemp = new DeleteTemp();
        deleteTemp.execute();
    }

    private class DeleteTemp extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            temp_directory.delete();
            return null;
        }
    }
}
