package com.birthday.mybirthday.adapter;

import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Tal Hashahar on 09/05/2018.
 */

public class BirthDateItem implements Comparable<BirthDateItem>{

    private long id;
    private String date;
    private String name;

    public BirthDateItem(String date, String name) {
        this.date = date;
        this.name = name;
    }

    public BirthDateItem(Date date, DateFormats dateFormat, String name) {
        setDateWithFormat(date, dateFormat);
        this.name = name;
    }

    public BirthDateItem(long id, String date, String name) {
        this.id = id;
        this.date = date;
        this.name = name;
    }

    private void setDateWithFormat(Date date, DateFormats format) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormats.getFormatByType(format), Locale.US);
        try {
            this.date = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int compareTo(@NonNull BirthDateItem item) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormats.getFormatByType(DateFormats.FORMAT_2), Locale.US);
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        Date date1;
        Date date2;
        try {
            date1 = sdf.parse(this.date);
            date2 = sdf.parse(item.date);

            cal1.setTime(date1);
            cal2.setTime(date2);

        } catch (Exception e) {
            e.printStackTrace();
        }


        int month1 = cal1.get(Calendar.MONTH);
        int month2 = cal2.get(Calendar.MONTH);

        if(month1 < month2)
            return -1;
        else if(month1 == month2)
            return cal1.get(Calendar.DAY_OF_MONTH) - cal2.get(Calendar.DAY_OF_MONTH);

        else return 1;
    }


}
