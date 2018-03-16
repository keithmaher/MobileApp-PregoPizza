package com.example.keith.pregopizza.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.keith.pregopizza.Activities.Models.MenuM;
import com.example.keith.pregopizza.Activities.Models.Order;
import com.example.keith.pregopizza.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FoodDetails extends AppCompatActivity {

    TextView food_name, food_price, food_descriprion;
    ImageView food_image;
    FloatingActionButton cartButton;
    ElegantNumberButton quantityButton;

    String menuId = "";

    FirebaseDatabase database;
    DatabaseReference foods;

    MenuM currentFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        database = FirebaseDatabase.getInstance();
        foods = database.getReference("menu");

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

    private void  saveData(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("key_id", menuId);
        editor.putString("key_name", currentFood.getName());
        editor.putString("key_quantity", quantityButton.getNumber());
        editor.putString("key_price", currentFood.getPrice());
        Toast.makeText(this, "Order: " + currentFood.getName(), Toast.LENGTH_SHORT).show();
        editor.commit();
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
