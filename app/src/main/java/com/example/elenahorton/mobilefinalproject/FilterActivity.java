package com.example.elenahorton.mobilefinalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FilterActivity extends AppCompatActivity {

    public static final String KEY_FILTERS = "KEY_FILTERS";
    @BindView(R.id.cbEvents)
    CheckBox cbEvents;
    @BindView(R.id.cbRestaurants)
    CheckBox cbRestaurants;
    @BindView(R.id.cbCafes)
    CheckBox cbCafes;
    @BindView(R.id.cbOutdoors)
    CheckBox cbOutdoors;
    @BindView(R.id.cbNightlife)
    CheckBox cbNightlife;
    @BindView(R.id.cbSightseeing)
    CheckBox cbSightseeing;
    @BindView(R.id.btnSave)
    Button btnSave;

    private ArrayList<String> currentFilters;
    private ArrayList<String> newFilters;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        setTitle(getString(R.string.filter_posts_foruser));
        ButterKnife.bind(this);
        currentFilters = getIntent().getStringArrayListExtra("FILTERS");
        newFilters = new ArrayList<String>();
        fillCheckboxes();

    }

    @OnClick(R.id.btnSave)
    public void onClick(View view) {
        fillNewFilters();
    }

    private void fillNewFilters() {
        Intent result = new Intent();
        if (cbRestaurants.isChecked()){
            newFilters.add("Restaurants");
        }if (cbCafes.isChecked()){
            newFilters.add("Cafes");
        }if (cbNightlife.isChecked()){
            newFilters.add("Nightlife");
        }if (cbOutdoors.isChecked()){
            newFilters.add("Outdoors");
        }if (cbEvents.isChecked()){
            newFilters.add("Events");
        }if (cbSightseeing.isChecked()){
            newFilters.add("Sightseeing");
        }
        result.putExtra(KEY_FILTERS, newFilters);
        setResult(RESULT_OK, result);
        finish();
    }

    public void fillCheckboxes() {
        if (currentFilters.contains("Events")) {
            cbEvents.setChecked(true);
        }if (currentFilters.contains("Restaurants")) {
            cbRestaurants.setChecked(true);
        }if (currentFilters.contains("Cafes")) {
            cbCafes.setChecked(true);
        }if (currentFilters.contains("Outdoors")) {
            cbOutdoors.setChecked(true);
        }if (currentFilters.contains("Nightlife")) {
            cbNightlife.setChecked(true);
        }if (currentFilters.contains("Sightseeing")) {
            cbSightseeing.setChecked(true);
        }
    }
}
