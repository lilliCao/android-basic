package com.example.android.inventory.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by tali on 06.01.18.
 */

public final class ProductContract {
    //pretend create object of this class from outside. Since this class is only to hold the constant
    private ProductContract() {
    }

    public static final String CONTENT_AUTHORITY = "com.example.android.inventory";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_PRODUCTS = "products";

    /**
     * Inner class that defines products table
     */
    public static final class ProductEntry implements BaseColumns {
        /**
         * Uri for table
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCTS);
        /**
         * Content types
         */
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;
        /**
         * Table name
         */
        public static final String TABLE_NAME = "products";

        /**
         * Defines columns
         */
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_PRODUCT_NAME = "name";
        public static final String COLUMN_PRODUCT_PRICE = "price";
        public static final String COLUMN_PRODUCT_QUANTITY = "quantity";
        public static final String COLUMN_PRODUCT_SUPPLIER = "supplier";
        public static final String COLUMN_PRODUCT_ORDER_METHOD = "order_method";
        public static final String COLUMN_PRODUCT_ORDER_DETAIL = "order_detail";
        public static final String COLUMN_PRODUCT_IMAGE = "image";
        public static final String COLUMN_PRODUCT_DATE = "date";
        public static final String COLUMN_PRODUCT_NUMBER_SALE = "number_sale";
        public static final String COLUMN_PRODUCT_INCOME = "income";

        /**
         * Defines possible order methods
         */
        public static final int ORDER_EMAIL = 0;
        public static final int ORDER_PHONE = 1;
        public static final int ORDER_WEB = 2;

        /**
         * Check valid order methods
         */
        public static boolean isValidOrderMethod(Integer method) {
            return method == ORDER_EMAIL || method == ORDER_PHONE || method == ORDER_WEB;
        }

    }
}
