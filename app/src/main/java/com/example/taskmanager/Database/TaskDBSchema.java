package com.example.taskmanager.Database;

public class TaskDBSchema {
    public static final class TaskTable{

        public static final String NAME = "Task";

        public static final class Cols {

            public static final String USERID = "UserId";
            public static final String UUID = "UUID";
            public static final String TITLE = "Title";
            public static final String DATE = "Date";
            public static final String STATE = "State";
            public static final String DESCRIPTION = "Description";

        }
    }

    public static final class UserTable{

        public static final String NAME = "User";

        public static final class Cols {

            public static final String UUID = "UUID";
            public static final String USERNAME = "Username";
            public static final String PASSWORD = "Password";
            public static final String LOGGED = "Logged";

        }
    }
}
