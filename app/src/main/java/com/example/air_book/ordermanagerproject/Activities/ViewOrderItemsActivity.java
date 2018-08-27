package com.example.air_book.ordermanagerproject.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.air_book.ordermanagerproject.R;

import java.util.ArrayList;
import java.util.List;

public class ViewOrderItemsActivity extends AppCompatActivity {

    SQLiteDatabase myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order_items);

        myDB = openOrCreateDatabase("orders",MODE_PRIVATE,null);

        Intent receivedIntent = getIntent();

        Bundle bundle = receivedIntent.getExtras();
        int orderId = bundle.getInt("order_id");

        double total = 0;
        List<String> displayItems = new ArrayList<>();

        Cursor cursor = myDB.rawQuery("SELECT * FROM OrderItems WHERE ORDER_ID=" + orderId,null);

        try{
            while(cursor.moveToNext()){
                String itemName = cursor.getString(1);
                double itemPrice = cursor.getDouble(2);
                int nrItems = cursor.getInt(3);

                String toDisplay = "" + itemName + ", " + itemPrice + " $, " + nrItems;
                total = itemPrice * nrItems + total;

                displayItems.add(toDisplay);
            }
        }finally {
            cursor.close();
        }

        displayItems.add("Total: " + total + " $");

        ListView lv = (ListView)findViewById(R.id.viewOrderItemsLV);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,displayItems);

        lv.setAdapter(arrayAdapter);
    }
}
