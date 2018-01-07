package com.example.android.inventory;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.android.inventory.data.ProductContract;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    //private FloatingActionButton add;
    private static final int LOADER_ID = 1;
    private static final int LOADER_ID_ITEM = 3;
    private ListView listView;
    private ProductCursorAdapter adapter;
    private RelativeLayout emptyview;
    private Uri uriPass;
    private String sortOrder = ProductContract.ProductEntry.COLUMN_PRODUCT_NAME + " ASC";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        FloatingActionButton add = findViewById(R.id.add_button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SubActivity.class);
                startActivity(intent);

            }

        });
        listView = findViewById(R.id.list);
        emptyview = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyview);
        adapter = new ProductCursorAdapter(this, null);
        listView.setAdapter(adapter);
        getLoaderManager().initLoader(LOADER_ID, null, this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                final Uri uri = ContentUris.withAppendedId(ProductContract.ProductEntry.CONTENT_URI, id);
                View option = view.findViewById(R.id.p_option);
                MenuBuilder builder = new MenuBuilder(MainActivity.this);
                new MenuInflater(MainActivity.this).inflate(R.menu.item_menu, builder);
                MenuPopupHelper options = new MenuPopupHelper(MainActivity.this, builder, option);
                options.setForceShowIcon(true);
                builder.setCallback(new MenuBuilder.Callback() {
                    @Override
                    public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_delete:
                                showDeleteConfirmDialog(uri, getString(R.string.delete_dialog_msg_item));
                                break;
                            case R.id.item_edit:
                                Intent intent = new Intent(MainActivity.this, SubActivity.class);
                                intent.setData(uri);
                                startActivity(intent);
                                break;
                            case R.id.item_sale:
                                uriPass = uri;
                                reload(LOADER_ID_ITEM);
                                break;
                            default:
                                break;
                        }
                        return true;
                    }

                    @Override
                    public void onMenuModeChange(MenuBuilder menu) {

                    }
                });
                options.show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all:
                showDeleteConfirmDialog(ProductContract.ProductEntry.CONTENT_URI, getString(R.string.delete_dialog_msg));
                return true;
            case R.id.finance:
                Intent intent = new Intent(MainActivity.this, FinanceActivity.class);
                startActivity(intent);
                return true;
            case R.id.about:
                showAboutDialog();
                return true;
            case R.id.feedback:
                showFeedbackDialog();
                return true;
            case R.id.sample:
                insertSampleProduct();
            case R.id.setting:
                showSettingDialog();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSettingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.sort_by)
                .setItems(R.array.sort_option, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            //alphabetical
                            sortOrder = ProductContract.ProductEntry.COLUMN_PRODUCT_NAME + " ASC";
                        } else {
                            //date
                            sortOrder = ProductContract.ProductEntry.COLUMN_PRODUCT_DATE + " ASC";
                        }
                        reload(LOADER_ID);
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void insertSampleProduct() {
        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, "test product");
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER, "test supplier");
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_ORDER_DETAIL, "http://udacity.com");
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, 10);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE, 1234);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_ORDER_METHOD, ProductContract.ProductEntry.ORDER_WEB);
        getContentResolver().insert(ProductContract.ProductEntry.CONTENT_URI, values);
    }

    private void showFeedbackDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.feedback, null);
        final EditText user = view.findViewById(R.id.f_name_enter);
        final EditText feedback = view.findViewById(R.id.f_feedback_enter);
        builder.setTitle(R.string.f_title)
                .setView(view)
                .setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("*/*");
                        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"caothivananh98@gmail.com"});
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for Inventory App from " + user.getText().toString());
                        intent.putExtra(Intent.EXTRA_TEXT, feedback.getText().toString());
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                        dialogInterface.cancel();
                    }
                });
        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.about_message);
        builder.setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmDialog(final Uri uri, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteProducts(uri);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteProducts(Uri uri) {
        int rowDeleted = getContentResolver().delete(uri, null, null);
        if (rowDeleted == 0) {
            Toast.makeText(this, "Error deleting all products", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Successfully deleting  " + rowDeleted + " product(s)", Toast.LENGTH_SHORT).show();
        }
    }


    private void reload(int id) {
        if (getLoaderManager().getLoader(id) == null) {
            getLoaderManager().initLoader(id, null, this);
        } else {
            getLoaderManager().restartLoader(id, null, this);
        }
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
        if (i == LOADER_ID_ITEM) {
            return new CursorLoader(this,
                    uriPass,
                    projection,
                    null,
                    null,
                    null);
        }
        return new CursorLoader(this,
                ProductContract.ProductEntry.CONTENT_URI,
                projection,
                null,
                null,
                sortOrder);


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (loader.getId() == LOADER_ID_ITEM) {
            //query and update data
            updateSalesData(cursor);
        } else {
            adapter.swapCursor(cursor);
        }
    }

    private void updateSalesData(Cursor cursor) {
        if (cursor.moveToFirst()) {
            //query
            int colPrice = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE);
            int colQuantity = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);
            int colIncome = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_INCOME);
            int colSale = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NUMBER_SALE);
            //set new data
            if (cursor.getInt(colQuantity) > 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, cursor.getInt(colQuantity) - 1);
                contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NUMBER_SALE, cursor.getInt(colSale) + 1);
                contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_INCOME,
                        cursor.getInt(colIncome) + cursor.getInt(colPrice));
                //update
                int rowUpdated = getContentResolver().update(uriPass, contentValues, null, null);
                if (rowUpdated == 0) {
                    // If the row ID is -1, then there was an error with insertion.
                    Toast.makeText(this, "Error with selling this product", Toast.LENGTH_SHORT).show();
                } else {
                    // Otherwise, the insertion was successful and we can display a toast with the row ID.
                    Toast.makeText(this, "Successful updating : " + rowUpdated + " product(s)", Toast.LENGTH_SHORT).show();
                }
                getLoaderManager().destroyLoader(LOADER_ID_ITEM);
            } else {
                Toast.makeText(this, "Unable to sell. Please check the products in store", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
