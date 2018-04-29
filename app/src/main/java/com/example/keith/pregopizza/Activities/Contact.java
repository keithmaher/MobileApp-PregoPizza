package com.example.keith.pregopizza.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.keith.pregopizza.Manifest;
import com.example.keith.pregopizza.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Contact extends Navigation implements OnMapReadyCallback {

    ImageView contact_phone_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_contact, null, false);
        drawer.addView(contentView, 0);
        navigationView.setCheckedItem(R.id.nav_location);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.contact_map_fragment);
        mapFragment.getMapAsync(this);

        contact_phone_image = findViewById(R.id.contact_phone_image);

        contact_phone_image.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:052123456"));
                if (ActivityCompat.checkSelfPermission(Contact.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(Contact.this, new String[]{android.Manifest.permission.CALL_PHONE}, PackageManager.PERMISSION_GRANTED);

                } else {
                    startActivity(callIntent);
                }
            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng prego = new LatLng(52.356853, -7.697648);
        googleMap.addMarker(new MarkerOptions().position(prego).title("Prego Pizza Clonmel")).showInfoWindow();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(prego, 15));
    }


    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}