package com.cerner.a2do.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cerner.a2do.contentProvider.ToDoListContract;

/**
 * Created by c15333 on 8/31/17.
 */

public class ToDoListSQLiteOpenHelper extends SQLiteOpenHelper {
    public ToDoListSQLiteOpenHelper(Context context) {
        super(context, "todo_list", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(ToDoListContract.TODO_LIST.CREATE_TODO_LIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
