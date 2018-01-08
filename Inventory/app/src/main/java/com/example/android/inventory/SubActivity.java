package com.example.android.inventory;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
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

import com.example.android.inventory.Utils.BitmapUtils;
import com.example.android.inventory.data.ProductContract;

import java.io.FileNotFoundException;
import java.io.InputStream;


public class SubActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private final static String LOG_TAG = SubActivity.class.getSimpleName();
    private static final int REQUEST_PHONE_CALL = 1;
    private static final int PICK_PHOTO_GALLERY = 2;
    private static final int CAPTURE_PHOTO = 3;
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
    private String orderDetailV;
    private Bitmap bitmap;

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
                if (TextUtils.isEmpty(quantityV) || (Integer.valueOf(quantityV) == 0)) {
                    Toast.makeText(SubActivity.this, getString(R.string.can_not_decrease_quantity), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    quantityValue = Integer.valueOf(quantityV);
                    quantityValue--;
                    quantity.setText(String.valueOf(quantityValue));
                }
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quantityV = quantity.getText().toString();
                if (TextUtils.isEmpty(quantityV)) {
                    Toast.makeText(SubActivity.this, getString(R.string.can_not_increase_quantity), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    quantityValue = Integer.valueOf(quantityV);
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
        imageButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                MenuBuilder builder = new MenuBuilder(SubActivity.this);
                new MenuInflater(SubActivity.this).inflate(R.menu.image_menu, builder);
                MenuPopupHelper options = new MenuPopupHelper(SubActivity.this, builder, imageButton);
                options.setForceShowIcon(true);
                builder.setCallback(new MenuBuilder.Callback() {
                    @Override
                    public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.gallery:
                                pickPhotoFromGallery();
                                break;
                            case R.id.camera:
                                takePhotoFromCamera();
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
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderDetailV = orderDetail.getText().toString();
                if (TextUtils.isEmpty(orderDetailV)) {
                    Toast.makeText(SubActivity.this, getString(R.string.order_empty), Toast.LENGTH_SHORT).show();
                    return;
                }
                String spinnerV = orderMethod.getSelectedItem().toString();
                if (spinnerV.equals(getString(R.string.phone))) {
                    orderCall();
                } else if (spinnerV.equals(getString(R.string.email))) {
                    orderEmail();
                } else {
                    orderWeb();
                }

            }
        });
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAPTURE_PHOTO);
    }

    private void pickPhotoFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_GALLERY && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Toast.makeText(SubActivity.this, getString(R.string.error_pick_photo), Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                InputStream ins = getContentResolver().openInputStream(data.getData());
                bitmap = BitmapFactory.decodeStream(ins);
                image.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAPTURE_PHOTO && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            image.setImageBitmap(bitmap);
        }
    }

    private void orderWeb() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (!Patterns.WEB_URL.matcher(orderDetailV).matches()) {
            Toast.makeText(SubActivity.this, getString(R.string.wrong_url), Toast.LENGTH_SHORT).show();
            return;
        }
        intent.setData(Uri.parse(orderDetailV));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void orderEmail() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{orderDetailV});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Order from Inventory App ");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void orderCall() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        if (Uri.parse("tel:" + orderDetailV) == null) {
            Toast.makeText(SubActivity.this, getString(R.string.wrong_phone), Toast.LENGTH_SHORT).show();
            return;
        }
        intent.setData(Uri.parse("tel:" + orderDetailV));
        if (intent.resolveActivity(getPackageManager()) != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(SubActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                return;
            }
            startActivity(intent);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PHONE_CALL: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    startActivity(new Intent().setData(Uri.parse("tel:" + orderDetailV)));
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
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
                showDeleteDialog();
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
            default:
                break;
        }
        return true;
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.delete_dialog_msg_item));
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteProduct();
                dialog.cancel();
                finish();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteProduct() {
        int rowDeleted = getContentResolver().delete(uriPass, null, null);
        if (rowDeleted == 0) {
            Toast.makeText(this, "Error deleting this product", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Successfully deleting this product", Toast.LENGTH_SHORT).show();
        }
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
        try {
            Double.parseDouble(quantityV);
        } catch (Exception e) {
            Toast.makeText(SubActivity.this, getString(R.string.fill_price), Toast.LENGTH_SHORT).show();
            return false;
        }


        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, nameV);
        if(!TextUtils.isEmpty(priceV)){
            values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE, Double.parseDouble(priceV));
        }
        if (!TextUtils.isEmpty(quantityV)) {
            values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, Integer.valueOf(quantityV));
        }
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER, supplierV);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_ORDER_DETAIL, orderDetailV);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_ORDER_METHOD, spinnerValue);
        if (bitmap != null) {
            values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_IMAGE, BitmapUtils.fromBitmapToByteArray(bitmap));
        }

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
            int colImage = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_IMAGE);

            name.setText(cursor.getString(colName));
            price.setText(String.valueOf(cursor.getFloat(colPrice)));
            quantity.setText(String.valueOf(cursor.getInt(colQuantity)));
            supplier.setText(cursor.getString(colSupplier));
            orderDetail.setText(cursor.getString(colOrderdetails));
            switch (cursor.getInt(colOrderMethod)) {
                case ProductContract.ProductEntry.ORDER_PHONE:
                    orderMethod.setSelection(0);
                    break;
                case ProductContract.ProductEntry.ORDER_EMAIL:
                    orderMethod.setSelection(1);
                    break;
                case ProductContract.ProductEntry.ORDER_WEB:
                    orderMethod.setSelection(2);
                    break;
                default:
                    break;

            }
            if (cursor.getBlob(colImage) != null) {
                bitmap = BitmapUtils.fromByteArrayToBitmap(cursor.getBlob(colImage));
                if (bitmap != null) {
                    image.setImageBitmap(bitmap);
                }
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
