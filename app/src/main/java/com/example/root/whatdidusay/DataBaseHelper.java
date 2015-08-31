package com.example.root.whatdidusay;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lalit vasan on 31/8/15.
 * Class DataBaseHelper for handling local data base to store audio recordings
 * All CRUD operations are handled in this class
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "WHAT_DID_YOU_SAY";

    // Contacts table name
    private static final String TABLE_RECORDINGS = "RECORDINGS";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_RECORD_NAME = "record_name";
    private static final String KEY_RECORD_DATE = "record_date";
    private static final String KEY_RECORD_TIME = "record_time";
    private static final String KEY_RECORD_DURATION = "record_duration";
    private static final String KEY_RECORD_PATH = "record_path";

    private final String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_RECORDINGS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_RECORD_NAME + " TEXT,"
            + KEY_RECORD_DATE + " TEXT," + KEY_RECORD_TIME + " TEXT," + KEY_RECORD_DURATION + " TEXT,"
            + KEY_RECORD_PATH + " TEXT" + ")";

    private final String DROP_CONTACT_TABLE = "DROP TABLE IF EXISTS " + TABLE_RECORDINGS;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_CONTACTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(DROP_CONTACT_TABLE);
        onCreate(db);

    }
}
