package com.example.keith.pregopizza.Activities.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.keith.pregopizza.Activities.Models.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Keith on 14/03/2018.
 */

public class Database extends SQLiteOpenHelper{


    public Database(Context context) {
        super(context, Util.DB_NAME, null, Util.DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db){
        //SQL = Structured Query Language
        String CREATE_MENU_TABLE = "CREATE TABLE " + Util.TABLE_NAME + "("
                + Util.KEY_ID + " INTEGER PRIMARY KEY,"
                + Util.KEY_NAME + " TEXT,"
                +Util.KEY_QUANTITY + " TEXT,"
                +Util.KEY_PRICE + " TEXT" +")";

        db.execSQL(CREATE_MENU_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

        db.execSQL("DROP TABLE IF EXISTS 'Util.TABLE_NAME'");

        onCreate(db);
    }

    public void addToCart(Order order){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(Util.KEY_ID, order.getProductId());
        value.put(Util.KEY_NAME, order.getProductName());
        value.put(Util.KEY_QUANTITY, order.getQuantity());
        value.put(Util.KEY_PRICE, order.getPrice());

        // INSEERT TO ROW
        db.insertOrThrow(Util.TABLE_NAME, null, value);
        db.close();
    }

    //Get a Order

//    public Order getOrder(int id){
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query( Util.TABLE_NAME , new String[] {Util.KEY_ID, Util.KEY_STARTER,
//                        Util.KEY_MAIN, Util.KEY_DESERT, Util.KEY_DRINK}, Util.KEY_ID + "=?"
//                ,new String[] {String.valueOf(id)}
//                ,null, null, null, null  );
//
//        if (cursor != null )
//            cursor.moveToFirst();
//
//        Order order = new Order(Integer.parseInt(cursor.getString(0)),
//                cursor.getString(1), cursor.getString(2), cursor.getString(3),
//                cursor.getString(4));
//
//        return order;
//    }


    public List<Order> getCarts() {

        SQLiteDatabase db = this.getReadableDatabase();
        List<Order> orderList = new ArrayList<>();

        //Select all orders

        String selectAll = "SELECT * FROM " +Util.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectAll, null);

        //Loop through our orders

        if (cursor.moveToFirst()){
            do{
                Order order = new Order();
                order.setProductId(cursor.getString(0));
                order.setProductName(cursor.getString(1));
                order.setQuantity(cursor.getString(2));
                order.setPrice(cursor.getString(3));
                orderList.add(order);
            }while (cursor.moveToNext());
        }

        return orderList;
    }


    //update order

//    public int updateOrder(Order order){
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(Util.KEY_STARTER, order.getStarter());
//        values.put(Util.KEY_MAIN, order.getMain());
//        values.put(Util.KEY_DESERT, order.getDesert());
//        values.put(Util.KEY_DRINK, order.getDrink());
//
//        //update row
//        return db.update(Util.TABLE_NAME, values,
//                Util.KEY_ID + "=?",
//                new String[] {String.valueOf(order.getId())});
//
//    }


//    public void deleteOrder(Order order){
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(Util.TABLE_NAME, Util.KEY_ID + "=?",
//                new String[]{String.valueOf(order.getId())});
//
//        db.close();
//
//    }

//    //Get Order Count
//    public int getOrderCount(){
//        String countQuery = "SELECT * FROM " +Util.TABLE_NAME;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//
//
//        //cursor.close();
//
//        return cursor.getCount();
//
//    }


    public void cleanCart (){

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetails;");
        db.execSQL(query);
    }


}
