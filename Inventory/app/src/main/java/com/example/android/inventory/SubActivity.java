package com.example.android.inventory;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.inventory.data.ProductContract;

import java.util.regex.Pattern;

import static com.example.android.inventory.ProductCursorAdapter.decimalFormat;

public class SubActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private EditText name;
    private EditText price;
    private EditText quantity;
    private Button minus;
    private Button plus;
    private EditText supplier;
    private EditText orderDetail;
    private Spinner orderMethod;
    private FloatingActionButton imageButton;
    private ImageView image;
    private Button order;
    private int spinnerValue = ProductContract.ProductEntry.ORDER_PHONE;
    private int quantityValue;
    private final String decimalPatter = "([0-9]+)\\.([0-9]{2})";
    private Uri uriPass;
    private static final int LOADER_ID = 2;
    private boolean productChanged = false;
    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            productChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        name = findViewById(R.id.s_name);
        price = findViewById(R.id.s_price);
        quantity = findViewById(R.id.s_quantity);
        minus = findViewById(R.id.minus);
        plus = findViewById(R.id.plus);
        supplier = findViewById(R.id.s_supplier);
        orderDetail = findViewById(R.id.s_order_data);
        orderMethod = findViewById(R.id.s_spinner);
        imageButton = findViewById(R.id.s_image_button);
        image = findViewById(R.id.s_image);
        order = findViewById(R.id.s_order);

        //set up touch listener
        name.setOnTouchListener(touchListener);
        price.setOnTouchListener(touchListener);
        quantity.setOnTouchListener(touchListener);
        minus.setOnTouchListener(touchListener);
        plus.setOnTouchListener(touchListener);
        supplier.setOnTouchListener(touchListener);
        orderDetail.setOnTouchListener(touchListener);
        orderMethod.setOnTouchListener(touchListener);
        imageButton.setOnTouchListener(touchListener);

        setupSpinner();
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quantityV = quantity.getText().toString();
                quantityValue = Integer.valueOf(quantityV);
                if (TextUtils.isEmpty(quantityV) || (quantityValue == 0)) {
                    Toast.makeText(SubActivity.this, getString(R.string.can_not_decrease_quantity), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    quantityValue--;
                    quantity.setText(String.valueOf(quantityValue));
                }
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quantityV = quantity.getText().toString();
                quantityValue = Integer.valueOf(quantityV);
                if (TextUtils.isEmpty(quantityV)) {
                    Toast.makeText(SubActivity.this, getString(R.string.can_not_decrease_quantity), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    quantityValue++;
                    quantity.setText(String.valueOf(quantityValue));
                }
            }
        });
        Intent intent = getIntent();
        uriPass = intent.getData();
        if (uriPass != null) {
            getSupportActionBar().setTitle(getString(R.string.edit_mode));
            getLoaderManager().initLoader(LOADER_ID, null, this);
        } else {
            invalidateOptionsMenu();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (uriPass == null) {
            MenuItem menuItem = menu.findItem(R.id.delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    private void setupSpinner() {
        ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.spinner_order,
                android.R.layout.simple_spinner_dropdown_item
        );
        orderMethod.setAdapter(spinnerAdapter);
        orderMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selection = (String) adapterView.getItemAtPosition(i);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.email))) {
                        spinnerValue = ProductContract.ProductEntry.ORDER_EMAIL;
                    } else if (selection.equals(getString(R.string.web))) {
                        spinnerValue = ProductContract.ProductEntry.ORDER_WEB;
                    } else {
                        spinnerValue = ProductContract.ProductEntry.ORDER_PHONE;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sub_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                break;
            case R.id.done:
                //call insert
                if (insertProduct()) {
                    finish();
                }
                break;
            case android.R.id.home:
                if (productChanged) {
                    showUnsaveChangesDialog();
                } else {
                    NavUtils.navigateUpFromSameTask(this);
                }
        }
        return true;
    }

    private void showUnsaveChangesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.unsave_message));
        builder.setPositiveButton(getString(R.string.stay), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (dialogInterface != null) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.setNegativeButton(getString(R.string.keep_exist), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (!productChanged) {
            super.onBackPressed();
            return;
        } else {
            showUnsaveChangesDialog();
        }
    }

    private boolean insertProduct() {
        String nameV = name.getText().toString().trim();
        String priceV = price.getText().toString().trim();
        String supplierV = supplier.getText().toString().trim();
        String orderDetailV = orderDetail.getText().toString().trim();
        String quantityV = quantity.getText().toString().trim();

        if (TextUtils.isEmpty(nameV)) {
            Toast.makeText(SubActivity.this, getString(R.string.fill_name), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(supplierV)) {
            Toast.makeText(SubActivity.this, getString(R.string.fill_supplier), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(orderDetailV)) {
            Toast.makeText(SubActivity.this, getString(R.string.fill_details), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Pattern.matches(decimalPatter, priceV)) {
            Toast.makeText(SubActivity.this, getString(R.string.fill_price), Toast.LENGTH_SHORT).show();
            return false;
        }


        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, nameV);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE, (int) (Double.parseDouble(priceV) * 100));
        if (!TextUtils.isEmpty(quantityV)) {
            values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, Integer.valueOf(quantityV));
        }
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER, supplierV);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_ORDER_DETAIL, orderDetailV);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_ORDER_METHOD, spinnerValue);

        if (uriPass == null) {
            Uri uri = getContentResolver().insert(ProductContract.ProductEntry.CONTENT_URI, values);
            if (uri == null) {
                // If the row ID is -1, then there was an error with insertion.
                Toast.makeText(this, "Error with saving this product", Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast with the row ID.
                Toast.makeText(this, "Successful insert product with uri of : " + uri, Toast.LENGTH_SHORT).show();
            }
        } else {
            //update
            int rowUpdated = getContentResolver().update(uriPass, values, null, null);
            if (rowUpdated == 0) {
                // If the row ID is -1, then there was an error with insertion.
                Toast.makeText(this, "Error with updating product", Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast with the row ID.
                Toast.makeText(this, "Successful updating : " + rowUpdated + " product(s)", Toast.LENGTH_SHORT).show();
            }
        }

        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = new String[]{
                ProductContract.ProductEntry._ID,
                ProductContract.ProductEntry.COLUMN_PRODUCT_NAME,
                ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductContract.ProductEntry.COLUMN_PRODUCT_INCOME,
                ProductContract.ProductEntry.COLUMN_PRODUCT_NUMBER_SALE,
                ProductContract.ProductEntry.COLUMN_PRODUCT_DATE,
                ProductContract.ProductEntry.COLUMN_PRODUCT_ORDER_DETAIL,
                ProductContract.ProductEntry.COLUMN_PRODUCT_ORDER_METHOD,
                ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER,
                ProductContract.ProductEntry.COLUMN_PRODUCT_IMAGE
        };
        return new CursorLoader(this,
                uriPass,
                projection,
                null,
                null,
                null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            int colName = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
            int colPrice = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE);
            int colQuantity = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);
            int colSupplier = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER);
            int colOrderMethod = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_ORDER_METHOD);
            int colOrderdetails = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_ORDER_DETAIL);
            name.setText(cursor.getString(colName));
            price.setText(decimalFormat.format(cursor.getInt(colPrice) / 100.00));
            quantity.setText(String.valueOf(cursor.getInt(colQuantity)));
            supplier.setText(cursor.getString(colSupplier));
            orderDetail.setText(cursor.getString(colOrderdetails));
            switch (cursor.getInt(colOrderMethod)) {
                case ProductContract.ProductEntry.ORDER_PHONE:
                    orderMethod.setSelection(0);
                case ProductContract.ProductEntry.ORDER_EMAIL:
                    orderMethod.setSelection(1);
                default:
                    orderMethod.setSelection(2);

            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        name.setText("");
        price.setText("");
        quantity.setText("");
        supplier.setText("");
        orderMethod.setSelection(0);
        orderDetail.setText("");
    }
}
