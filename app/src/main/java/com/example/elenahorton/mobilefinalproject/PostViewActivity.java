package com.example.elenahorton.mobilefinalproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.elenahorton.mobilefinalproject.model.Post;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PostViewActivity extends AppCompatActivity implements OnMapReadyCallback {

    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.tvAuthor)
    TextView tvAuthor;
    @BindView(R.id.tvCategoryOne)
    TextView tvCategory;
    @BindView(R.id.tvDescriptionOne)
    TextView tvDescription;

//    private ImageView image;
//    private TextView tvAuthor;
//    private TextView tvDescription;
//    private TextView tvCategory;
    private Double latitude;
    private Double longitude;
    private GoogleMap mMap;
    private LatLng postLoc = null;
    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view);
        ButterKnife.bind(this);
        setTitle("");

        post = (Post) getIntent().getSerializableExtra("POST");
        Glide.with(getApplicationContext()).load(post.getImage()).into(image);
        tvAuthor.setText(post.getAuthor());
        tvDescription.setText(post.getDescription());
        tvCategory.setText(post.getCategory());
        latitude = post.getLat();
        longitude = post.getLng();
        setLocation(latitude, longitude);

    }

    private void setLocation(Double lat, Double lng) {
        postLoc = new LatLng(lat, lng);
    }

    @Override
    public void onResume() {
        super.onResume();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions().position(postLoc));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(postLoc));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(postLoc).zoom(11).bearing(0).tilt(0).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
