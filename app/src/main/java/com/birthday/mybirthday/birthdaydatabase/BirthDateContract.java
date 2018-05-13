package com.birthday.mybirthday.birthdaydatabase;

import android.provider.BaseColumns;

import static com.birthday.mybirthday.birthdaydatabase.BirthDateContract.BirthDates.COLUMN_BIRTH_DATE_TITLE;
import static com.birthday.mybirthday.birthdaydatabase.BirthDateContract.BirthDates.COLUMN_NAME_TITLE;
import static com.birthday.mybirthday.birthdaydatabase.BirthDateContract.BirthDates.TABLE_NAME;

/**
 * Created by Tal Hashahar on 09/05/2018.
 */

public final class BirthDateContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private BirthDateContract() {}

    /* Inner class that defines the table contents */
    public static class BirthDates implements BaseColumns {
        public static final String TABLE_NAME = "BIRTH_DATES";
        public static final String COLUMN_NAME_TITLE = "NAME";
        public static final String COLUMN_BIRTH_DATE_TITLE= "BIRTH_DATE";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    BirthDates._ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_TITLE + " TEXT," +
                    COLUMN_BIRTH_DATE_TITLE + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
}
