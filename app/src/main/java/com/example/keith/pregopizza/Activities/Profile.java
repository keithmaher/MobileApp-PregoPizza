package com.example.keith.pregopizza.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.keith.pregopizza.Activities.Models.Customer;
import com.example.keith.pregopizza.Activities.Sessions.Storage;
import com.example.keith.pregopizza.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Profile extends Navigation {

    EditText profileName, profilePhoneNumber, profileEmail, profilePassword;
    Button changImageButton, profileSaveButton, profileDeleteButton;

    FirebaseDatabase database;
    DatabaseReference customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_profile, null, false);
        drawer.addView(contentView, 0);
        navigationView.setCheckedItem(R.id.nav_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        profileName = findViewById(R.id.profileName);
        profilePhoneNumber = findViewById(R.id.profilePhoneNumber);
        profileEmail = findViewById(R.id.profileEmail);
        profilePassword = findViewById(R.id.profilePassword);
        profileSaveButton = findViewById(R.id.profileSaveButton);

        profileName.setText(Storage.currentCustomer.getName());
        profilePhoneNumber.setText(Storage.currentCustomer.getPhoneNumber());
        profileEmail.setText(Storage.currentCustomer.getEmail());
        profilePassword.setText(Storage.currentCustomer.getPassword());

//
//        database = FirebaseDatabase.getInstance();
//        customer = database.getReference("customers").child(loggedinUser);

        profileSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = profileName.getText().toString();
                String phone = profilePhoneNumber.getText().toString();
                String email = profileEmail.getText().toString();
                String password = profilePassword.getText().toString();


                Toast.makeText(Profile.this, ""+name , Toast.LENGTH_SHORT).show();
                Customer updateCustomer = new Customer(name, phone, email, password);
                customers.child(phone).setValue(updateCustomer);
                Storage.currentCustomer = updateCustomer;
            }
        });



    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity();
            } else {
                super.onBackPressed();
            }
        }
    }
}
