package com.example.keith.pregopizza.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.keith.pregopizza.Activities.Interface.ItemClickListener;
import com.example.keith.pregopizza.Activities.Models.Order;
import com.example.keith.pregopizza.Activities.Models.Requests;
import com.example.keith.pregopizza.Activities.ViewHolder.CartViewHolder;
import com.example.keith.pregopizza.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Cart extends Navigation{

    SharedPreferences settings;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference carts;
    DatabaseReference orders;

    TextView txtTotalPrice;
    Button placeOrder;

    AlertDialog.Builder alertDialog;
    AlertDialog dialog;
    EditText cart_address;
    TextView cart_name, cart_number;
    Button yesButton, noButton;

    List<Order> cart = new ArrayList<>();


    FirebaseRecyclerAdapter<Order, CartViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_cart, null, false);
        drawer.addView(contentView, 0);
        navigationView.setCheckedItem(R.id.nav_cart);

        settings = getSharedPreferences("loginSettings", 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database = FirebaseDatabase.getInstance();
        carts = database.getReference("carts");

        final String user = settings.getString("userphone", null);
        carts = carts.child(user);

        orders = database.getReference("orders");

        recyclerView = findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = findViewById(R.id.total);
        placeOrder = findViewById(R.id.placeOrder);

        loadListFoods();

        carts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {


                Double rtotal = 0.0;
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    Order order = userSnapshot.getValue(Order.class);
                    cart.add(order);

                    double runprice = Double.parseDouble(order.getPrice());
                    double runquant = Double.parseDouble(order.getQuantity());
                    double runrun = runprice * runquant;
                    rtotal = rtotal + runrun;

                }
                DecimalFormat number = new DecimalFormat("#0.00");
                txtTotalPrice.setText(String.valueOf(number.format(rtotal)));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerView.getChildCount() == 0) {
                    Toast.makeText(Cart.this, "Nothing in your Cart", Toast.LENGTH_SHORT).show();
                } else {
                    checkoutDialog();
                }
            }
        });

    }

    private void checkoutDialog() {

        alertDialog = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.checkout, null);
        yesButton = view.findViewById(R.id.yes);
        noButton = view.findViewById(R.id.no);
        cart_name = view.findViewById(R.id.cart_name);
        cart_number = view.findViewById(R.id.cart_number);
        cart_address = view.findViewById(R.id.cart_address);
        alertDialog.setView(view);
        dialog = alertDialog.create();
        dialog.show();

        cart_name.setText(settings.getString("username", null));
        cart_number.setText(settings.getString("userphone", null));

        final String name = cart_name.getText().toString();
        final String phone = cart_number.getText().toString();

        yesButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                String address =  cart_address.getText().toString();
                if (name.isEmpty() || address.isEmpty() || phone.isEmpty()){
                    Toast.makeText(Cart.this, "Make sure details are all filled correctly!", Toast.LENGTH_SHORT).show();
                }else {
                    Requests requests = new Requests(phone, name, address, cart);

                    String timeStamp = String.valueOf(System.currentTimeMillis());

                    orders.child(phone).child(timeStamp).setValue(requests);
                    carts.getRef().removeValue();
                    txtTotalPrice.setText("0");
                    dialog.dismiss();
                    Toast.makeText(Cart.this, "Thank you " + name + " your order has been recieved", Toast.LENGTH_LONG).show();
                }
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    private void loadListFoods() {

        adapter = new FirebaseRecyclerAdapter<Order, CartViewHolder>(Order.class, R.layout.cart_layout, CartViewHolder.class, carts) {

            @Override
            protected void populateViewHolder(CartViewHolder viewHolder, final Order model, int position) {
                TextDrawable drawable = TextDrawable.builder().buildRound(""+model.getQuantity(), Color.rgb(168,223,0));
                viewHolder.img_cart_count.setImageDrawable(drawable);

                double price = Double.parseDouble(model.getPrice());
                double quantity = Double.parseDouble(model.getQuantity());
                final double total= price * quantity;
                String runtotal = String.valueOf(total);
                viewHolder.txt_cart_price.setText(runtotal);
                viewHolder.txt_cart_name.setText(model.getProductName());

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        int id = Integer.parseInt(model.getProductId());
                        String amount = model.getQuantity();
                        String name = model.getProductName();
                        // Toast.makeText(Cart.this, "" +id, Toast.LENGTH_SHORT).show();
                        deleteItemDialog(id, amount, name);
                    }
                });

            }

        };

        recyclerView.setAdapter(adapter);

    }


    public void deleteItemDialog(final int id, String amount, String name){
        AlertDialog.Builder builder = new AlertDialog.Builder(Cart.this);
        builder.setTitle("Item Removal");
        builder.setMessage("You are about to remove " +amount + " " + name.toLowerCase() + " from your cart" );

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                carts.child(String.valueOf(id)).removeValue();
                finish();
                startActivity(getIntent());
                Toast.makeText(Cart.this, "Succesfully Deleted ", Toast.LENGTH_SHORT).show();
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