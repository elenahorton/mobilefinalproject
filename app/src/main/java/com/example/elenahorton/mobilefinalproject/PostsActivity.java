package com.example.elenahorton.mobilefinalproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elenahorton.mobilefinalproject.adapter.PostAdapter;
import com.example.elenahorton.mobilefinalproject.adapter.ViewPagerAdapter;
import com.example.elenahorton.mobilefinalproject.location.PostLocationManager;
import com.example.elenahorton.mobilefinalproject.model.Post;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;


public class PostsActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.OnConnectionFailedListener, PostLocationManager.OnLocChanged, GeoQueryEventListener {

    public static final int REQUEST_CODE = 101;
    private PostLocationManager postLocationManager;
    private GoogleApiClient googleApiClient;
    private PostAdapter postsLocAdapter;
    private PostAdapter postsUserAdapter;
    private Location userLocation;
    private Circle searchCircle;
    private GeoFire geoFire;
    private GeoQuery geoQuery;
    private Map<String, Marker> markers;
    private ArrayList<String> filters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViewPager();
        toolbar.setLogo(R.drawable.app_title);

        filters = new ArrayList<String>();
        filters.add("Restaurants");
        filters.add("Cafes");
        filters.add("Outdoors");
        filters.add("Nightlife");
        filters.add("Events");
        filters.add("Sightseeing");
        filters.add("Shopping");

        initPostsListener();
        requestNeededPermission();

        postLocationManager = new PostLocationManager(getApplicationContext(), this);
        googleApiClient = new GoogleApiClient.Builder(this).addApi(Places.GEO_DATA_API).addApi(Places.PLACE_DETECTION_API).enableAutoManage(this, this).build();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        userLocation = postLocationManager.getLocMan().getLastKnownLocation(postLocationManager.getLocMan().GPS_PROVIDER);
        postsLocAdapter = new PostAdapter(getApplicationContext(), getUid(), 0, filters, userLocation);
        postsUserAdapter = new PostAdapter(getApplicationContext(), getUid(), 1, filters, userLocation);

        postsLocAdapter.getPostsByLocation();



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewPostActivity();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    private void startNewPostActivity() {
        Intent intentNewPost = new Intent();
        intentNewPost.putExtra("LOCATION", userLocation);
        intentNewPost.putExtra("USER_POSTS", postsUserAdapter.getUserPosts());
        intentNewPost.setClass(PostsActivity.this, NewPostActivity.class);
        startActivity(intentNewPost);
    }

    private void startFilterDialog() {
        Intent intentFilter = new Intent();
        intentFilter.setClass(PostsActivity.this, FilterActivity.class);
        intentFilter.putExtra("FILTERS", filters);
        startActivityForResult(intentFilter, REQUEST_CODE);
    }

    private void setupViewPager() {
        final ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
    }

    private void initPostsListener() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("posts");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Post newPost = dataSnapshot.getValue(Post.class);
//                System.out.println("THIS IS THE KEY: " + dataSnapshot.getKey());
//                System.out.println("Getting valid locations " + postsLocAdapter.getValidLocations());
//                postsLocAdapter.addPost(newPost, dataSnapshot.getKey());
                if (newPost.getAuthor() == FirebaseAuth.getInstance().getCurrentUser().getDisplayName()) {
                    postsUserAdapter.addPost(newPost, dataSnapshot.getKey());
                    Log.d("TAG_ADDED", "added to user adapter");
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                postsUserAdapter.removePostByKey(dataSnapshot.getKey());
                postsLocAdapter.removePostByKey(dataSnapshot.getKey());
                Log.d("TAG_REMOVED", "removing post");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }




    public void requestNeededPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(PostsActivity.this,
                        "To Find Posts Near You", Toast.LENGTH_SHORT).show();
            }

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    101);
        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 101: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(PostsActivity.this, "ACCESS_FINE_LOCATION perm granted", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(PostsActivity.this,
                            "ACCESS_FINE_LOCATION perm NOT granted", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        postLocationManager.startLocationMonitoring();
    }

    @Override
    protected void onStop() {
        postLocationManager.stopLocationMonitoring();
        super.onStop();
    }


        @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.posts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filter) {
            startFilterDialog();

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
           showConfirmationAlert();
        }

        if (id == R.id.nav_newPost) {
            startNewPostActivity();
        }  if (id == R.id.nav_about) {
            showHelpInfo();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void showHelpInfo() {
        new AlertDialog.Builder(this).setIcon(R.drawable.info).setTitle(R.string.about_app).setMessage(R.string.about_info).setPositiveButton(R.string.ok, null).show();
    }

    private void showConfirmationAlert() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.logout_confirm)
                .setMessage(R.string.are_you_sure)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    }
                })
                .setNegativeButton(R.string.no, null).show();
    }

    public PostAdapter getPostsLocAdapter() {
        return postsLocAdapter;
    }
    public PostAdapter getPostsUserAdapter() {
        return postsUserAdapter;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void locationChanged(Location location) {
        userLocation = location;
    }

    public Location getUserLocation() {
        return userLocation;
    }

    @Override
    public void onKeyEntered(String key, GeoLocation location) {

    }

    @Override
    public void onKeyExited(String key) {

    }

    @Override
    public void onKeyMoved(String key, GeoLocation location) {

    }

    @Override
    public void onGeoQueryReady() {

    }

    @Override
    public void onGeoQueryError(DatabaseError error) {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            filters = data.getStringArrayListExtra(FilterActivity.KEY_FILTERS);
            reloadPosts();
        }
    }

    private void reloadPosts() {
        postsLocAdapter.deleteAllItems();
        postsLocAdapter.setFilters(filters);
    }
}
