package com.example.android.inventory;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.inventory.data.ProductContract;

import java.text.DecimalFormat;

/**
 * Created by tali on 06.01.18.
 */

public class ProductCursorAdapter extends CursorAdapter {
    public static final DecimalFormat decimalFormat = new DecimalFormat("0.00");


    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        int nameCol = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
        int priceCol = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE);
        int quantityCol = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);
        int dateCol = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_DATE);
        //get views
        TextView name = view.findViewById(R.id.p_name);
        TextView price = view.findViewById(R.id.p_price);
        TextView quantity = view.findViewById(R.id.p_quantity);
        TextView date = view.findViewById(R.id.p_last_edit);
        //set data
        name.setText(cursor.getString(nameCol));
        price.setText(decimalFormat.format(cursor.getInt(priceCol) / 100.00) + "$");
        quantity.setText(String.valueOf(cursor.getInt(quantityCol)));
        date.setText(cursor.getString(dateCol));

    }
}
