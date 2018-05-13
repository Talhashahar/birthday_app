package com.birthday.mybirthday.birthdaydatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.birthday.mybirthday.adapter.BirthDateItem;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.birthday.mybirthday.birthdaydatabase.BirthDateContract.BirthDates.COLUMN_BIRTH_DATE_TITLE;
import static com.birthday.mybirthday.birthdaydatabase.BirthDateContract.BirthDates.COLUMN_NAME_TITLE;
import static com.birthday.mybirthday.birthdaydatabase.BirthDateContract.BirthDates.TABLE_NAME;

/**
 * Created by Tal Hashahar on 09/05/2018.
 */

public class BirthDateDbManager {

    private static final String TAG = "BirthDateDbManager";
    private static BirthDateDbManager mInstance;
    private BirthDatesDbHelper mDbHelper;

    private BirthDateDbManager(Context context) {
        mDbHelper = new BirthDatesDbHelper(context);
    }


    public static BirthDateDbManager getInstance() {
        if (mInstance == null) {
            Log.e(TAG, "Use initDbManager method before you call this method");
        }
        return mInstance;
    }

    public static void initDbManager(Context context) {
        if (mInstance == null) {
            synchronized (BirthDateDbManager.class) {
                if (mInstance == null) {
                    mInstance = new BirthDateDbManager(context);
                }
            }
        }
    }

    public void insertNewDate(BirthDateItem item) {
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_TITLE, item.getName());
        values.put(COLUMN_BIRTH_DATE_TITLE, item.getDate());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TABLE_NAME, null, values);
    }

    public void deleteRow(BirthDateItem item) {
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Define 'where' part of query.
        String selection = _ID + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {String.valueOf(item.getId())};
        // Issue SQL statement.
        int deletedRows = db.delete(TABLE_NAME, selection, selectionArgs);
    }

    public void updateName(BirthDateItem item, String name) {
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_TITLE, name);

        // Which row to update, based on the title
        String selection = _ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(item.getId())};

        int count = db.update(
                TABLE_NAME,
                values,
                selection,
                selectionArgs);

    }

    public void updateDate(BirthDateItem item, String date) {
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(COLUMN_BIRTH_DATE_TITLE, date);

        // Which row to update, based on the title
        String selection = _ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(item.getId())};

        int count = db.update(
                TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public ArrayList<BirthDateItem> getAllDates() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        ArrayList<BirthDateItem> list = new ArrayList<>();

        Cursor  cursor = db.rawQuery("select * from " + TABLE_NAME,null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {

                String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TITLE));
                String date = cursor.getString(cursor.getColumnIndex(COLUMN_BIRTH_DATE_TITLE));
                long id = cursor.getLong(cursor.getColumnIndex(_ID));

                list.add(new BirthDateItem(id, date, name));
                cursor.moveToNext();
            }
        }

        return list;
    }

    public ArrayList<BirthDateItem> getSorted() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        ArrayList<BirthDateItem> list = new ArrayList<>();
        final Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + "ORDER BY SUBSTR(DATE('NOW'), 6)>(SUBSTR(" + COLUMN_BIRTH_DATE_TITLE + ", 4, 3) || SUBSTR(" + COLUMN_BIRTH_DATE_TITLE + ", 1, 2)), (SUBSTR(" + COLUMN_BIRTH_DATE_TITLE + ", 4, 3) || SUBSTR(" + COLUMN_BIRTH_DATE_TITLE + ", 1, 2))", null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {

                String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TITLE));
                String date = cursor.getString(cursor.getColumnIndex(COLUMN_BIRTH_DATE_TITLE));
                long id = cursor.getLong(cursor.getColumnIndex(_ID));

                list.add(new BirthDateItem(id, date, name));
                cursor.moveToNext();
            }
        }

        return list;
    }
}