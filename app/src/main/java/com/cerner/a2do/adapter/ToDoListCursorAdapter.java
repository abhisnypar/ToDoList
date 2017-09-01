package com.cerner.a2do.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.cerner.a2do.R;
import com.cerner.a2do.contentProvider.ToDoListContract;

/**
 * Created by c15333 on 8/31/17.
 */

public class ToDoListCursorAdapter extends CursorAdapter {
    Context context;
    private LayoutInflater layoutInflater;
    private TextView textView;

    public ToDoListCursorAdapter(Context context, Cursor c, int o) {
        super(context, c, o);
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return layoutInflater.inflate(R.layout.list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if (cursor != null) {
            textView = view.findViewById(R.id.list_item_textView);
            String taskDescription = cursor.getString(cursor.getColumnIndex(ToDoListContract.TODO_LIST.TASK_DESCRIPTION));
            textView.setText(taskDescription);
        }
    }
}
