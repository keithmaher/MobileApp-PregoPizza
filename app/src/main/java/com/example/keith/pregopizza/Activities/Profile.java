package com.example.keith.pregopizza.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.keith.pregopizza.Activities.Models.Customer;
import com.example.keith.pregopizza.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Profile extends Navigation {

    SharedPreferences settings;

    EditText profileName, profileEmail, profilePassword;
    TextView profilePhoneNumber;
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

        settings = getSharedPreferences("loginSettings", 0);

        database = FirebaseDatabase.getInstance();
        customer = database.getReference("customers");

        profileName = findViewById(R.id.profileName);
        profilePhoneNumber = findViewById(R.id.profilePhoneNumber);
        profileEmail = findViewById(R.id.profileEmail);
        profilePassword = findViewById(R.id.profilePassword);
        profileSaveButton = findViewById(R.id.profileSaveButton);
        profileDeleteButton = findViewById(R.id.deleteProfile);

        profileName.setText(settings.getString("username", null));
        profileEmail.setText(settings.getString("useremail", null));
        profilePhoneNumber.setText(settings.getString("userphone", null));
        profilePassword.setText(settings.getString("password", null));


        profileSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String name = profileName.getText().toString();
                String phone = profilePhoneNumber.getText().toString();
                String email = profileEmail.getText().toString();
                String password = profilePassword.getText().toString();


                if (name.equals(settings.getString("username", null)) && email.equals(settings.getString("useremail", null)) && password.equals(settings.getString("password", null))){
                    Toast.makeText(Profile.this, "No changes made", Toast.LENGTH_SHORT).show();
                } else {

                    Customer updateCustomer = new Customer(name, phone, email, password);
                    customer.child(phone).setValue(updateCustomer);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("loggedin", true);
                    editor.putString("username", name);
                    editor.putString("useremail", email);
                    editor.putString("password", password);
                    editor.commit();

                    Intent change = new Intent(Profile.this, CategoryMenu.class);
                    startActivity(change);
                    Toast.makeText(Profile.this, "Details Saved", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        profileDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProfieDialog();
            }
        });

    }

    public void deleteProfieDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
        builder.setTitle("Profile Removal");
        builder.setMessage("You are about to remove your profile from the system, this can not be undone" );

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String phone = profilePhoneNumber.getText().toString();
                customer.child(phone).removeValue();
                logout();
                finish();
                Toast.makeText(Profile.this, "Succesfully Deleted ", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialogBox = builder.create();
        dialogBox.show();
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