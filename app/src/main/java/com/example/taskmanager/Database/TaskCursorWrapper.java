package com.example.taskmanager.Database;

import android.database.Cursor;
import android.database.CursorWrapper;
import com.example.taskmanager.model.Task;

import java.util.Date;
import java.util.UUID;

public class TaskCursorWrapper extends CursorWrapper {

    public TaskCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Task getTask() {
        String userId = getString(getColumnIndex(TaskDBSchema.TaskTable.Cols.USERID));
        String stringUUID = getString(getColumnIndex(TaskDBSchema.TaskTable.Cols.UUID));
        String title = getString(getColumnIndex(TaskDBSchema.TaskTable.Cols.TITLE));
        String Date = getString(getColumnIndex(TaskDBSchema.TaskTable.Cols.DATE));
        String state = getString(getColumnIndex(TaskDBSchema.TaskTable.Cols.STATE));
        String description = getString(getColumnIndex(TaskDBSchema.TaskTable.Cols.DESCRIPTION));

        UUID uuid = UUID.fromString(stringUUID);
        Date date = new Date(Date);

        Task task = new Task(uuid);
        task.setUserId(userId);
        task.setTitle(title);
        task.setDate(date);
        task.setState(state);
        task.setDescription(description);

        return task;
    }
}
