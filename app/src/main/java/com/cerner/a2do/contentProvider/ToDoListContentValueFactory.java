package com.cerner.a2do.contentProvider;

import android.content.ContentValues;

/**
 * This class saves the content values to the database
 */
public class ToDoListContentValueFactory {

    private ToDoListContentValueFactory() {

    }

    public static ContentValues newToDoListValues(String title, String description, String status, String type) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ToDoListContract.TODO_LIST.TASK_TITLE, title);
        contentValues.put(ToDoListContract.TODO_LIST.TASK_DESCRIPTION, description);
        contentValues.put(ToDoListContract.TODO_LIST.TASK_STATUS, status);
        contentValues.put(ToDoListContract.TODO_LIST.TASK_TYPE, type);
        return contentValues;
    }

    public static ContentValues updateToDoList(String title, String description, String status, String type) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ToDoListContract.TODO_LIST.TASK_TITLE, title);
        contentValues.put(ToDoListContract.TODO_LIST.TASK_DESCRIPTION, description);
        contentValues.put(ToDoListContract.TODO_LIST.TASK_STATUS, status);
        contentValues.put(ToDoListContract.TODO_LIST.TASK_TYPE, type);
        return contentValues;
    }
}
