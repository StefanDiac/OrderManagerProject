package com.example.air_book.ordermanagerproject.Activities;

import android.content.Intent;
import android.net.Uri;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.air_book.ordermanagerproject.Model.Order;
import com.example.air_book.ordermanagerproject.Model.Table;
import com.example.air_book.ordermanagerproject.Model.Waiter;
import com.example.air_book.ordermanagerproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public ArrayList<Order> orders = new ArrayList<>();
    public ArrayList<Table> tables = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            String myJson = PreferenceManager.getDefaultSharedPreferences(this).getString("TablesJson","");
            JSONObject tablesJson = new JSONObject(myJson);
            JSONArray tablesArrayJson = (JSONArray)tablesJson.get("Tables");

            for(int i = 0; i<tablesArrayJson.length();i++){
                JSONObject table = tablesArrayJson.getJSONObject(i);

                Table addTable = new Table(table.getInt("UID"),table.getInt("Seats"),table.getBoolean("Smoking"));
                tables.add(addTable);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void onRestaurantLocationBtnPressed(View view) {
        Intent intent = new Intent(getApplicationContext(), RestaurantLocationActivity.class);
        intent.putExtra("latitude", 44.4475513);
        intent.putExtra("longitude", 26.0967866);
        startActivity(intent);
    }

    public void onManageRestuarantBtnPressed(View view) {

        //waiters.add(new Waiter("James","Jamie",2343.5,24,true));
        //waiters.add(new Waiter("Lucy","Johnson",2343.5,21,false));
        Intent receivedUID = getIntent();
        Bundle bundle = receivedUID.getExtras();
        String UID = bundle.getString("UID");

        Intent intent = new Intent(getApplicationContext(), ManagerActivity.class);
        intent.putExtra("tables", tables);
        intent.putExtra("UID", UID);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        saveData();
    }

    @Override
    protected void onPause() {
        super.onPause();

        saveData();
    }

    public void saveData() {
        try {
            JSONObject tablesJSON = new JSONObject();

            JSONArray tablesJsonArray = new JSONArray();

            for (Table table : tables) {
                JSONObject currentTable = new JSONObject();
                currentTable.put("UID", table.getNumber());
                currentTable.put("Seats", table.getNumberOfSeats());
                currentTable.put("Smoking", table.isSmokable());
                tablesJsonArray.put(currentTable);
            }

            tablesJSON.put("Tables", tablesJsonArray);

            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("TablesJson",tablesJSON.toString()).apply();
        }catch (JSONException jsonExp) {
            jsonExp.printStackTrace();
        }
    }

    public void onViewOrdersBtnPressed(View view) {

        Intent intent = new Intent(getApplicationContext(), ViewOrdersActivity.class);
        startActivity(intent);

    }

    public void onAddOrderBtnPressed(View view) {
        Intent receivedUID = getIntent();
        Bundle bundle = receivedUID.getExtras();
        String UID = bundle.getString("UID");

        Intent intent = new Intent(getApplicationContext(), AddOrderActivity.class);
        intent.putExtra("tables",tables);
        intent.putExtra("UID", UID);
        startActivity(intent);
    }
}
