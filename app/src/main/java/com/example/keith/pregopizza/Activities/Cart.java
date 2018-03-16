package com.example.keith.pregopizza.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.keith.pregopizza.Activities.Interface.ItemClickListener;
import com.example.keith.pregopizza.Activities.Models.MenuM;
import com.example.keith.pregopizza.Activities.Models.Order;
import com.example.keith.pregopizza.Activities.Models.Requests;
import com.example.keith.pregopizza.Activities.ViewHolder.CartViewHolder;
import com.example.keith.pregopizza.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class Cart extends AppCompatActivity {

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

    List<Order> newOrder;

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
                if (recyclerView.getChildCount() == 0) {
                    Toast.makeText(Cart.this, "Nothing in your Cart", Toast.LENGTH_SHORT).show();
                } else {
                    showAlertDialog();
                }
            }
        });

    }

    private void showAlertDialog() {

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
                    Requests requests = new Requests(Phone, Name, Address, newOrder);
                    request.child(String.valueOf(System.currentTimeMillis())).setValue(requests);
                    orders.getRef().removeValue();
                    txtTotalPrice.setText("0");
                    dialog.dismiss();
                    Toast.makeText(Cart.this, "Your Order has been submitted to our Database", Toast.LENGTH_SHORT).show();
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
                orders.child(Order.getId()))

        {

            @Override
            protected void populateViewHolder(CartViewHolder viewHolder, final Order model, int position) {
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
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {

            case R.id.orders : Toast.makeText(this, "Available in V2", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu : startActivity (new Intent(this, FoodMenu.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
