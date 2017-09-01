package com.cerner.a2do.interfaceListeners;

import android.database.Cursor;
import android.view.View;

/**
 * Created by c15333 on 9/1/17.
 */

public interface ListItemClickListener {
    void onDeleteClicked(View view, Cursor cursor);

    void onCardViewClicked(View view, Cursor cursor);
}
