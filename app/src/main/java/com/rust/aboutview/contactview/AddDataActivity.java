package com.rust.aboutview.contactview;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.rust.aboutview.R;
import com.rust.aboutview.database.RustDatabaseHelper;

public class AddDataActivity extends AppCompatActivity {

    public static final String ITEM_ID = "select_item_id";
    public static final String ITEM_OPERATION_TYPE = "edit_or_add_item";
    public static final int ADD_ITEM = 1;
    public static final int EDIT_ITEM = 2;

    final String CONTACT_TABLE = RustDatabaseHelper.Tables.CONTACT_TABLE;
    final String CONTACT_ID = RustDatabaseHelper.Contact._ID;
    final String CONTACT_NAME = RustDatabaseHelper.Contact.NAME;
    final String CONTACT_EMAIL = RustDatabaseHelper.Contact.EMAIL;
    final String CONTACT_PHONE = RustDatabaseHelper.Contact.PHONE;

    int currentID;
    Button doneAddData;
    EditText editName;
    EditText editPhone;
    EditText editEmail;
    RustDatabaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        final boolean editItem = intent.getIntExtra(ITEM_OPERATION_TYPE, ADD_ITEM) == EDIT_ITEM;
        setContentView(R.layout.activity_add_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.add_data_toolbar);
        doneAddData = (Button) findViewById(R.id.done_add_data_button);
        editName = (EditText) findViewById(R.id.add_name);
        editPhone = (EditText) findViewById(R.id.add_phone);
        editEmail = (EditText) findViewById(R.id.add_email);

        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_24dp);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(toolbar);

        database = new RustDatabaseHelper(this, RustDatabaseHelper.DATABASE_NAME, null, 1);

        if (editItem) {
            setTitle(getString(R.string.title_edit_data));
            doneAddData.setText(getString(R.string.done_edit_data));
            String id = intent.getStringExtra(ITEM_ID);// long click item id
            SQLiteDatabase dbE = database.getReadableDatabase();// ust to Edit
            /* insert all the data, or can't find the column and got the -1 */
            Cursor cursor = dbE.query(CONTACT_TABLE,
                    new String[]{CONTACT_ID, CONTACT_NAME, CONTACT_PHONE, CONTACT_EMAIL},
                    CONTACT_ID + "=?", new String[]{id}, null, null, null);
            int nameIndex = cursor.getColumnIndex(CONTACT_NAME);
            int phoneIndex = cursor.getColumnIndex(CONTACT_PHONE);
            int emailIndex = cursor.getColumnIndex(CONTACT_EMAIL);
            while (cursor.moveToNext()) {
                currentID = cursor.getInt(cursor.getColumnIndex(CONTACT_ID));
                editName.setText(cursor.getString(nameIndex));
                editPhone.setText(cursor.getString(phoneIndex));
                editEmail.setText(cursor.getString(emailIndex));
            }
            cursor.close();
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        doneAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString();
                if (noName(name)) {
                    finish();
                    return;// no name, don't save this item
                }
                String phone = editPhone.getText().toString();
                String email = editEmail.getText().toString();
                ContentValues values = new ContentValues();
                values.put(CONTACT_NAME, name);
                values.put(CONTACT_PHONE, TextUtils.isEmpty(phone) ? "" : phone);
                values.put(CONTACT_EMAIL, TextUtils.isEmpty(email) ? "" : email);
                SQLiteDatabase db = database.getWritableDatabase();

                if (editItem) {
                    db.update(CONTACT_TABLE, values, CONTACT_ID + "=?", new String[]{String.valueOf(currentID)});
                } else {
                    db.insert(CONTACT_TABLE, null, values);
                }

                db.close();
                finish();

            }
        });

        doneAddData.setEnabled(!TextUtils.isEmpty(editName.getText()));// no name, can't add item

        editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                doneAddData.setEnabled(!TextUtils.isEmpty(s));
            }
        });

    }

    private boolean noName(String name) {
        return TextUtils.isEmpty(name.trim());
    }

}
