package com.marshallepie.root.whatdidusay.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.marshallepie.root.whatdidusay.Models.ModelRecording;

import java.util.ArrayList;

/**
 * Created by Dottechnologies on 31/8/15.
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
    private static final String KEY_RECORD_FOLDER = "record_folder";

    private final String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_RECORDINGS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_RECORD_NAME + " TEXT,"
            + KEY_RECORD_DATE + " TEXT," + KEY_RECORD_TIME + " TEXT," + KEY_RECORD_DURATION + " TEXT,"
            + KEY_RECORD_FOLDER + " TEXT," + KEY_RECORD_PATH + " TEXT" + ")";

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

    public void addRecord(ModelRecording record) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_RECORD_NAME, record.getName());
        values.put(KEY_RECORD_DATE, record.getDate());
        values.put(KEY_RECORD_TIME, record.getTime());
        values.put(KEY_RECORD_DURATION, record.getDuration());
        values.put(KEY_RECORD_PATH, record.getPath());
        values.put(KEY_RECORD_FOLDER, record.getFolder());
        db.insert(TABLE_RECORDINGS, null, values);
        db.close();
    }

    public ArrayList<ModelRecording> getAllRecords() {
        ArrayList<ModelRecording> recordtList = new ArrayList<ModelRecording>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RECORDINGS;
        // String selectQuery = "SELECT  * FROM " + TABLE_RECORDINGS +" ORDER BY "+ KEY_ID+" DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ModelRecording record = new ModelRecording();
                record.setId(cursor.getString(cursor.getColumnIndex(KEY_ID)));
                record.setName(cursor.getString(cursor.getColumnIndex(KEY_RECORD_NAME)));
                record.setDate(cursor.getString(cursor.getColumnIndex(KEY_RECORD_DATE)));
                record.setTime(cursor.getString(cursor.getColumnIndex(KEY_RECORD_TIME)));
                record.setPath(cursor.getString(cursor.getColumnIndex(KEY_RECORD_PATH)));
                record.setDuration(cursor.getString(cursor.getColumnIndex(KEY_RECORD_DURATION)));
                record.setIsForDelete(false);
                record.setIsPlaying(false);

                recordtList.add(record);
            } while (cursor.moveToNext());
        }


        return recordtList;
    }

    public void deleteRecordFolder(String folderName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RECORDINGS, KEY_RECORD_FOLDER + " = ?",
                new String[]{String.valueOf(folderName)});
    }

    public ArrayList<ModelRecording> getAllRecordsFromFolder(String folderName) {
        ArrayList<ModelRecording> recordtList = new ArrayList<ModelRecording>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RECORDINGS + " where " + KEY_RECORD_FOLDER + " = '" + folderName + "'" + " ORDER BY " + KEY_ID + " DESC";
        // String selectQuery = "SELECT  * FROM " + TABLE_RECORDINGS +" ORDER BY "+ KEY_ID+" DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ModelRecording record = new ModelRecording();
                record.setId(cursor.getString(cursor.getColumnIndex(KEY_ID)));
                record.setName(cursor.getString(cursor.getColumnIndex(KEY_RECORD_NAME)));
                record.setDate(cursor.getString(cursor.getColumnIndex(KEY_RECORD_DATE)));
                record.setTime(cursor.getString(cursor.getColumnIndex(KEY_RECORD_TIME)));
                record.setPath(cursor.getString(cursor.getColumnIndex(KEY_RECORD_PATH)));
                record.setFolder(cursor.getString(cursor.getColumnIndex(KEY_RECORD_FOLDER)));
                record.setDuration(cursor.getString(cursor.getColumnIndex(KEY_RECORD_DURATION)));
                record.setIsForDelete(false);
                record.setIsPlaying(false);

                recordtList.add(record);
            } while (cursor.moveToNext());
        }


        return recordtList;
    }

    public void updateName(String id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_RECORD_NAME, name);
        db.update(TABLE_RECORDINGS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteContact(String ids) {
        String allid = ids;
        allid = allid.replace("[", "");
        allid = allid.replace("]", "");

        Log.e("Ids", "" + allid);
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(String.format("DELETE FROM " + TABLE_RECORDINGS + " WHERE " + KEY_ID + " IN (%s);", allid));
        db.close();
    }

    public int getLastRecordIdenty() {

        String selectQuery = "SELECT last_insert_rowid()";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        return cursor.getInt(0);
    }

    public int getHighestID() {
        SQLiteDatabase db = this.getReadableDatabase();
        final String MY_QUERY = "SELECT last_insert_rowid()";
        Cursor cur = db.rawQuery(MY_QUERY, null);
        cur.moveToFirst();
        int ID = cur.getInt(0);
        cur.close();
        return ID;
    }

    public int getLastRecordId() {

        int index = 0;
        String selectQuery = "SELECT  * FROM " + TABLE_RECORDINGS + " ORDER BY " + KEY_ID + " DESC LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

            index = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID)));

        } else {
            SQLiteDatabase db1 = this.getWritableDatabase();

            db1.delete(TABLE_RECORDINGS, null, null);
            db1.delete("SQLITE_SEQUENCE", "NAME = ?", new String[]{TABLE_RECORDINGS});
            db1.close();
        }


        return index;

    }

    public int getRecordCount() {
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RECORDINGS, null);
        int i = cursor.getCount();
        count = i;
        cursor.close();
        db.close();

        return count;
    }


}
