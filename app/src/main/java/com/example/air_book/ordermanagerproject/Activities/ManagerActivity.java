package com.example.air_book.ordermanagerproject.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.air_book.ordermanagerproject.Model.Table;
import com.example.air_book.ordermanagerproject.Model.Waiter;
import com.example.air_book.ordermanagerproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ManagerActivity extends AppCompatActivity {

    Intent receivedIntent;
    ArrayList<Table>tables = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        receivedIntent = getIntent();
        Bundle bundle = receivedIntent.getExtras();
        tables = bundle.getParcelableArrayList("tables");

        Button statsBtn = findViewById(R.id.restaurantStatsBtn);

        statsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StatisticsActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onAddTableBtnPressed(View view) {
        Intent intent = new Intent(getApplicationContext(), AddTableActivity.class);
        intent.putExtra("tables",tables);
        startActivityForResult(intent,1);
    }

    public void onViewTablesBtnPressed(View view) {
        Intent intent = new Intent(getApplicationContext(), ViewTablesActivity.class);
        intent.putExtra("tables",tables);
        startActivity(intent);
    }

    public void onAddWaiterBtnPressed(View view) {
        Intent intent = new Intent(getApplicationContext(), AddWaiterActivity.class);
        Bundle bundle = receivedIntent.getExtras();
        String UID = bundle.getString("UID");
        intent.putExtra("UID", UID);
        startActivity(intent);
    }

    public void onViewWaitersBtnPressed(View view) {
        Intent intent = new Intent(getApplicationContext(), ViewWaitersActivity.class);
        Bundle bundle = receivedIntent.getExtras();
        String UID = bundle.getString("UID");
        intent.putExtra("UID", UID);
        startActivity(intent);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = data.getExtras();
                tables = bundle.getParcelableArrayList("tables");
            }
        }
    }

    public void onAddMenuItemBtnPressed(View view) {
        Intent intent = new Intent(getApplicationContext(), AddMenuItemActivity.class);
        Bundle bundle = receivedIntent.getExtras();
        String UID = bundle.getString("UID");
        intent.putExtra("UID",UID);
        startActivity(intent);
    }

    public void onViewMenuBtnPressed(View view) {
        Intent intent = new Intent(getApplicationContext(), ViewMenuActivity.class);
        startActivity(intent);
    }

    public void onLinkAppBtnPressed(View view) {
        Bundle bundle = receivedIntent.getExtras();
        String UID = bundle.getString("UID");

        AlertDialog.Builder builder = new AlertDialog.Builder(ManagerActivity.this);
        builder.setCancelable(true);
        builder.setTitle("Link ID:");
        builder.setMessage(UID);
        builder.setPositiveButton("Understood", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    @Override
    protected void onPause() {
        super.onPause();

        saveData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        saveData();
    }
}
