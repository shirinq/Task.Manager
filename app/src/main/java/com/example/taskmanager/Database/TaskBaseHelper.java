package com.example.taskmanager.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class TaskBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "TaskUserBase.db";

    public TaskBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TaskDBSchema.TaskTable.NAME +
                "(" +
                TaskDBSchema.TaskTable.Cols.USERID + " ," +
                TaskDBSchema.TaskTable.Cols.UUID + " ," +
                TaskDBSchema.TaskTable.Cols.TITLE + " ," +
                TaskDBSchema.TaskTable.Cols.DATE + " ," +
                TaskDBSchema.TaskTable.Cols.STATE + " ," +
                TaskDBSchema.TaskTable.Cols.DESCRIPTION + " ," +
                " FOREIGN KEY "+" ("+ TaskDBSchema.TaskTable.Cols.USERID+" )" + " REFERENCES " +
                TaskDBSchema.UserTable.NAME +" (" + TaskDBSchema.UserTable.Cols.UUID + " )" +
                " )"
        );

        sqLiteDatabase.execSQL(" CREATE TABLE " + TaskDBSchema.UserTable.NAME +
                "(" +
                TaskDBSchema.UserTable.Cols.UUID + ", " +
                TaskDBSchema.UserTable.Cols.USERNAME + ", " +
                TaskDBSchema.UserTable.Cols.PASSWORD + ", " +
                TaskDBSchema.UserTable.Cols.LOGGED +
                " )"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
