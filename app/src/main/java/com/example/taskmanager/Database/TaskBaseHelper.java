package com.example.taskmanager.Database;

import android.content.Context;

import com.example.taskmanager.model.DaoMaster;

public class TaskBaseHelper extends DaoMaster.OpenHelper {

    private static final String DATABASE_NAME = "TaskUserBase.db";

    public TaskBaseHelper(Context context) {
        super(context, DATABASE_NAME);
    }
}
