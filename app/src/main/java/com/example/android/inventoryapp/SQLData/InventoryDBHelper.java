package com.example.android.inventoryapp.SQLData;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by brayan pc on 11/11/2017.
 */

public class InventoryDBHelper extends SQLiteOpenHelper {

    public final static String DATABASE_NAME = "Inventory.db";
    public final static int DATABASE_VERSION = 1;

    public InventoryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CreateTable = "CREATE TABLE " + InventoryContract.InventoryEntry.TABLE_NAME
                + "(" + InventoryContract.InventoryEntry.COL_PRODUCT_ID + " INTEGER PRIMARY KEY,"
                + InventoryContract.InventoryEntry.COL_PRODUCT_NAME + " TEXT,"
                + InventoryContract.InventoryEntry.COL_PRODUCT_PRICE + " INTEGER,"
                + InventoryContract.InventoryEntry.COL_PRODUCT_QUANTITY + " INTEGER,"
                + InventoryContract.InventoryEntry.COL_PRODUCT_IMAGE + " BLOB );";
        db.execSQL(CreateTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
