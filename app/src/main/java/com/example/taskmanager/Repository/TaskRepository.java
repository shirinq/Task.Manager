package com.example.taskmanager.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import com.example.taskmanager.Database.TaskBaseHelper;
import com.example.taskmanager.Database.TaskCursorWrapper;
import com.example.taskmanager.Database.TaskDBSchema;
import com.example.taskmanager.model.State;
import com.example.taskmanager.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskRepository {

    private Context mContext;
    private SQLiteDatabase mDatabase;
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
        mDatabase = new TaskBaseHelper(context).getWritableDatabase();
    }

    public static void setUserId(String Id){
        UserId = Id;
    }

    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();

        TaskCursorWrapper cursor = (TaskCursorWrapper) queryTasks(
                null,
                TaskDBSchema.TaskTable.Cols.USERID + " = ?",
                new String[]{UserId});

        try {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                tasks.add(cursor.getTask());

                cursor.moveToNext();
            }

        } finally {
            cursor.close();
        }

        return tasks;
    }

    /**
     * get based on state
     * @param state
     * @return
     */
    public List<Task> getTasks(State state){
        List<Task> tasks = new ArrayList<>();
        for(Task task : getTasks())
            if(task.getState().equalsIgnoreCase(state.toString()))
                tasks.add(task);
        return tasks;
    }

    /**
     * READ
     * @param id
     * @return
     */

    public Task getTask(UUID id) {
        TaskCursorWrapper cursor = (TaskCursorWrapper) queryTasks(
                null,
                TaskDBSchema.TaskTable.Cols.UUID + " = ?",
                new String[]{id.toString()});

        try {
            cursor.moveToFirst();

            if (cursor == null || cursor.getCount() == 0)
                return null;

            return cursor.getTask();
        } finally {
            cursor.close();
        }
    }

    public int getPosition(UUID id) {
        List<Task> tasks = getTasks();
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId().equals(id))
                return i;
        }
        return 0;
    }

    /**
     * INSERT
     * @param task
     */
    public void insertTask(Task task) {
        ContentValues values = getContentValues(task);
        mDatabase.insert(TaskDBSchema.TaskTable.NAME, null, values);
    }

    /**
     * UPDATE
     * @param task
     */

    public void updateTask(Task task) {
        ContentValues values = getContentValues(task);
        String where = TaskDBSchema.TaskTable.Cols.UUID + " = ?";
        String[] whereArgs = new String[]{task.getId().toString()};
        mDatabase.update(TaskDBSchema.TaskTable.NAME, values, where, whereArgs);
    }

    /**
     * DELETE
     * @param task
     */

    public void deleteTask(Task task){
        String where = TaskDBSchema.TaskTable.Cols.UUID + " = ?";
        String[] whereArgs = new String[]{task.getId().toString()};
        mDatabase.delete(TaskDBSchema.TaskTable.NAME,where,whereArgs);
    }

    private CursorWrapper queryTasks(String[] columns, String where, String[] whereArgs) {
        Cursor cursor = mDatabase.query(TaskDBSchema.TaskTable.NAME,
                columns,
                where,
                whereArgs,
                null,
                null,
                null);

        return new TaskCursorWrapper(cursor);
    }

    private ContentValues getContentValues(Task task) {
        ContentValues values = new ContentValues();
        values.put(TaskDBSchema.TaskTable.Cols.USERID, UserId);
        values.put(TaskDBSchema.TaskTable.Cols.UUID, task.getId().toString());
        values.put(TaskDBSchema.TaskTable.Cols.TITLE, task.getTitle());
        values.put(TaskDBSchema.TaskTable.Cols.DATE, task.getDate().toString());
        values.put(TaskDBSchema.TaskTable.Cols.STATE , task.getState());
        values.put(TaskDBSchema.TaskTable.Cols.DESCRIPTION , task.getDescription());

        return values;
    }
}
