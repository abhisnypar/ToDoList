package com.cerner.a2do.contentProvider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.cerner.a2do.database.ToDoListSQLiteOpenHelper;

/**
 * This class provides the methods to store, update and delete data
 */
public class ToDoListContentProvider extends ContentProvider {
    public static final UriMatcher uri_matcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int TODO_LIST = 1;
    private static final int TODO_LIST_ID = 1;

    static {
        uri_matcher.addURI(ToDoListContract.AUTHORITY, ToDoListContract.TODO_LIST.TABLE, TODO_LIST);
        uri_matcher.addURI(ToDoListContract.AUTHORITY, ToDoListContract.TODO_LIST.TABLE + "/#", TODO_LIST_ID);

    }

    private Context context;
    private ContentResolver contentResolver;
    private SQLiteOpenHelper sqLiteOpenHelper;

    @Override
    public boolean onCreate() {
        context = getContext();
        assert context != null;
        contentResolver = context.getContentResolver();
        sqLiteOpenHelper = new ToDoListSQLiteOpenHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (uri_matcher.match(uri)) {
            case TODO_LIST:
                cursor = sqLiteOpenHelper
                        .getReadableDatabase()
                        .query(ToDoListContract.TODO_LIST.TABLE,
                                projection,
                                selection,
                                selectionArgs,
                                null,
                                null,
                                sortOrder);
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown uri: %s", uri));
        }
        cursor.setNotificationUri(context.getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uri_matcher.match(uri)) {
            case TODO_LIST:
                return ToDoListContract.TODO_LIST.DIR_CONTENT_TYPE;
            default:
                throw new IllegalArgumentException(String.format("Unknow uri: %s", uri));
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        Uri newUri;
        long id;
        switch (uri_matcher.match(uri)) {
            case TODO_LIST:
                id = sqLiteOpenHelper
                        .getWritableDatabase()
                        .insert(ToDoListContract.TODO_LIST.TABLE,
                                null,
                                contentValues);
                newUri = ContentUris.withAppendedId(uri, id);
                contentResolver.notifyChange(uri, null);
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown uri: %s", uri));
        }
        return newUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        int affected;
        switch (uri_matcher.match(uri)) {
            case TODO_LIST:
                affected = sqLiteOpenHelper
                        .getWritableDatabase()
                        .update(ToDoListContract.TODO_LIST.TABLE,
                                contentValues,
                                selection,
                                selectionArgs);
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown Uri: %s", uri));
        }
        return affected;
    }
}
