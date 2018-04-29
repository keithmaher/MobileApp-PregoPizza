package com.example.keith.pregopizza.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.keith.pregopizza.Activities.Models.Requests;
import com.example.keith.pregopizza.Activities.ViewHolder.OrderViewHolder;
import com.example.keith.pregopizza.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class Order extends Navigation {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Requests, OrderViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_order, null, false);
        drawer.addView(contentView, 0);
        navigationView.setCheckedItem(R.id.nav_orders);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database = FirebaseDatabase.getInstance();
        orders = database.getReference("orders");

        String user = settings.getString("userphone", null);
        orders = orders.child(user);

        recyclerView = findViewById(R.id.orderList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        orders.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Toast.makeText(Order.this, "Orders Updating", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        loadOrders();

    }

    private void loadOrders() {

        adapter = new FirebaseRecyclerAdapter<Requests, OrderViewHolder>(Requests.class, R.layout.order_card, OrderViewHolder.class, orders.orderByChild("status")) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Requests model, int position) {
                viewHolder.orderId.setText(adapter.getRef(position).getKey());
                viewHolder.orderPhone.setText(model.getPhone());
                viewHolder.orderAddress.setText(model.getAddress());
                viewHolder.status.setText(orderBy(model.getStatus()));
            }
        };
        recyclerView.setAdapter(adapter);

    }

    private String orderBy(String status) {

        if (status.equals("0"))
            return "Placed";
        else if (status.equals("1"))
            return "Recieved";
        else if (status.equals("2"))
            return "On it's way";
        else
            return "Delivered";
    }
}
