package com.example.keith.pregopizza.Activities.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.keith.pregopizza.Activities.Interface.ItemClickListener;
import com.example.keith.pregopizza.R;

/**
 * Created by Keith on 23/04/2018.
 */

public class OrderViewHolder extends RecyclerView.ViewHolder{

    public TextView orderId, orderPhone, orderAddress, status;

    private ItemClickListener itemClickListener;

    public OrderViewHolder(View itemView) {
        super(itemView);

        orderId = itemView.findViewById(R.id.order_id);
        orderPhone = itemView.findViewById(R.id.order_phone);
        orderAddress = itemView.findViewById(R.id.order_address);
        status = itemView.findViewById(R.id.order_status);

    }

}
