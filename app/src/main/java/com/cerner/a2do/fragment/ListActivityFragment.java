package com.cerner.a2do.fragment;

import android.app.AlertDialog;
import android.content.AsyncQueryHandler;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cerner.a2do.R;
import com.cerner.a2do.adapter.CustomListAdapter;
import com.cerner.a2do.adapter.ToDoListCursorAdapter;
import com.cerner.a2do.contentProvider.ToDoListContract;
import com.cerner.a2do.database.ToDoListSQLiteOpenHelper;
import com.cerner.a2do.interfaceListeners.ListItemClickListener;
import com.cerner.a2do.util.NotifyingAsyncQueryHandler;

import java.util.ArrayList;
import java.util.Locale;

/**
 * A placeholder fragment containing a simple view.
 */
public class ListActivityFragment extends Fragment implements NotifyingAsyncQueryHandler.AsyncQueryListener, LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener, ListItemClickListener{
    public static String EDIT_TAG = "edit_button";
    public static String DESCRIPTION_TAG = "description_tag";
    private CustomListAdapter customListAdapter;
    protected ToDoListCursorAdapter cursorAdapter;
    private ListView listView;
    private AsyncQueryHandler asyncQueryHandler;
    private SQLiteDatabase sqLiteDatabase;
    private SQLiteOpenHelper sqLiteOpenHelper;

    private static final int TODO_LIST_LOADER = 0;
    private static final int INSERT_LIST_TOKEN = 1;


    public ListActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        asyncQueryHandler = new NotifyingAsyncQueryHandler(getContext(), this);
        sqLiteOpenHelper = new ToDoListSQLiteOpenHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ArrayList<String> arrayList = new ArrayList<String>();
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        listView = rootView.findViewById(R.id.list_view);
        cursorAdapter = new ToDoListCursorAdapter(getContext(), null, 0, this);
        arrayList.add(getTag());
        customListAdapter = new CustomListAdapter(getContext(), getTag(), arrayList);
        listView.setAdapter(cursorAdapter);
        customListAdapter.notifyDataSetChanged();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            getLoaderManager().initLoader(TODO_LIST_LOADER, null, this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();
        String query = "SELECT COUNT (*)" + "FROM " + ToDoListContract.TODO_LIST.TABLE + "'";
        try {
            Cursor cursor = sqLiteDatabase.rawQuery(query, null);
            if (cursor != null) {
                asyncQueryHandler.startQuery(INSERT_LIST_TOKEN,
                        null,
                        ToDoListContract.TODO_LIST.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);
            }
        } catch (SQLException s) {
            s.printStackTrace();
        }
    }

    @Override
    public void onInsertComplete(int token, Object cookie, Uri uri) {

    }

    @Override
    public void onQueryComplete(int token, Object cookie, Cursor cursor) {
        sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();
        String query = "SELECT COUNT (*)" + "FROM " + ToDoListContract.TODO_LIST.TABLE + "'";
        cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        cursorAdapter.notifyDataSetChanged();
    }

    @Override
    public void onUpdateComplete(int token, Object cookie, int result) {

    }

    @Override
    public void onDeleteComplete(int token, Object cookie, int result) {
        cursorAdapter.notifyDataSetChanged();
    }


    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case TODO_LIST_LOADER:
                return new android.support.v4.content.CursorLoader(getContext(),
                        ToDoListContract.TODO_LIST.CONTENT_URI,
                        ToDoListContract.TODO_LIST.LIST_PROJECTION,
                        null,
                        null,
                        null
                );
            default:
                throw new IllegalArgumentException(String.format(Locale.US, "Unknown loader id: %d", id));
        }
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case TODO_LIST_LOADER:
                cursorAdapter.swapCursor(data);
                break;

            default:
                throw new IllegalArgumentException(String.format(Locale.US, "Unknown loader id: %d", loader.getId()));
        }

    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.changeCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
    }

    @Override
    public void onDeleteClicked(View view, final Cursor cursor) {
        if (cursor != null) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
            alertDialog.setTitle(R.string.delete_dialog_title);
            alertDialog.setMessage(R.string.delete_dialog_message);
            alertDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String item = cursor.getString(cursor.getColumnIndex(ToDoListContract.TODO_LIST.TASK_TITLE));
                    asyncQueryHandler.startDelete(INSERT_LIST_TOKEN, null, ToDoListContract.TODO_LIST.CONTENT_URI, ToDoListContract.TODO_LIST.TASK_TITLE + " = ? ", new String[]{item});

                }
            });
            alertDialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            alertDialog.setCancelable(false);
            alertDialog.create().show();

        }
    }

    @Override
    public void onCardViewClicked(View view, Cursor cursor) {
        AddItemFragment addItemFragment = new AddItemFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("ID", cursor.getInt(cursor.getColumnIndex(ToDoListContract.TODO_LIST._ID)));
        bundle.putString(EDIT_TAG, cursor.getString(cursor.getColumnIndex(ToDoListContract.TODO_LIST.TASK_TITLE)));
        bundle.putString(DESCRIPTION_TAG, cursor.getString(cursor.getColumnIndex(ToDoListContract.TODO_LIST.TASK_DESCRIPTION)));
        addItemFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.list_fragment_activity, addItemFragment, "EDITED").commit();
    }
}