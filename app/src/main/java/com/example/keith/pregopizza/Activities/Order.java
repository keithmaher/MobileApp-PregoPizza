package com.example.keith.pregopizza.Activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.keith.pregopizza.Activities.Models.Requests;
import com.example.keith.pregopizza.Activities.ViewHolder.OrderViewHolder;
import com.example.keith.pregopizza.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Order extends Navigation {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Requests, OrderViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference orders;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        database = FirebaseDatabase.getInstance();
        orders = database.getReference("orders");

        String user = settings.getString("userphone", null);
        orders = orders.child(user);

        recyclerView = findViewById(R.id.orderList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders();


    }

    private void loadOrders() {

        adapter = new FirebaseRecyclerAdapter<Requests, OrderViewHolder>(Requests.class, R.layout.order_card, OrderViewHolder.class, orders) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Requests model, int position) {
                viewHolder.orderId.setText(adapter.getRef(position).getKey());
                viewHolder.orderPhone.setText(model.getPhone());
                viewHolder.orderAddress.setText(model.getAddress());
                viewHolder.status.setText(model.getStatus());
            }
        };
        recyclerView.setAdapter(adapter);

    }
}
