package com.example.keith.pregopizza.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.keith.pregopizza.Activities.Interface.ItemClickListener;
import com.example.keith.pregopizza.Activities.Models.Category;
import com.example.keith.pregopizza.Activities.ViewHolder.CategoryViewHolder;
import com.example.keith.pregopizza.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class CategoryMenu extends Navigation {

    FirebaseDatabase database;
    DatabaseReference category;
    FirebaseRecyclerAdapter<Category,CategoryViewHolder> adapter;

    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_category_menu, null, false);
        drawer.addView(contentView, 0);
        navigationView.setCheckedItem(R.id.nav_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database = FirebaseDatabase.getInstance();
        category = database.getReference("category");

        recycler_menu = findViewById(R.id.recycler_category_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);

        loadMenu();
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

    private void loadMenu() {

        adapter = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(Category.class,R.layout.category_row,CategoryViewHolder.class,category) {
            @Override
            protected void populateViewHolder(CategoryViewHolder viewHolder, Category model, int position) {
                viewHolder.menuNameText.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.ImageView);
                final Category clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent MenuList = new Intent(CategoryMenu.this, FoodMenu.class);
                        MenuList.putExtra("categoryId",adapter.getRef(position).getKey());
                        startActivity(MenuList);
                        finish();
                    }
                });
            }
        };
        recycler_menu.setAdapter(adapter);
    }

}
