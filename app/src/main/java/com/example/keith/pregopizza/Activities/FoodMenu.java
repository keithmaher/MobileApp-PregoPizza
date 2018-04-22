package com.example.keith.pregopizza.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.keith.pregopizza.Activities.Interface.ItemClickListener;
import com.example.keith.pregopizza.Activities.Models.MenuM;
import com.example.keith.pregopizza.Activities.Sessions.Storage;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.keith.pregopizza.Activities.ViewHolder.MenuViewHolder;
import com.example.keith.pregopizza.R;
import com.squareup.picasso.Picasso;

public class FoodMenu extends Navigation{

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodMenuList;
    FirebaseRecyclerAdapter<MenuM, MenuViewHolder> adapter;

    String categoryId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_menu, null, false);
        drawer.addView(contentView, 0);
        navigationView.setCheckedItem(R.id.nav_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database = FirebaseDatabase.getInstance();
        foodMenuList = database.getReference("menu");

        recyclerView = findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (getIntent() != null){
            categoryId = getIntent().getStringExtra("categoryId");
            if (categoryId != null){
                loadListFood();
            }
        }

    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            startActivity(new Intent(this, CategoryMenu.class));
        }
    }


    private void loadListFood() {

        adapter = new FirebaseRecyclerAdapter<MenuM, MenuViewHolder>(MenuM.class, R.layout.menu_row, MenuViewHolder.class, foodMenuList.orderByChild("menuId").equalTo(categoryId))

        {
            @Override
            protected void populateViewHolder(MenuViewHolder viewHolder, MenuM model, int position) {
                viewHolder.MenuName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.ImageView);

                final MenuM local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent foodDetails = new Intent(FoodMenu.this, FoodDetails.class);
                        foodDetails.putExtra("menuId", adapter.getRef(position).getKey());
                        startActivity(foodDetails);
                    }
                });
            }
        };

        recyclerView.setAdapter(adapter);
    }

}