package com.birthday.mybirthday.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.birthday.mybirthday.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Tal Hashahar on 09/05/2018.
 */

public class BirthDateListAdapter extends RecyclerView.Adapter<BirthDateListAdapter.BirthDateViewHolder> {

    private ArrayList<BirthDateItem> mItems;
    private Context mContext;

    public BirthDateListAdapter(ArrayList<BirthDateItem> items, Context context) {
        mItems = items;
        sortNextBirthDates();
        mContext = context;
    }

    @Override
    public BirthDateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView =LayoutInflater.from(mContext).inflate(R.layout.birth_day_card_item, parent, false);
        return new BirthDateViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BirthDateViewHolder holder, int position) {
        BirthDateItem item = mItems.get(position);
        holder.mName.setText(item.getName());
        holder.mDate.setText(item.getDate());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class BirthDateViewHolder extends RecyclerView.ViewHolder {
        private TextView mDate;
        private TextView mName;
        public RelativeLayout viewBackground, viewForeground;

        public BirthDateViewHolder(View itemView) {
            super(itemView);
            mDate = itemView.findViewById(R.id.birth_date);
            mName = itemView.findViewById(R.id.name);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);
        }
    }

    public void removeItem(int position) {
        mItems.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(BirthDateItem item, int position) {
        mItems.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    public void updateDataSet(ArrayList<BirthDateItem> items) {
        mItems = items;
        sortNextBirthDates();
        notifyDataSetChanged();
    }

    private void sortNextBirthDates() {
        Collections.sort(mItems);
        ArrayList<BirthDateItem> firstPart = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        calendar.setTime(date);

        ArrayList<BirthDateItem> list = new ArrayList<>();
        list.addAll(mItems);
        for(BirthDateItem item : list) {
            SimpleDateFormat sdf = new SimpleDateFormat(DateFormats.getFormatByType(DateFormats.FORMAT_2), Locale.US);
            try {
                Date birthDate = sdf.parse(item.getDate());
                Calendar birthCal = Calendar.getInstance();
                birthCal.setTime(birthDate);


                int todayMonth = calendar.get(Calendar.MONTH);
                int birthMonth = birthCal.get(Calendar.MONTH);

                if(todayMonth < birthMonth) {
                    firstPart.add(item);
                    mItems.remove(item);
                } else if(todayMonth == birthMonth) {
                    if(birthCal.get(Calendar.DAY_OF_MONTH) - calendar.get(Calendar.DAY_OF_MONTH) > 0) {
                        firstPart.add(item);
                        mItems.remove(item);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        firstPart.addAll(mItems);
        mItems = firstPart;
    }

    public BirthDateItem getItem(int position) {
        return mItems.get(position);
    }
}
