package com.example.keith.pregopizza.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.keith.pregopizza.Activities.Models.Customer;
import com.example.keith.pregopizza.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Navigation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences settings;

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
    DatabaseReference newCustomes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        settings = getSharedPreferences("loginSettings", 0);

        database = FirebaseDatabase.getInstance();
        customers = database.getReference("customers");
        newCustomes = database.getReference("customers");

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

        if (!settings.getBoolean("loggedin", false)){
            nav_name.setText("You need to login");
            nav_email.setText("to view more options");
            nav_Menu.findItem(R.id.nav_logout).setVisible(false);
            nav_Menu.findItem(R.id.nav_cart).setVisible(false);
            nav_Menu.findItem(R.id.nav_profile).setVisible(false);
            nav_Menu.findItem(R.id.nav_orders).setVisible(false);
        }else {

            String username = settings.getString("username", null);
            String useremail = settings.getString("useremail", null);
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
        } else if (id == R.id.nav_orders) {
            startAnimatedActivity(new Intent(getApplicationContext(), Order.class));
            finish();
        }else if (id == R.id.nav_location) {
            startAnimatedActivity(new Intent(getApplicationContext(), Contact.class));
            finish();
        } else if (id == R.id.nav_profile) {
            startAnimatedActivity(new Intent(getApplicationContext(), Profile.class));
            finish();
        } else if (id == R.id.nav_login) {
            loginDialog();
        } else if (id == R.id.nav_logout) {
            logout();
        } else if (id == R.id.nav_register) {
            registerDialog();
        }

        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logout() {

            AlertDialog.Builder builder = new AlertDialog.Builder(Navigation.this);
            builder.setTitle("Logging Out");
            String username = settings.getString("username", null);
            builder.setMessage(username + " you are about to logout, are you sure?");

            builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(Navigation.this, "Thank you come again!", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = getSharedPreferences("loginSettings", 0).edit();
                    editor.putBoolean("loggedin", false);
                    editor.commit();
                    Intent logout = new Intent(getApplicationContext(), CategoryMenu.class);
                    logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(logout);
                    finish();
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


        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String phoneNumber = loginPhoneNumber.getText().toString();
                final String password = loginPassword.getText().toString();

                customers.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child(phoneNumber).exists()) {
                            Customer customer = dataSnapshot.child(phoneNumber).getValue(Customer.class);
                            if (customer.getPassword().equals(password)) {
                                Toast.makeText(Navigation.this, "Welcome "+customer.getName(), Toast.LENGTH_SHORT).show();
                                Intent menu = new Intent(Navigation.this, CategoryMenu.class);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putBoolean("loggedin", true);
                                editor.putString("username", customer.getName());
                                editor.putString("userphone", phoneNumber);
                                editor.putString("useremail", customer.getEmail());
                                editor.putString("password", password);
                                editor.commit();
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

                newCustomes.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (name.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || password.isEmpty()){
                            Toast.makeText(Navigation.this, "Make sure details are all filled correctly!", Toast.LENGTH_SHORT).show();
                        } else if (dataSnapshot.child(phoneNumber).exists()) {
                            Toast.makeText(Navigation.this, "Phone Number Already Registered", Toast.LENGTH_SHORT).show();
                        } else {
                            Customer customer = new Customer(name, phoneNumber, email, password);
                            newCustomes.child(phoneNumber).setValue(customer);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putBoolean("loggedin", true);
                            editor.putString("username", name);
                            editor.putString("userphone", phoneNumber);
                            editor.putString("useremail", email);
                            editor.putString("password", password);
                            editor.commit();
                            Toast.makeText(Navigation.this, "Thank you " + name + " for registering", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            Intent login = new Intent(getApplicationContext(), CategoryMenu.class);
                            startActivity(login);
                            finish();
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