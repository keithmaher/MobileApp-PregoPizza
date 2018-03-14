package com.example.keith.pregopizza.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.example.keith.pregopizza.Activities.Interface.ItemClickListener;
import com.example.keith.pregopizza.Activities.Models.MenuM;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.keith.pregopizza.Activities.ViewHolder.MenuViewHolder;
import com.example.keith.pregopizza.R;
import com.squareup.picasso.Picasso;

public class FoodMenu extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodMenuList;
    FirebaseRecyclerAdapter<MenuM, MenuViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database = FirebaseDatabase.getInstance();
        foodMenuList = database.getReference("menu");

        recyclerView = findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        loadListFood();


    }

    private void loadListFood() {

        adapter = new FirebaseRecyclerAdapter<MenuM, MenuViewHolder>(MenuM.class,
                R.layout.menu_row,
                MenuViewHolder.class,
                foodMenuList)

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Category; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
                case R.id.menuHome : startActivity (new Intent(this, FoodMenu.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
