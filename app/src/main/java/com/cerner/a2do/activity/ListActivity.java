package com.cerner.a2do.activity;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cerner.a2do.R;
import com.cerner.a2do.database.ToDoListSQLiteOpenHelper;
import com.cerner.a2do.fragment.AddItemFragment;
import com.cerner.a2do.fragment.ListActivityFragment;

/*
* This Activity displays the to do items in the list view*/

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFloatingButtonClicked(view);
            }
        });
        ListActivityFragment listActivityFragment = new ListActivityFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.list_fragment_activity, listActivityFragment).commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * On Floating button clicked.
     *
     * @param view
     */
    public void onFloatingButtonClicked(View view) {
        AddItemFragment addItemFragment = new AddItemFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.list_fragment_activity, addItemFragment).addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
