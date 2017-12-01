package com.example.android.inventoryapp.SQLData;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by brayan pc on 11/12/2017.
 */

public class InventoryContentProvider extends ContentProvider {
    final static UriMatcher InventoryUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    InventoryDBHelper inventoryDBHelper;

    static {
        InventoryUriMatcher.addURI("com.example.android.inventoryapp", "Inventory", 1);
        InventoryUriMatcher.addURI("com.example.android.inventoryapp", "Inventory/#", 2);
    }

    @Override
    public boolean onCreate() {
        inventoryDBHelper = new InventoryDBHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sort) {
        int code = InventoryUriMatcher.match(uri);
        SQLiteDatabase db = inventoryDBHelper.getReadableDatabase();
        Cursor cursor;
        switch (code) {
            case 1:
                cursor = db.query(InventoryContract.InventoryEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sort);
                break;

            case 2:
                selection = InventoryContract.InventoryEntry.COL_PRODUCT_ID + "=?";
                selectionArgs = new String[]{ContentUris.parseId(uri) + ""};
                cursor = db.query(InventoryContract.InventoryEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sort);
                break;

            default:
                throw new IllegalArgumentException("Cannot resolve the URI" + uri);

        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int code = InventoryUriMatcher.match(uri);
        if (values != null) {
            String name = values.getAsString(InventoryContract.InventoryEntry.COL_PRODUCT_NAME);
            if (name == null) {
                Toast.makeText(getContext(), "Please enter valid values", Toast.LENGTH_LONG).show();
            }
            Long price = values.getAsLong(InventoryContract.InventoryEntry.COL_PRODUCT_PRICE);
            if (price == null) {
                Toast.makeText(getContext(), "Please enter valid values", Toast.LENGTH_LONG).show();
            }
            Long quantity = values.getAsLong(InventoryContract.InventoryEntry.COL_PRODUCT_QUANTITY);
            if (quantity == null) {
                Toast.makeText(getContext(), "Please enter valid values", Toast.LENGTH_LONG).show();
            }
            byte[] image = values.getAsByteArray(InventoryContract.InventoryEntry.COL_PRODUCT_IMAGE);
            if (image == null) {
                Toast.makeText(getContext(), "Please enter valid values", Toast.LENGTH_LONG).show();
            }
        }
        if (code == 1) {
            SQLiteDatabase db = inventoryDBHelper.getWritableDatabase();
            db.insert(InventoryContract.InventoryEntry.TABLE_NAME, null, values);
            getContext().getContentResolver().notifyChange(uri, null);

        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[]
            selectionArgs) {
        int code = InventoryUriMatcher.match(uri);
        SQLiteDatabase db = inventoryDBHelper.getWritableDatabase();
        int RowsEffected = 0;
        switch (code) {


            case 1:
                db.delete(InventoryContract.InventoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case 2:
                selection = InventoryContract.InventoryEntry.COL_PRODUCT_ID + "=?";
                selectionArgs = new String[]{ContentUris.parseId(uri) + ""};
                RowsEffected = db.delete(InventoryContract.InventoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Cannot resolve the URI" + uri);

        }
        if (RowsEffected > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return RowsEffected;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String
            selection, @Nullable String[] selectionArgs) {
        int code = InventoryUriMatcher.match(uri);
        SQLiteDatabase db = inventoryDBHelper.getWritableDatabase();
        int RowsEffected = 0;
        switch (code) {

            case 1:
                RowsEffected = db.update(InventoryContract.InventoryEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case 2:
                selection = InventoryContract.InventoryEntry.COL_PRODUCT_ID + "=?";
                selectionArgs = new String[]{ContentUris.parseId(uri) + ""};
                RowsEffected = db.update(InventoryContract.InventoryEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Cannot resolve the URI" + uri);

        }
        if (RowsEffected != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return RowsEffected;
    }
}
