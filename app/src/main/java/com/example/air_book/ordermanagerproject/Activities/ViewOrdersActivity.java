package com.example.air_book.ordermanagerproject.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.air_book.ordermanagerproject.R;

import java.util.ArrayList;
import java.util.List;

public class ViewOrdersActivity extends AppCompatActivity {

    SQLiteDatabase myDB;
    ArrayList<Integer> ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);

        myDB = openOrCreateDatabase("orders",MODE_PRIVATE,null);

        Cursor cursor = myDB.rawQuery("SELECT * FROM Orders",null);

        List<String> orders = new ArrayList<>();

        ids = new ArrayList<>();

        try {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String waiter = cursor.getString(1);
                String table = cursor.getString(2);

                String toDisplay = "" + id + ", " + waiter + ", " + table;

                ids.add(id);
                orders.add(toDisplay);
            }
        } finally {
            cursor.close();
        }

        ListView lv = (ListView)findViewById(R.id.viewOrdersLV);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,orders);

        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),ViewOrderItemsActivity.class);
                intent.putExtra("order_id",ids.get(position));
                startActivity(intent);
            }
        });
    }
}
