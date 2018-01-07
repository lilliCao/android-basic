package com.example.android.inventory.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tali on 06.01.18.
 */

public class ProductProvider extends ContentProvider {
    public static final String LOG_TAG = ProductProvider.class.getSimpleName();
    private ProductDbHelper productDbHelper;
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Set up match URIs
     */
    private static final int PRODUCTS = 100;
    private static final int PRODUCT_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PATH_PRODUCTS, PRODUCTS);
        sUriMatcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PATH_PRODUCTS + "/#", PRODUCT_ID);
    }

    private String currentDate() {
        return formatter.format(new Date());
    }

    @Override
    public boolean onCreate() {
        productDbHelper = new ProductDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor = null;
        SQLiteDatabase db = productDbHelper.getReadableDatabase();
        int type = sUriMatcher.match(uri);
        switch (type) {
            case PRODUCTS:
                break;
            case PRODUCT_ID:
                selection = ProductContract.ProductEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                break;
            default:
                throw new IllegalArgumentException("Can not parser uri");
        }
        cursor = db.query(ProductContract.ProductEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int type = sUriMatcher.match(uri);
        switch (type) {
            case PRODUCTS:
                return ProductContract.ProductEntry.CONTENT_LIST_TYPE;
            case PRODUCT_ID:
                return ProductContract.ProductEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown uri " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        switch (sUriMatcher.match(uri)) {
            case PRODUCTS:
                return insertProduct(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported with this uri " + uri);
        }
    }

    private Uri insertProduct(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = productDbHelper.getWritableDatabase();
        //Check contentValues
        sanityCheck(contentValues);
        contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_DATE, currentDate());
        long id = db.insert(ProductContract.ProductEntry.TABLE_NAME, null, contentValues);
        if (id == -1) {
            Log.e(LOG_TAG, "Insert product failed");
            return null;
        } else {
            getContext().getContentResolver().notifyChange(uri, null);
            return ContentUris.withAppendedId(uri, id);
        }
    }

    private void sanityCheck(ContentValues contentValues) {
        checkNull(contentValues, ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
        checkNull(contentValues, ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER);
        checkNull(contentValues, ProductContract.ProductEntry.COLUMN_PRODUCT_ORDER_DETAIL);
        if (contentValues.containsKey(ProductContract.ProductEntry.COLUMN_PRODUCT_ORDER_METHOD)) {
            Integer method = contentValues.getAsInteger(ProductContract.ProductEntry.COLUMN_PRODUCT_ORDER_METHOD);
            if (method == null) {
                throw new IllegalArgumentException("Order method is null");
            }
        }
    }

    private void checkNull(ContentValues contentValues, String column) {
        if (contentValues.containsKey(column)) {
            String value = contentValues.getAsString(column);
            if (value == null) {
                throw new IllegalArgumentException("Value of " + column + "is null");
            }
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = productDbHelper.getWritableDatabase();
        int type = sUriMatcher.match(uri);
        int deletedRows;
        switch (type) {
            case PRODUCTS:
                deletedRows = db.delete(ProductContract.ProductEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PRODUCT_ID:
                selection = ProductContract.ProductEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                deletedRows = db.delete(ProductContract.ProductEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deleting is not supported for this uri " + uri);
        }
        if (deletedRows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deletedRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        int type = sUriMatcher.match(uri);
        switch (type) {
            case PRODUCTS:
                break;
            case PRODUCT_ID:
                selection = ProductContract.ProductEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                break;
            default:
                throw new IllegalArgumentException("Update is not supported for this uri " + uri);
        }
        return updateProduct(uri, contentValues, selection, selectionArgs);
    }

    private int updateProduct(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        sanityCheck(contentValues);
        if (contentValues.size() == 0) {
            return 0;
        }
        SQLiteDatabase db = productDbHelper.getWritableDatabase();
        contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_DATE, currentDate());
        int updatedRow = db.update(ProductContract.ProductEntry.TABLE_NAME, contentValues, selection, selectionArgs);
        if (updatedRow != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return updatedRow;
    }
}
