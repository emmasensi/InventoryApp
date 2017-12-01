package com.example.android.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.inventoryapp.SQLData.InventoryContract;

import static com.example.android.inventoryapp.R.id.quantity;
import static com.example.android.inventoryapp.R.id.sale;

/**
 * Created by brayan pc on 11/12/2017.
 */

public class InventoryCursorAdapter extends CursorAdapter {
    public InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.listitem, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView name_text_view = (TextView) view.findViewById(R.id.productname);
        TextView price_text_view = (TextView) view.findViewById(R.id.price);
        TextView quantity_text_view = (TextView) view.findViewById(quantity);
        ImageView Image_Image_view = (ImageView) view.findViewById(R.id.imageview);
        Button Sale_view = (Button) view.findViewById(sale);
        String name = cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryEntry.COL_PRODUCT_NAME));
        long price = cursor.getLong(cursor.getColumnIndex(InventoryContract.InventoryEntry.COL_PRODUCT_PRICE));
        final long quantity = cursor.getLong(cursor.getColumnIndex(InventoryContract.InventoryEntry.COL_PRODUCT_QUANTITY));
        final long Id = cursor.getLong(cursor.getColumnIndex(InventoryContract.InventoryEntry.COL_PRODUCT_ID));
        byte[] image = cursor.getBlob(cursor.getColumnIndex(InventoryContract.InventoryEntry.COL_PRODUCT_IMAGE));
        price_text_view.setText("price: " + price + " $");
        name_text_view.setText("product name: " + name);
        quantity_text_view.setText("quantity: " + quantity + "");
        if (image != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            Image_Image_view.setImageBitmap(bitmap);
        }

        Sale_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 0) {
                    ContentValues values = new ContentValues();
                    values.put(InventoryContract.InventoryEntry.COL_PRODUCT_QUANTITY, quantity - 1);
                    context.getContentResolver().update(ContentUris.withAppendedId(InventoryContract.InventoryEntry.PRODUCT_URI, Id), values, null, null);
                }
            }
        });
    }

}
