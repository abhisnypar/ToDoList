package com.cerner.a2do.contentProvider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by c15333 on 8/31/17.
 */

@SuppressWarnings("Since15")
public class ToDoListContract {

    public static final String AUTHORITY = "com.cerner.a2do.contentProvider";
    public static final Uri CONTENT_URI = new Uri.Builder()
            .scheme(ContentResolver.SCHEME_CONTENT)
            .authority(AUTHORITY)
            .build();
    public static final String DIR_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd." + AUTHORITY;

    public static class TODO_LIST implements BaseColumns {
        public static final String TABLE = "todoList";
        public static final String DIR_CONTENT_TYPE = ToDoListContract.DIR_CONTENT_TYPE + "." + TABLE;
        public static final Uri CONTENT_URI = ToDoListContract.CONTENT_URI
                .buildUpon()
                .appendPath(TABLE)
                .build();
        public static final String TASK_TITLE = "task_title";
        public static final String TASK_DESCRIPTION = "task_description";
        public static final String TASK_TYPE = "task_type";
        public static final String TASK_STATUS = "task_status";

        public static final String CREATE_TODO_LIST = "CREATE TABLE " + TABLE + " (" +
                _ID + " INTEGER PRIMARY KEY NOT NULL, " +
                TASK_TITLE + " TEXT NOT NULL, " +
                TASK_DESCRIPTION + " TEXT NOT NULL, " +
                TASK_TYPE + " TEXT, " +
                TASK_STATUS + " TEXT NOT NULL" +
                ")";

        public static final String[] LIST_PROJECTION = new String[]{
                _ID,
                TASK_TITLE,
                TASK_DESCRIPTION,
                TASK_TYPE,
                TASK_STATUS
        };
    }
}
