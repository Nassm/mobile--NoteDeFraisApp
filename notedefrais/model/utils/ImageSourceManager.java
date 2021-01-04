package com.example.notedefrais.model.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.notedefrais.App;
import com.example.notedefrais.R;
import com.example.notedefrais.view.NdfActivity;
import com.example.notedefrais.view.selection.DepenseCategorySelectionActivity;

public class ImageSourceManager
{
    private Activity activity;
    private Context context;
    private ContentValues values;
    private Uri imageUri;

    public ImageSourceManager(Activity activity, Context context)
    {
        this.activity = activity;
        this.context = context;
        this.values = new ContentValues();
        this.values.put(MediaStore.Images.Media.TITLE, "new Pic"); // title of picture
        this.values.put(MediaStore.Images.Media.DESCRIPTION, "Image to text"); // title of picture
    }

    public Uri getImageUri() { return imageUri; }

    /* private method for manage capture */
    public void getImageFromSource(int idNdf)
    {
        if( idNdf != 0)
        {
            App.setNdfId(idNdf); /* not clean | need refactoring | require set idNdf through the intent */
        }

        String [] options = {"Scan intelligent", "Galerie d'image", "Saisie manuelle" };

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setIcon(R.drawable.ic_mode_select);
        dialog.setTitle(App.get().getApplicationContext().getString(R.string.SELECTIONNER_MODE));
        dialog.setItems(options, (dialog1, which) ->
        {
            if(which == 0)
            {
                if(!isCameraPermissionGranted())
                {
                    requestingCameraPermission();
                } else
                {
                    onCameraPicking();
                }

            } else if (which == 1)
            {
                if(!isStoragePermissionGranted())
                {
                    requestingStoragePermission();
                } else
                {
                    onGalleryImageSelecting();
                }
            } else if(which == 2)
            {
                activity.startActivity(new Intent(context, DepenseCategorySelectionActivity.class));
            }
        });

        dialog.setOnCancelListener(dialog2 -> activity.startActivity(new Intent(context, NdfActivity.class)));

        dialog.show();
    }

    private boolean isCameraPermissionGranted()
    {
        boolean isCamPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean isStorePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return isCamPermission && isStorePermission;
    }

    private boolean isStoragePermissionGranted()
    {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
    }

    private void requestingCameraPermission()
    {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, NdfActivity.CAMERA_REQUEST_PERMISSION_CODE);
    }

    private void requestingStoragePermission()
    {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, NdfActivity.STORAGE_REQUEST_PERMISSION_CODE);
    }

    public void onCameraPicking()
    {
        this.imageUri = this.activity.getContentResolver().insert((MediaStore.Images.Media.EXTERNAL_CONTENT_URI), values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, 1);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        activity.startActivityForResult(cameraIntent, NdfActivity.IMAGE_PICK_CAMERA_CODE);
    }

    public void onGalleryImageSelecting()
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        activity.startActivityForResult(intent, NdfActivity.IMAGE_PICK_GALLERY_CODE);
    }
}
