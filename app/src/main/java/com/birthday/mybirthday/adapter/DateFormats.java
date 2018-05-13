package com.birthday.mybirthday.adapter;

/**
 * Created by Tal Hashahar on 09/05/2018.
 */

public enum DateFormats {
    FORMAT_1("dd/MM/yyyy"),
    FORMAT_2("dd/MM/yy"),
    FORMAT_3("ddMMyyyy"),
    FORMAT_4("dd.MM.yyyy"),
    FORMAT_5("dd.MM.yy"),
    FORMAT_6("YYYY-MM-DD");

    private String mFormat;
    DateFormats(String format) {
        mFormat = format;
    }

    public String getFormat() {
        return mFormat;
    }

    public static String getFormatByType(DateFormats dateFormats) {
        switch (dateFormats){
            case FORMAT_1:
                return FORMAT_1.mFormat;
            case FORMAT_2:
                return FORMAT_2.mFormat;
            case FORMAT_3:
                return FORMAT_3.mFormat;
            case FORMAT_4:
                return FORMAT_4.mFormat;
            case FORMAT_5:
                return FORMAT_5.mFormat;
            default:
                return FORMAT_1.mFormat;

        }
    }
}
