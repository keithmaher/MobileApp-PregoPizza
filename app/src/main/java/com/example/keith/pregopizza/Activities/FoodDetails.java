package com.example.keith.pregopizza.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.keith.pregopizza.Activities.Models.*;
import com.example.keith.pregopizza.Activities.Models.Order;
import com.example.keith.pregopizza.Activities.Sessions.Storage;
import com.example.keith.pregopizza.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class FoodDetails extends AppCompatActivity {

    SharedPreferences settings;

    TextView food_name, food_price, food_descriprion;
    ImageView food_image;
    FloatingActionButton cartButton;
    ElegantNumberButton quantityButton;

    String menuId = "";

    FirebaseDatabase database;
    DatabaseReference foods;
    DatabaseReference carts;

    MenuM currentFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        settings = getSharedPreferences("loginSettings", 0);

        database = FirebaseDatabase.getInstance();
        foods = database.getReference("menu");

        carts = database.getReference("carts");

        quantityButton = findViewById(R.id.number_button);
        cartButton = findViewById(R.id.cartButton);

        food_descriprion =  findViewById(R.id.food_description);
        food_name =  findViewById(R.id.food_name);
        food_price =  findViewById(R.id.food_price);
        food_image =  findViewById(R.id.img_food);

        if (getIntent() != null)
            menuId = getIntent().getStringExtra("menuId");
        if (!menuId.isEmpty()){
            getDetailFood(menuId);
        }

        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });


    }

    public void onBackPressed()
    {
        finish();
    }

    private void  saveData(){

        Order order = new Order(menuId, currentFood.getName(), quantityButton.getNumber(), currentFood.getPrice());

        if (!settings.getBoolean("loggedin", false)){
            Toast.makeText(this, "Please login/Register", Toast.LENGTH_SHORT).show();
        }else {
            carts.child(settings.getString("userphone", null)).child(menuId).setValue(order);
            Toast.makeText(this, quantityButton.getNumber() + " " + currentFood.getName() + " added to your Cart", Toast.LENGTH_SHORT).show();
        }

        Intent back = new Intent(FoodDetails.this, CategoryMenu.class);
        startActivity(back);
        finish();
    }


    private void getDetailFood(String foodId){

        foods.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentFood = dataSnapshot.getValue(MenuM.class);
                Picasso.with(getBaseContext()).load(currentFood.getImage()).into(food_image);
                food_price.setText(currentFood.getPrice());
                food_name.setText(currentFood.getName());
                food_descriprion.setText(currentFood.getToppings());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
}