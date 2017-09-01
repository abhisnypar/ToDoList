package com.cerner.a2do.interfaceListeners;

import android.database.Cursor;
import android.view.View;

/**
 * This interface contains method declarations which serves as
 * communication between fragment and cursor adapter
 */
public interface ListItemClickListener {
    void onDeleteClicked(View view, Cursor cursor);

    void onCardViewClicked(View view, Cursor cursor);
}
