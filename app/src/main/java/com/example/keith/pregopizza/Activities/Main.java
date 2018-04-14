package com.example.keith.pregopizza.Activities;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.keith.pregopizza.Activities.Models.Customer;
import com.example.keith.pregopizza.Activities.Sessions.Storage;
import com.example.keith.pregopizza.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Main extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference customers;

    Button mainMenuButton;

    AlertDialog.Builder alertDialog;
    AlertDialog dialog;
    Button loginbutton;
    Button loginCancelButton;
    TextView loginPhoneNumber;
    TextView loginPassword;

    Button registerButton;
    Button registerCancelButton;
    TextView registerName;
    TextView registerPhoneNumber;
    TextView registerEmail;
    TextView registerPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        customers = database.getReference("customers");

        mainMenuButton = findViewById(R.id.mainMenuButton);

        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main.this, FoodMenu.class));
            }
        });

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
                final String phoneNumber =  loginPhoneNumber.getText().toString();
                final String password = loginPassword.getText().toString();

                customers.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child(phoneNumber).exists()) {
                            Customer customer = dataSnapshot.child(phoneNumber).getValue(Customer.class);
                            if (customer.getPassword().equals(password)) {
                                Intent menu = new Intent(Main.this, FoodMenu.class);
                                Storage.currentCustomer = customer;
                                startActivity(menu);
                                dialog.dismiss();
                            } else {
                                Toast.makeText(Main.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(Main.this, "User not Registered", Toast.LENGTH_SHORT).show();
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

                customers.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (name.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || password.isEmpty()){
                            Toast.makeText(Main.this, "Make sure details are all filled correctly!", Toast.LENGTH_SHORT).show();
                        } else if (dataSnapshot.child(phoneNumber).exists()) {
                            Toast.makeText(Main.this, "Phone Number Already Registered", Toast.LENGTH_SHORT).show();
                        } else {
                            Customer customer = new Customer(name, phoneNumber, email, password);
                            customers.child(phoneNumber).setValue(customer);
                            Toast.makeText(Main.this, "Thank you " + name + " for registering", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
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

