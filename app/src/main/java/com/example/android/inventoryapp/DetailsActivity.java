package com.example.android.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.android.inventoryapp.SQLData.InventoryContract;
import com.example.android.inventoryapp.databinding.ActivityDetailsBinding;

import java.io.ByteArrayOutputStream;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    ActivityDetailsBinding binding;
    int quantity = 0;
    boolean EditMode = false;
    Uri Inventory_Uri;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    byte[] imageInBit;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                binding.imageview.setImageBitmap(imageBitmap);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                imageInBit = byteArrayOutputStream.toByteArray();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details);
        long id = getIntent().getLongExtra("ID", -1);
        if (id != -1) {
            EditMode = true;
            Inventory_Uri = ContentUris.withAppendedId(InventoryContract.InventoryEntry.PRODUCT_URI, id);
            getSupportLoaderManager().initLoader(1, null, this);
            binding.delete.setVisibility(View.VISIBLE);
        }

        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.name.getText().toString();
                String price = binding.price.getText().toString();
                String quantity = binding.quantity.getText().toString();
                ContentValues values = new ContentValues();
                values.put(InventoryContract.InventoryEntry.COL_PRODUCT_NAME, name);
                values.put(InventoryContract.InventoryEntry.COL_PRODUCT_PRICE, price);
                values.put(InventoryContract.InventoryEntry.COL_PRODUCT_QUANTITY, quantity);
                if (imageInBit != null) {
                    values.put(InventoryContract.InventoryEntry.COL_PRODUCT_IMAGE, imageInBit);
                }

                if (EditMode) {
                    if (!name.isEmpty() && !price.isEmpty() && !quantity.isEmpty() && imageInBit != null) {
                        getContentResolver().update(Inventory_Uri, values, null, null);
                        finish();
                    } else {
                        Toast.makeText(DetailsActivity.this, "Please enter valid values", Toast.LENGTH_LONG).show();
                    }

                } else {
                    if (!name.isEmpty() && !price.isEmpty() && !quantity.isEmpty() && imageInBit != null) {
                        getContentResolver().insert(InventoryContract.InventoryEntry.PRODUCT_URI, values);
                        finish();
                    } else {
                        Toast.makeText(DetailsActivity.this, "Please enter valid values", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
        binding.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity++;
                binding.quantity.setText(quantity + "");
            }
        });
        binding.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 0) {
                    quantity--;
                }
                binding.quantity.setText(quantity + "");
            }
        });
        binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailsActivity.this);
                builder.setMessage("Are you sure you want to delete this product?");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getContentResolver().delete(Inventory_Uri, null, null);
                        finish();
                    }
                });
                builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
        binding.order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                startActivity(emailIntent);


            }
        });
        binding.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, Inventory_Uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryEntry.COL_PRODUCT_NAME));
            long price = cursor.getLong(cursor.getColumnIndex(InventoryContract.InventoryEntry.COL_PRODUCT_PRICE));
            long quantity = cursor.getLong(cursor.getColumnIndex(InventoryContract.InventoryEntry.COL_PRODUCT_QUANTITY));
            byte[] image = cursor.getBlob(cursor.getColumnIndex(InventoryContract.InventoryEntry.COL_PRODUCT_IMAGE));
            binding.price.setText(price + " $");
            binding.name.setText(name);
            binding.quantity.setText(quantity + "");
            if (image != null) {
                binding.imageview.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
