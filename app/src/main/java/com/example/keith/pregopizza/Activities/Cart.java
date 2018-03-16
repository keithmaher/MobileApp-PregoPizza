package com.example.keith.pregopizza.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.keith.pregopizza.Activities.Models.Order;
import com.example.keith.pregopizza.Activities.Models.Requests;
import com.example.keith.pregopizza.Activities.ViewHolder.CartViewHolder;
import com.example.keith.pregopizza.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference orders;
    DatabaseReference request;

    TextView txtTotalPrice;
    Button placeOrder;

    FirebaseRecyclerAdapter<Order, CartViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database = FirebaseDatabase.getInstance();
        orders = database.getReference("order");
        request = database.getReference("requests");

        recyclerView = findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = findViewById(R.id.total);
        placeOrder = findViewById(R.id.placeOrder);

        loadListFoods();

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });



    }

    private void showAlertDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("Last Few Step's!");
        alertDialog.setMessage("Enter Your Address: ");

//        EditText edtName = new EditText(Cart.this);
//        LinearLayout.LayoutParams name = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
//
//        alertDialog.setMessage("Enter Your Name: ");
//
//        EditText edtPhone = new EditText(Cart.this);
//        LinearLayout.LayoutParams phone = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
//
//        alertDialog.setMessage("Enter Your Name: ");
//
        final EditText edtAddress = new EditText(Cart.this);
        LinearLayout.LayoutParams addey = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);

//        edtName.setLayoutParams(name);
//        edtPhone.setLayoutParams(phone);
        edtAddress.setLayoutParams(addey);
        alertDialog.setView(edtAddress);

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String Address =  edtAddress.getText().toString();

                Requests requests = new Requests("Keith", "12345", Address);
                request.child(String.valueOf(System.currentTimeMillis())).setValue(requests);
                orders.getRef().removeValue();
               finish();

            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void loadListFoods() {

        adapter = new FirebaseRecyclerAdapter<Order, CartViewHolder>(Order.class,
                R.layout.cart_layout,
                CartViewHolder.class,
                orders.child(Order.getId()))

        {

            @Override
            protected void populateViewHolder(CartViewHolder viewHolder, Order model, int position) {
                TextDrawable drawable = TextDrawable.builder()
                .buildRound(""+model.getQuantity(), Color.RED);
                 viewHolder.img_cart_count.setImageDrawable(drawable);

                  double price = Double.parseDouble(model.getPrice());
                  double quantity = Double.parseDouble(model.getQuantity());
                  double total = price*quantity;
                  String total2 = String.valueOf(total);
                viewHolder.txt_cart_price.setText(total2);
                viewHolder.txt_cart_name.setText(model.getProductName());

                txtTotalPrice.setText(total2);

            }

        };

        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Category; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu : startActivity (new Intent(this, FoodMenu.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
