package com.birthday.mybirthday;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.birthday.mybirthday.adapter.BirthDateItem;
import com.birthday.mybirthday.adapter.DateFormats;
import com.birthday.mybirthday.birthdaydatabase.BirthDateDbManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Tal Hashahar on 09/05/2018.
 */

public class BirthDateInsertionDialog extends Dialog {

    private Context mContext;
    final Calendar myCalendar = Calendar.getInstance();

    private FrameLayout mAddButton;
    private EditText mName;
    private EditText mDate;

    public BirthDateInsertionDialog(Context context) {
        super(context, R.style.full_screen_dialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.birth_date_insertion_dialog);

        init();
    }

    private void init() {
        mAddButton = (FrameLayout) findViewById(R.id.add_button_container);
        mName = (EditText) findViewById(R.id.name);
        mDate = (EditText) findViewById(R.id.date);

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(mName.getText()) && !TextUtils.isEmpty(mDate.getText())) {
                    BirthDateDbManager.getInstance().insertNewDate(new BirthDateItem(mDate.getText().toString(), mName.getText().toString()));
                    dismiss();
                }
            }
        });


        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel();
                    }

                }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormats.getFormatByType(DateFormats.FORMAT_2), Locale.US);

        mDate.setText(sdf.format(myCalendar.getTime()));
    }

    public static BirthDateInsertionDialog build(Context context) {
        return new BirthDateInsertionDialog(context);
    }

    public BirthDateInsertionDialog addOnDismissListener(OnDismissListener onDismissListener) {
        setOnDismissListener(onDismissListener);
        return this;
    }

    public void play() {
        show();
    }

}
