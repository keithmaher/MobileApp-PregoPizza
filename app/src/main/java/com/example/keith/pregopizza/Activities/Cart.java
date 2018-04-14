package com.example.keith.pregopizza.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
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

import java.util.ArrayList;

public class Cart extends Navigation{

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference orders;
    DatabaseReference request;

    TextView txtTotalPrice;
    Button placeOrder;

    AlertDialog.Builder alertDialog;
    AlertDialog dialog;
    EditText edname, ednumber, edaddress;
    Button yesButton, noButton;

    ArrayList<ArrayList<Order>> newOrder;

    FirebaseRecyclerAdapter<Order, CartViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_cart, null, false);
        drawer.addView(contentView, 0);
        navigationView.setCheckedItem(R.id.nav_gallery);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database = FirebaseDatabase.getInstance();
        orders = database.getReference("order");
        orders = orders.child("My order");
        request = database.getReference("requests");

        recyclerView = findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = findViewById(R.id.total);
        placeOrder = findViewById(R.id.placeOrder);


        loadListFoods();

//        DataSnapshot userSnapshot = null;
//        Order value = userSnapshot.getValue(Order.class);
//        newOrder = new ArrayList<>();
//        ArrayList<Order> order1 = new ArrayList<>();
//        order1.add(value);
//        newOrder.add(order1);




//        orders.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//
//                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
//
//
////                    newOrder = new ArrayList<>();
////
////                    ArrayList<Order> order1 = new ArrayList<>();
////                    order1.add(userSnapshot.getValue(Order.class));
////
////
////                    newOrder.add(order1);
//
// //                   newOrder.add(userSnapshot.getValue(Order.class));
//
//
//                            //userSnapshot.getValue(Order.class);
//
////                    int id = Integer.parseInt(order1.getProductId());
//
////                    for (int i = 0; i < snapshot.getChildrenCount(); i++){
////
// //                       newOrder = new ArrayList<>();
// //                       newOrder.add(new Order());
////                        newOrder.add(order1);
////                    }
//
////                      newOrder = new ArrayList<>();
////                      newOrder.add(order1);
//
//
//
//                      //Log.i("Message", "" + order1);
//
//                    Toast.makeText(Cart.this, ""+newOrder.toString(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });






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
        View view = getLayoutInflater().inflate(R.layout.dialog, null);
        yesButton = view.findViewById(R.id.yes);
        noButton = view.findViewById(R.id.no);
        edname = view.findViewById(R.id.edtname);
        ednumber = view.findViewById(R.id.ednumber);
        edaddress = view.findViewById(R.id.edtaddress);
        alertDialog.setView(view);
        dialog = alertDialog.create();
        dialog.show();


        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Address =  edaddress.getText().toString();
                String Name = edname.getText().toString();
                String Phone = ednumber.getText().toString();
                if (Name.isEmpty() || Address.isEmpty() || Phone.isEmpty()){
                    Toast.makeText(Cart.this, "Make sure details are all filled correctly!", Toast.LENGTH_SHORT).show();
                }else {

//                    DataSnapshot userSnapshot = null;
//                    Order value = userSnapshot.getValue(Order.class);
//                    newOrder = new ArrayList<>();
//                    ArrayList<Order> order1 = new ArrayList<>();
//                    order1.add(value);
//                    newOrder.add(order1);

                    Requests requests = new Requests(Phone, Name, Address, newOrder);
                    request.child(String.valueOf(System.currentTimeMillis())).setValue(requests);
                    orders.getRef().removeValue();
                    txtTotalPrice.setText("0");
                    dialog.dismiss();
                    Toast.makeText(Cart.this, "Thank you " + Name + " your order has been recieved and will be delivered to " +Address, Toast.LENGTH_LONG).show();
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

        adapter = new FirebaseRecyclerAdapter<Order, CartViewHolder>(Order.class,
                R.layout.cart_layout,
                CartViewHolder.class,
                orders)

        {

            @Override
            protected void populateViewHolder(CartViewHolder viewHolder, final Order model, int position) {
                TextDrawable drawable = TextDrawable.builder()
                        .buildRound(""+model.getQuantity(), Color.RED);
                viewHolder.img_cart_count.setImageDrawable(drawable);

                double price = Double.parseDouble(model.getPrice());
                double quantity = Double.parseDouble(model.getQuantity());
                final double total= price * quantity;
                String runtotal = String.valueOf(total);
                viewHolder.txt_cart_price.setText(runtotal);
                viewHolder.txt_cart_name.setText(model.getProductName());


                orders.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

//////////////////////////////////////////////////////////////////////////////////////////
                        /////////////////////////////////////////////////////////////////

                        //    THIS SECTION IS NOT WORKING.
                        //    TRYING TO CREATE A RUNNING TOTAL OF ALL ITEMS IN ORDERS.

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Order order = snapshot.getValue(Order.class);
                            double runprice = Double.parseDouble(order.getPrice());
                            double runquant = Double.parseDouble(order.getQuantity());
                            String rtotal = "";
                            rtotal = String.valueOf(rtotal+runprice*runquant);
                            txtTotalPrice.setText(rtotal);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

/////////////////////////////////////////////////////////////////////////////////////////
                ////////////////////////////////////////////////////////////////////////

            }

        };

        recyclerView.setAdapter(adapter);

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