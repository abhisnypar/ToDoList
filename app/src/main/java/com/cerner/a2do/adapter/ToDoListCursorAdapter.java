package com.cerner.a2do.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cerner.a2do.R;
import com.cerner.a2do.contentProvider.ToDoListContract;
import com.cerner.a2do.interfaceListeners.ListItemClickListener;

/**
 * Created by c15333 on 8/31/17.
 */

public class ToDoListCursorAdapter extends CursorAdapter {
    Context context;
    private LayoutInflater layoutInflater;
    private TextView textView;
    private ListItemClickListener listItemClickListener;

    public ToDoListCursorAdapter(Context context, Cursor c, int o, ListItemClickListener listItemClickListener) {
        super(context, c, o);
        this.context = context;
        this.listItemClickListener = listItemClickListener;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return layoutInflater.inflate(R.layout.list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        if (cursor != null) {
            textView = view.findViewById(R.id.list_item_textView);
            Button button = view.findViewById(R.id.delete_button);
            String taskDescription = cursor.getString(cursor.getColumnIndex(ToDoListContract.TODO_LIST.TASK_DESCRIPTION));
            textView.setText(taskDescription);
           button.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   listItemClickListener.onDeleteClicked(view, cursor);
               }
           });
        }

        LinearLayout linearLayout = view.findViewById(R.id.list_item_linearlayout);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listItemClickListener.onCardViewClicked(view, cursor);
            }
        });
    }
}
