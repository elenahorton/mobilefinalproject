package com.example.elenahorton.mobilefinalproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;

/**
 * Created by elladzenitis on 12/9/16.
 */
public class NewPostActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_PHOTO = 101;
    public static final int REQUEST_CODE_PHOTOGALLERY = 102;
    public static final String KEY_DATA = "data";
    public static final int REQUEST_CODE_PERMS = 101;
    private ImageView ivPhoto;
    private Bitmap imageBitmap = null;
    private Button btnTakePhoto;
    private Button btnGetPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_post);

        this.setTitle("Create a New Post");

        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
        btnTakePhoto = (Button) findViewById(R.id.btnTakePhoto);
        btnTakePhoto.setEnabled(false);
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTakePhoto = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intentTakePhoto, REQUEST_CODE_PHOTO);
            }
        });

        btnGetPhoto = (Button) findViewById(R.id.btnGetPhoto);
        btnGetPhoto.setEnabled(true);
        btnGetPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGetPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentGetPhoto, REQUEST_CODE_PHOTOGALLERY);
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_DATA)) {
            imageBitmap = (Bitmap)
                    savedInstanceState.getParcelable(KEY_DATA);
            ivPhoto.setImageBitmap(imageBitmap);
        }

        requestMyPermissions();
    }

    private void requestMyPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                Toast.makeText(this, "I need access to the camera", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CODE_PERMS);
        } else {
            btnTakePhoto.setEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,
                            "CAMERA perm granted",
                            Toast.LENGTH_SHORT).show();

                    btnTakePhoto.setEnabled(true);
                } else {
                    Toast.makeText(this, "CAMERA perm NOT granted", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        if (imageBitmap != null) {
            outState.putParcelable(KEY_DATA,imageBitmap);
        }
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_PHOTO) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get(KEY_DATA);

            ivPhoto.setImageBitmap(imageBitmap);
        }
        else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_PHOTOGALLERY){
            Uri targetUri = data.getData();
            try {
                imageBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                ivPhoto.setImageBitmap(imageBitmap);
                System.out.println("GOT THE IMAGE");
            } catch (FileNotFoundException e) {
                // Auto-generated catch block
                e.printStackTrace();
            }

        }


    }
}
