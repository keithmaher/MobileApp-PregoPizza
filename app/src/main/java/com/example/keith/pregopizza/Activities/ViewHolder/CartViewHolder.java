package com.example.keith.pregopizza.Activities.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.keith.pregopizza.Activities.Interface.ItemClickListener;
import com.example.keith.pregopizza.Activities.Models.Order;
import com.example.keith.pregopizza.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Keith on 16/03/2018.
 */

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView txt_cart_name, txt_cart_price;
    public ImageView img_cart_count;

    private ItemClickListener itemClickListener;


    public CartViewHolder(View itemView) {
        super(itemView);
        txt_cart_name = itemView.findViewById(R.id.cart_item_name);
        txt_cart_price = itemView.findViewById(R.id.cart_item_price);
        img_cart_count = itemView.findViewById(R.id.cart_item_count);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//
//        String delete = "Delete";
//
//        menu.setHeaderTitle("Please Select");
//        menu.add(0,0, getAdapterPosition(), delete);
//
//    }
}
