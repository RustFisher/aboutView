package com.rust.contactview;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.rust.aboutview.R;
import com.rust.database.RustDatabaseHelper;

public class PeopleMainActivity extends AppCompatActivity {
    final String CONTACT_TABLE = RustDatabaseHelper.Tables.CONTACT_TABLE;
    final String DATABASE_NAME = RustDatabaseHelper.DATABASE_NAME;
    int fragmentContainerID = R.id.fragment_container;

    RustDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_database);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // get the custom database helper whit database name
        databaseHelper = new RustDatabaseHelper(getApplicationContext(), DATABASE_NAME, null, 1);

        getFragmentManager().beginTransaction()
                .add(fragmentContainerID, new DataListView()).commit();// display data

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(getApplicationContext(), AddDataActivity.class);
                startActivity(i);// jump to add data activity
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // Also we can use toolbar.setOnMenuItemClickListener; but this way needs more code
        int id = item.getItemId();
        if (id == R.id.action_delete_all) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);// build a dialog
            builder.setCancelable(true);
            builder.setMessage(getString(R.string.delete_all_message));
            builder.setPositiveButton(getString(R.string.delete_all_yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SQLiteDatabase db = databaseHelper.getWritableDatabase();
                    db.delete(CONTACT_TABLE, "1", null);
                    getFragmentManager().beginTransaction()
                            .replace(fragmentContainerID, new DataListView()).commit();// refresh data view
                }
            });
            builder.setNegativeButton(getString(R.string.delete_all_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
            return true;
        } else if (id == R.id.action_contact_people_activity) {
            Intent i = new Intent();
            i.setClass(this, ContactPeopleActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}
