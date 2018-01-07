package com.example.android.inventory;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.inventory.data.ProductContract;

import static com.example.android.inventory.ProductCursorAdapter.decimalFormat;

public class FinanceActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private TextView total;
    private LinearLayout summaryReport;
    private Button detailButton;
    private static final int LOADER_ID = 4;
    private int totalMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance);
        //get View
        total = findViewById(R.id.total);
        detailButton = findViewById(R.id.details_button);
        summaryReport = findViewById(R.id.summary_report);
        summaryReport.setVisibility(View.GONE);

        //fetch data from database
        getLoaderManager().initLoader(LOADER_ID, null, this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        detailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (summaryReport.getVisibility() == View.GONE) {
                    summaryReport.setVisibility(View.VISIBLE);
                } else {
                    summaryReport.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = new String[]{
                ProductContract.ProductEntry.COLUMN_PRODUCT_NAME,
                ProductContract.ProductEntry.COLUMN_PRODUCT_INCOME,
                ProductContract.ProductEntry.COLUMN_PRODUCT_NUMBER_SALE
        };
        return new CursorLoader(this,
                ProductContract.ProductEntry.CONTENT_URI,
                projection,
                null,
                null,
                ProductContract.ProductEntry.COLUMN_PRODUCT_NAME + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        while (cursor.moveToNext()) {
            LinearLayout view = new LinearLayout(this);
            LayoutInflater.from(this).inflate(R.layout.summary_report, view);
            TextView reportName = view.findViewById(R.id.r_name);
            TextView reportNumber = view.findViewById(R.id.r_number);
            TextView reportIncome = view.findViewById(R.id.r_income);

            int colName = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
            int colNumber = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NUMBER_SALE);
            int colIncome = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_INCOME);
            totalMoney += cursor.getInt(colIncome);

            reportName.setText(cursor.getString(colName));
            reportNumber.setText(String.valueOf(cursor.getInt(colNumber)));
            reportIncome.setText(decimalFormat.format(cursor.getInt(colIncome) / 100.00));

            summaryReport.addView(view);
        }
        total.setText(decimalFormat.format(totalMoney / 100.00) + "$");
        getLoaderManager().destroyLoader(LOADER_ID);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
