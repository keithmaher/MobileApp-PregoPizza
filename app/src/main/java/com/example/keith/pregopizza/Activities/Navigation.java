package com.example.keith.pregopizza.Activities;

import android.content.Intent;
import android.hardware.camera2.params.BlackLevelPattern;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.keith.pregopizza.Activities.Models.Category;
import com.example.keith.pregopizza.Activities.Models.Customer;
import com.example.keith.pregopizza.Activities.Sessions.Storage;
import com.example.keith.pregopizza.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Navigation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String newUserPhone;
    String newUserPassword;

    DrawerLayout drawer;
    NavigationView navigationView;
    TextView nav_name;
    TextView nav_email;

    AlertDialog.Builder alertDialog;
    AlertDialog dialog;
    Button loginbutton;
    Button loginCancelButton;
    EditText loginPhoneNumber;
    EditText loginPassword;

    Button registerButton;
    Button registerCancelButton;
    EditText registerName;
    EditText registerPhoneNumber;
    EditText registerEmail;
    EditText registerPassword;

    FirebaseDatabase database;
    DatabaseReference customers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database = FirebaseDatabase.getInstance();
        customers = database.getReference("customers");

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu nav_Menu = navigationView.getMenu();

        View userView = navigationView.getHeaderView(0);
        nav_name = userView.findViewById(R.id.nav_name);
        nav_email = userView.findViewById(R.id.nav_email);

        if (Storage.currentCustomer == null){
            Toast.makeText(this, "No one logged in", Toast.LENGTH_SHORT).show();
            nav_name.setText("You need to login");
            nav_email.setText("to view more options");
            nav_Menu.findItem(R.id.nav_logout).setVisible(false);
            nav_Menu.findItem(R.id.nav_cart).setVisible(false);
            nav_Menu.findItem(R.id.nav_profile).setVisible(false);
            nav_Menu.findItem(R.id.nav_orders).setVisible(false);
        }else {

        String username = Storage.currentCustomer.getName();
        String useremail = Storage.currentCustomer.getEmail();
        nav_name.setText(username);
        nav_email.setText(useremail);
        nav_Menu.findItem(R.id.nav_login).setVisible(false);
        nav_Menu.findItem(R.id.nav_register).setVisible(false);
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            startAnimatedActivity(new Intent(getApplicationContext(), CategoryMenu.class));
            finish();
        } else if (id == R.id.nav_cart) {
            startAnimatedActivity(new Intent(getApplicationContext(), Cart.class));
            finish();
        } else if (id == R.id.nav_location) {
            startAnimatedActivity(new Intent(getApplicationContext(), Contact.class));
            finish();
        } else if (id == R.id.nav_profile) {
            startAnimatedActivity(new Intent(getApplicationContext(), Profile.class));
            finish();
        } else if (id == R.id.nav_login) {
            loginDialog();
        } else if (id == R.id.nav_logout) {
            Storage.currentCustomer = null;
            Intent logout = new Intent(getApplicationContext(), CategoryMenu.class);
            logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(logout);
        } else if (id == R.id.nav_register) {
            registerDialog();
        }

        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void startAnimatedActivity(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void loginDialog() {

        alertDialog = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.login, null);
        loginbutton = view.findViewById(R.id.loginButton);
        loginCancelButton = view.findViewById(R.id.loginCancelButton);
        loginPhoneNumber = view.findViewById(R.id.loginPhoneNumber);
        loginPassword = view.findViewById(R.id.loginPassword);
        alertDialog.setView(view);
        dialog = alertDialog.create();
        dialog.show();

        if (newUserPhone != null) {

            loginPhoneNumber.setText(newUserPhone);
            loginPassword.setText(newUserPassword);
        }



        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newUserPhone = null;
                newUserPassword = null;

                final String phoneNumber = loginPhoneNumber.getText().toString();
                final String password = loginPassword.getText().toString();

                customers.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child(phoneNumber).exists()) {
                            Customer customer = dataSnapshot.child(phoneNumber).getValue(Customer.class);
                            if (customer.getPassword().equals(password)) {
                                Intent menu = new Intent(Navigation.this, CategoryMenu.class);
                                Storage.currentCustomer = customer;
                                startActivity(menu);
                                dialog.dismiss();
                                finish();
                            } else {
                                Toast.makeText(Navigation.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(Navigation.this, "User not Registered", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });


        loginCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }


    public void registerDialog() {

        Storage.currentCustomer = null;
        alertDialog = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.register, null);
        registerButton = view.findViewById(R.id.registerButton);
        registerCancelButton = view.findViewById(R.id.registerCancelButton);
        registerName = view.findViewById(R.id.registerName);
        registerPhoneNumber = view.findViewById(R.id.registerPhoneNumber);
        registerEmail = view.findViewById(R.id.registerEmail);
        registerPassword = view.findViewById(R.id.registerPassword);
        alertDialog.setView(view);
        dialog = alertDialog.create();
        dialog.show();


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name =  registerName.getText().toString();
                final String phoneNumber = registerPhoneNumber.getText().toString();
                final String email = registerEmail.getText().toString();
                final String password = registerPassword.getText().toString();

                customers.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (name.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || password.isEmpty()){
                            Toast.makeText(Navigation.this, "Make sure details are all filled correctly!", Toast.LENGTH_SHORT).show();
                        } else if (dataSnapshot.child(phoneNumber).exists()) {
                            Toast.makeText(Navigation.this, "Phone Number Already Registered", Toast.LENGTH_SHORT).show();
                        } else {
                            Customer customer = new Customer(name, phoneNumber, email, password);
                            customers.child(phoneNumber).setValue(customer);

                            newUserPhone = phoneNumber;
                            newUserPassword = password;

                            Toast.makeText(Navigation.this, "Thank you " + name + " for registering", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            loginDialog();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });


        registerCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }


}
