package com.example.android.inventoryapp.SQLData;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by brayan pc on 11/11/2017.
 */
public class InventoryContract {
    public final static Uri BASE_URI = Uri.parse("content://com.example.android.inventoryapp");

    public final static class InventoryEntry implements BaseColumns {

        public final static String TABLE_NAME = "Inventory";
        public final static String COL_PRODUCT_ID = BaseColumns._ID;
        public final static String COL_PRODUCT_NAME = "Name";
        public final static String COL_PRODUCT_PRICE = "Price";
        public final static String COL_PRODUCT_QUANTITY = "Quantity";
        public final static String COL_PRODUCT_IMAGE = "Image";
        public final static Uri PRODUCT_URI = Uri.withAppendedPath(BASE_URI, TABLE_NAME);

    }
}
