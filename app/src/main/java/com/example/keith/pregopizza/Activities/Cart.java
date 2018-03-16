package com.example.keith.pregopizza.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.keith.pregopizza.Activities.Models.Order;
import com.example.keith.pregopizza.Activities.Database.Database;
import com.example.keith.pregopizza.Activities.ViewHolder.CartAdapter;
import com.example.keith.pregopizza.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference request;

    TextView txtTotalPrice;
    Button placeOrder;

    List<Order> cart = new ArrayList<Order>();

    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database = FirebaseDatabase.getInstance();
        request = database.getReference("requests");

        recyclerView = findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = findViewById(R.id.total);
        placeOrder = findViewById(R.id.placeOrder);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        String id = pref.getString("key_id", null);
        String name = pref.getString("key_name", null);
        String quantity = pref.getString("key_quantity", null);
        String price = pref.getString("key_price", null);

        Toast.makeText(this, "Showing: " + name, Toast.LENGTH_SHORT).show();

        Order orders = new Order(id, name, quantity, price);

        cart.add(0,orders);

        loadListFoods();

    }

    private void loadListFoods() {

        adapter = new CartAdapter(cart, this);
        recyclerView.setAdapter(adapter);

        Double total = 0.0;
        for (Order order:cart)
            total+=(Double.parseDouble(order.getPrice()))*(Double.parseDouble(order.getQuantity()));
        Locale locale = new Locale("en", "IE");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        txtTotalPrice.setText(fmt.format(total));

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
