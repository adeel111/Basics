package com.example.muhammadashfaq.eatit.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.muhammadashfaq.eatit.Model.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {

    private  static final String DB_NAME="EatItDb.db";
    private  static final  int DB_VERSION=1;



    public Database(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

          public List<Order> getCarts()
         {
            SQLiteDatabase db=getReadableDatabase();
            SQLiteQueryBuilder  qb=new SQLiteQueryBuilder();

            Cursor cursor=db.rawQuery("SELECT * from OrderDetail;",null);
             final List<Order> result=new ArrayList<>();
             if(cursor.moveToFirst()){

                 do {
                     result.add(new Order(
                             cursor.getString(cursor.getColumnIndex("Productid")),
                             cursor.getString(cursor.getColumnIndex("ProductName")),
                             cursor.getString(cursor.getColumnIndex("Quanitity")),
                             cursor.getString(cursor.getColumnIndex("Price")),
                                     cursor.getString(cursor.getColumnIndex("Discount"))));


                 }while (cursor.moveToNext());
             }
             return result;
         }

         public void addtoCart(Order order){
            SQLiteDatabase database=getReadableDatabase();
            String query=String.format("INSERT into OrderDetail(Productid,ProductName,Quanitity,Price,Discount) " +
                    "VALUES('%s','%s','%s','%s','%s');",
                    order.getProductid(),
                    order.getProductName(),
                    order.getQuanitity(),
                    order.getPrice(),
                    order.getDiscount());
            database.execSQL(query);
         }
    public void clearCart(){
        SQLiteDatabase database=getReadableDatabase();
        String query=String.format("DELETE from OrderDetail");
        database.execSQL(query);
    }


    public void addToFavorites(String foodId){
        SQLiteDatabase db=getReadableDatabase();
        String query=String.format("INSERT INTO Favorites(FoodId) VALUES('%s');",foodId);
        db.execSQL(query);
    }
    public void removeFromFavorites(String foodId){
        SQLiteDatabase db=getReadableDatabase();
        String query=String.format("DELETE FROM Favorites WHERE foodId='%s';",foodId);
        db.execSQL(query);
    }
    public boolean isFavorite(String foodId){
        SQLiteDatabase db=getReadableDatabase();
        String query=String.format("SELECT * FROM Favorites WHERE foodId='%s';",foodId);
        Cursor cursor=db.rawQuery(query,null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}

