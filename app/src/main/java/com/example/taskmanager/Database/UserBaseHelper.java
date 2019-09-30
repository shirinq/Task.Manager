package com.example.taskmanager.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class UserBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "UserBase.db";

    public UserBaseHelper(@Nullable Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
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
