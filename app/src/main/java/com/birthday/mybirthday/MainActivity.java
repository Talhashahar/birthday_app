package com.birthday.mybirthday;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.birthday.mybirthday.adapter.BirthDateItem;
import com.birthday.mybirthday.adapter.BirthDateListAdapter;
import com.birthday.mybirthday.birthdaydatabase.BirthDateDbManager;
import com.birthday.mybirthday.helpers.RecyclerItemTouchHelper;

import java.util.ArrayList;


/**
 * Created by Tal Hashahar on 09/05/2018.
 */

public class MainActivity extends Activity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

    private RecyclerView mRecyclerView;
    private BirthDateListAdapter mAdapter;
    private LinearLayoutManager mLinearLayout;
    private FloatingActionButton mFab;
    private CoordinatorLayout mMainContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        mRecyclerView = (RecyclerView) findViewById(R.id.birth_dates_list);
        mFab = (FloatingActionButton) findViewById(R.id.fab_add_new_date);
        mMainContainer = (CoordinatorLayout) findViewById(R.id.main_content);

        BirthDateDbManager.initDbManager(this);


        init();
    }

    private void init() {
        mLinearLayout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLinearLayout);

        ArrayList<BirthDateItem> list = BirthDateDbManager.getInstance().getAllDates();
        for (BirthDateItem item : list) {
            BirthDateDbManager.getInstance().deleteRow(item);
        }

        BirthDateDbManager.getInstance().insertNewDate(new BirthDateItem("02/10/90", "Tal"));
        BirthDateDbManager.getInstance().insertNewDate(new BirthDateItem("28/02/1961", "Dad"));
        BirthDateDbManager.getInstance().insertNewDate(new BirthDateItem("25/12/1963", "Mom"));
        BirthDateDbManager.getInstance().insertNewDate(new BirthDateItem("10/09/1993", "Rotem"));

        mAdapter = new BirthDateListAdapter(BirthDateDbManager.getInstance().getAllDates(), this);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BirthDateInsertionDialog.build(MainActivity.this).addOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        mAdapter.updateDataSet(BirthDateDbManager.getInstance().getAllDates());
                    }
                }).play();
            }
        });
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof BirthDateListAdapter.BirthDateViewHolder) {
            // get the removed item name to display it in snack bar
            String name = mAdapter.getItem(viewHolder.getAdapterPosition()).getName();

            // backup of removed item for undo purpose
            final BirthDateItem deletedItem = mAdapter.getItem(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());

            BirthDateDbManager.getInstance().deleteRow(deletedItem);
            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(mMainContainer, name + " removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    mAdapter.restoreItem(deletedItem, deletedIndex);
                    BirthDateDbManager.getInstance().insertNewDate(deletedItem);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}
