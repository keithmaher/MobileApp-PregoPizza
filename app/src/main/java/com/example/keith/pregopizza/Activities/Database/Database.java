package com.example.keith.pregopizza.Activities.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.keith.pregopizza.Activities.Models.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Keith on 14/03/2018.
 */

public class Database extends SQLiteAssetHelper{
    private static final String DB_NAME="PregoPizza.db";
    private static final int DB_VER=1;
    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }


    public List<Order> getCarts()
    {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect={"ProductId", "ProductName", "Quantity", "Price"};
        String sqlTable="OrderDetails";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);

        final List<Order> result = new ArrayList<>();
        if (c.moveToFirst())
        {
            do {
                result.add(new Order(c.getString(c.getColumnIndex("ProductId")),
                        c.getString(c.getColumnIndex("ProductName")),
                        c.getString(c.getColumnIndex("Quantity")),
                        c.getString(c.getColumnIndex("Price"))

                ));
            }while (c.moveToFirst());
        }
        return result;
    }


    public void addToCart (Order order){

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO OrderDetails(ProductId, ProductName, Quantity, Price) VALUES('%s', '%s', '%s', '%s');",
                order.getProductId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice());
        db.execSQL(query);
    }

    public void cleanCart (){

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetails;");
        db.execSQL(query);
    }


}
