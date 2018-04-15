package com.example.keith.pregopizza.Activities.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.keith.pregopizza.Activities.Interface.ItemClickListener;
import com.example.keith.pregopizza.R;

/**
 * Created by Keith on 15/04/2018.
 */

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView menuNameText;
    public ImageView ImageView;

    private ItemClickListener itemClickListener;

    public CategoryViewHolder(View itemView){
        super(itemView);

        menuNameText = itemView.findViewById(R.id.category_menu_name);
        ImageView = itemView.findViewById(R.id.category_menu_image);

        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}
