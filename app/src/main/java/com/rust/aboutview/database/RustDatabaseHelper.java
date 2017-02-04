package com.rust.aboutview.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RustDatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "data.db";

    public interface Tables {
        String CONTACT_TABLE = "contact";
    }

    public interface Contact {
        String _ID = "contact_id";
        String NAME = "name";
        String PHONE = "phone";
        String EMAIL = "email";
    }

    public RustDatabaseHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String cmd = "CREATE TABLE IF NOT EXISTS " + Tables.CONTACT_TABLE + "("
                + Contact._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Contact.NAME + " VARCHAR, "
                + Contact.PHONE + " VARCHAR, "
                + Contact.EMAIL + " VARCHAR);";
        db.execSQL(cmd);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
