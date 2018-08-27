package com.example.air_book.ordermanagerproject.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.air_book.ordermanagerproject.Model.Table;
import com.example.air_book.ordermanagerproject.Model.Waiter;
import com.example.air_book.ordermanagerproject.Model.WaiterAdapter;
import com.example.air_book.ordermanagerproject.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class AddOrderActivity extends AppCompatActivity {

    boolean tableSelected = false;
    boolean waiterSelected = false;
    Intent receivedIntent;
    ArrayList<Table>tables = new ArrayList<>();
    SQLiteDatabase myDB;
    int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);

        myDB = openOrCreateDatabase("orders",MODE_PRIVATE,null);

        //Reset commands for testing purposes
        //myDB.execSQL("DROP TABLE IF EXISTS OrderItems");
        //myDB.execSQL("DROP TABLE IF EXISTS ORDERS");

        receivedIntent = getIntent();
        Bundle bundle = receivedIntent.getExtras();
        tables = bundle.getParcelableArrayList("tables");
        String UID = bundle.getString("UID");


        final Spinner tablesSpinner = findViewById(R.id.addOrderTables);


        WaiterDownloader waiterDownloader = new WaiterDownloader();
        waiterDownloader.UID = UID;
        waiterDownloader.waiters = new ArrayList<>();
        waiterDownloader.execute();

        if(tables.isEmpty()){
            showError(3);
        }

        ArrayList<String> tablesChoices = new ArrayList<>();
        tablesChoices.add("None Selected");

        for (Table table: tables){
            tablesChoices.add("Table Number " + table.getNumber());
        }

        ArrayAdapter<String> tableAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, tablesChoices);
        tableAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        tablesSpinner.setAdapter(tableAdapter);

        tablesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    tableSelected = false;
                    findViewById(R.id.addOrderNextBtn).setVisibility(View.INVISIBLE);
                }else {
                    tableSelected = true;
                    if (waiterSelected) {
                        findViewById(R.id.addOrderNextBtn).setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final Spinner waiterSpinner = findViewById(R.id.addOrderWaiters);
        waiterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    waiterSelected = false;
                    findViewById(R.id.addOrderNextBtn).setVisibility(View.INVISIBLE);
                }else{
                    waiterSelected = true;
                    if(tableSelected){
                        findViewById(R.id.addOrderNextBtn).setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button nextButton = findViewById(R.id.addOrderNextBtn);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String createSQL = "CREATE TABLE IF NOT EXISTS Orders(ID INTEGER PRIMARY KEY AUTOINCREMENT, WAITER TEXT NOT NULL, TABLE_Number TEXT NOT NULL, IS_DONE INTEGER, IS_EDITING INTEGER);";
                myDB.execSQL(createSQL);
                String insertSQL = "INSERT INTO Orders (WAITER, TABLE_NUMBER, IS_DONE, IS_EDITING) VALUES ('" + waiterSpinner.getSelectedItem().toString()
                        + "','" + tablesSpinner.getSelectedItem().toString() + "',0,1);";
                myDB.execSQL(insertSQL);

                Cursor currentOrder = myDB.rawQuery("SELECT id from Orders WHERE is_editing = 1",null);
                currentOrder.moveToFirst();

                id = currentOrder.getInt(0);

                currentOrder.close();

                Intent intent = new Intent(getApplicationContext(),AddOrderContinueActivity.class);
                intent.putExtra("currentOrderID",id);
                startActivityForResult(intent,1);
            }
        });
    }

    private class WaiterDownloader extends AsyncTask<Void,Void,Void> {

        String UID;
        ArrayList<Waiter> waiters;

        @Override
        protected Void doInBackground(Void... params) {
            try(Socket socket = new Socket("10.0.2.2",5050)) {
                BufferedReader receiver = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter sendData = new PrintWriter(socket.getOutputStream(), true);

                sendData.println("3,"+UID);
                if(receiver.readLine().equals("0")) {
                    boolean doneDownloading = false;
                    while(!doneDownloading) {
                        String responseWaiter = receiver.readLine();
                        if(responseWaiter.equals("1")) {
                            doneDownloading = true;
                        } else if (responseWaiter.equals("2")) {
                            showError(2);
                        } else {
                            String[] waiterParameters = responseWaiter.split(",");

                            String firstName = waiterParameters[1];
                            String cnp = waiterParameters[0];
                            String lastName = waiterParameters[2];
                            double salary = Double.parseDouble(waiterParameters[3]);
                            int age = Integer.parseInt(waiterParameters[4]);
                            boolean isPFA = (waiterParameters[5] == "0") ? false : true;

                            Waiter waiterToAdd = new Waiter(firstName, lastName, salary, age, isPFA, cnp);
                            waiters.add(waiterToAdd);
                            sendData.println("Next Please");
                        }
                    }
                }
            }catch (IOException e) {
                e.printStackTrace();
                showError(1);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Spinner waiterSpinner = findViewById(R.id.addOrderWaiters);
            waiterSpinner.setVisibility(View.VISIBLE);

            ArrayList<String> waiterChoices = new ArrayList<>();
            waiterChoices.add("None Selected");
            for(Waiter waiter: waiters){
                waiterChoices.add(waiter.getFirstName() + " " + waiter.getLastName());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(AddOrderActivity.this, android.R.layout.simple_spinner_item, waiterChoices);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            waiterSpinner.setAdapter(adapter);
        }
    }

    private void showError(int code) {

        if(code == 1) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(AddOrderActivity.this);
                    builder.setCancelable(false);
                    builder.setTitle("Error downloading data !");
                    builder.setMessage("Something went wrong while downloading the data !");
                    builder.setPositiveButton("Understood", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            finish();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
        }else if(code == 2){
            AddOrderActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddOrderActivity.this);
                    builder.setCancelable(false);
                    builder.setTitle("No Waiters have been added!");
                    builder.setMessage("You are unable to create an order without any waiters being available");
                    builder.setPositiveButton("Understood", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            finish();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(AddOrderActivity.this);
            builder.setCancelable(false);
            builder.setTitle("No Tables have been added!");
            builder.setMessage("You are unable to create an order without any tables being available");
            builder.setPositiveButton("Understood", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    finish();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1){
            if(resultCode== Activity.RESULT_OK){
                String updateSQL = "UPDATE Orders Set is_editing = 0 WHERE id="+id;
                myDB.execSQL(updateSQL);
            }else if(requestCode==Activity.RESULT_CANCELED){
                String deleteSQL = "Delete FROM Orders Where id="+id;
                myDB.execSQL(deleteSQL);
            }else{
                String deleteSQL = "Delete FROM Orders Where id="+id;
                myDB.execSQL(deleteSQL);
            }
        }

        finish();
    }
}
