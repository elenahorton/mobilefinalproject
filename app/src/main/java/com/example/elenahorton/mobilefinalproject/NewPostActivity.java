package com.example.elenahorton.mobilefinalproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.MenuPopupWindow;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.elenahorton.mobilefinalproject.location.PostLocationManager;
import com.example.elenahorton.mobilefinalproject.model.Post;
import com.example.elenahorton.mobilefinalproject.model.User;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

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
    private Button btnAdd;
    private EditText etDescription;
    private Spinner category_menu;
    private Spinner costRate;
    private Location postLocation;
    private ArrayList<String> userPosts;

    private FirebaseStorage storage;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_post);

        storage = FirebaseStorage.getInstance();
        postLocation = getIntent().getParcelableExtra("LOCATION");
        userPosts = getIntent().getStringArrayListExtra("USER_POSTS");



        this.setTitle("Create a New Post");
        setupCategoryMenu();
        setupCostRatingMenu();

        etDescription = (EditText) findViewById(R.id.etDescription);
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
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentGetPhoto, REQUEST_CODE_PHOTOGALLERY);
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_DATA)) {
            imageBitmap = (Bitmap)
                    savedInstanceState.getParcelable(KEY_DATA);
            ivPhoto.setImageBitmap(imageBitmap);
        }

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendPost();
            }
        });

        requestMyPermissions();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
            outState.putParcelable(KEY_DATA, imageBitmap);
        }
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_PHOTO) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get(KEY_DATA);

            ivPhoto.setImageBitmap(imageBitmap);
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_PHOTOGALLERY) {
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

    // sets up spinner menu for options that you are able to select
    private void setupCategoryMenu() {
        category_menu = (Spinner) findViewById(R.id.category_menu);
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.image_category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        category_menu.setAdapter(adapter);
    }

    private void setupCostRatingMenu() {
        costRate = (Spinner) findViewById(R.id.getCostRating);
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.cost_menu, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        costRate.setAdapter(adapter);
    }

    private void sendPost() {
        if (!isFormValid()) {
            return;
        }

        final String key = FirebaseDatabase.getInstance().getReference().child("posts").push().getKey();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://mobile-final-project-30d00.appspot.com");
        String imageName = key + ".jpg";
        StorageReference imageRef = storageRef.child(imageName);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imgData = baos.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(imgData);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                //Add Error message or something
                Log.d("UPLOAD", "Failed to upload photo");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                //I'm being lazy and the spinner is not implemented
                Post newPost = new Post(getUid(), getUserName(), etDescription.getText().toString(),
                        category_menu.getItemAtPosition(category_menu.getSelectedItemPosition()).toString(),
                        downloadUrl.toString(),
                        costRate.getSelectedItemPosition() + 1, postLocation.getLatitude(), postLocation.getLongitude());

                FirebaseDatabase.getInstance().getReference().child("posts").child(key).setValue(newPost);

                userPosts.add(key);
                FirebaseDatabase.getInstance().getReference().child("users").child(getUid()).child("userPosts").setValue(userPosts);

                GeoFire geoFire = new GeoFire(FirebaseDatabase.getInstance().getReference().child("post_locations"));
                geoFire.setLocation(key, new GeoLocation(postLocation.getLatitude(), postLocation.getLongitude()));

            }
        });

        Toast.makeText(this, "Post created", Toast.LENGTH_SHORT).show();

        finish();
        Log.d("UPLOAD", "Successfully uploaded photo");
    }

    public GeoFire getGeoFireRef() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReferenceFromUrl("gs://mobile-final-project-30d00.appspot.com");
        GeoFire geoFire = new GeoFire(ref);
        return geoFire;
    }

    private boolean isFormValid() {
        boolean result = true;
        if (TextUtils.isEmpty(etDescription.getText().toString())) {
            etDescription.setError("Required");
            result = false;
        } else {
            etDescription.setError(null);
        }

        if (ivPhoto.getDrawable() == null) {
            //display and error message
            result = false;
        }

        return result;
    }

    // could probably get these from BaseActivity
    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public String getUserName() {
        return FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "NewPost Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.elenahorton.mobilefinalproject/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "NewPost Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.elenahorton.mobilefinalproject/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
