package com.example.air_book.ordermanagerproject.Activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.air_book.ordermanagerproject.Model.Deserts;
import com.example.air_book.ordermanagerproject.Model.Drink;
import com.example.air_book.ordermanagerproject.Model.ItemType;
import com.example.air_book.ordermanagerproject.Model.MainCourse;
import com.example.air_book.ordermanagerproject.Model.Orderable;
import com.example.air_book.ordermanagerproject.Model.Starter;
import com.example.air_book.ordermanagerproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddOrderContinueActivity extends AppCompatActivity {

    ArrayList<Starter> starters = new ArrayList<>();
    ArrayList<MainCourse> mainCourses = new ArrayList<>();
    ArrayList<Deserts> deserts = new ArrayList<>();
    ArrayList<Drink> drinks = new ArrayList<>();
    ArrayList<Orderable> addedItems = new ArrayList<>();
    //boolean typeSelected = false;
    //boolean itemSelected = false;
    SQLiteDatabase database;
    private FirebaseAuth mAuth;
    String myUID;
    DatabaseReference firebaseDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order_continue);

        database = openOrCreateDatabase("orders",MODE_PRIVATE,null);

        Intent receivedIntent = getIntent();
        Bundle bundle = receivedIntent.getExtras();
        final int orderID = bundle.getInt("currentOrderID");

        final Spinner itemSpinner = (Spinner)findViewById(R.id.addOrderItem);
        myUID = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("UID", "");

        final Spinner itemTypeSpinner = (Spinner)findViewById(R.id.addOrderItemType);
        ArrayList<String> itemTypes = new ArrayList<>();
        itemTypes.add("None");
        for(ItemType type: ItemType.values()){
            itemTypes.add(type.toString());
        }

        final ArrayAdapter<String> itemTypeAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,itemTypes);
        itemTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemTypeSpinner.setAdapter(itemTypeAdapter);

        final ArrayList<Orderable> downloadedStarters = downloadItemsFromFirebase(ItemType.STARTER_DISH);
        final ArrayList<Orderable> downloadedCourses = downloadItemsFromFirebase(ItemType.MAIN_COURSE);
        final ArrayList<Orderable> downloadedDeserts = downloadItemsFromFirebase(ItemType.DESERT);
        final ArrayList<Orderable> downloadedDrinks = downloadItemsFromFirebase(ItemType.DRINKS);

        itemTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){

                    itemSpinner.setVisibility(View.VISIBLE);

                    switch(position){
                        case 1:
                            if(starters.isEmpty()){
                                for (Orderable item: downloadedStarters) {
                                    starters.add((Starter)item);
                                }
                                ArrayList<String> starterNames = new ArrayList<String>();
                                starterNames.add("None");
                                for(Starter starter: starters){
                                    starterNames.add(starter.getName());
                                }
                                itemSpinner.setEnabled(true);
                                loadToSpinner(starterNames,itemSpinner);
                            }else{
                                ArrayList<String> starterNames = new ArrayList<String>();
                                starterNames.add("None");
                                for(Starter starter: starters){
                                    starterNames.add(starter.getName());
                                }
                                loadToSpinner(starterNames,itemSpinner);
                            }
                            break;
                        case 2:
                            if(mainCourses.isEmpty()){
                                itemSpinner.setEnabled(false);
                                for (Orderable item: downloadedCourses) {
                                    mainCourses.add((MainCourse) item);
                                }
                                ArrayList<String> mainCourseNames = new ArrayList<String>();
                                mainCourseNames.add("None");
                                for(MainCourse mainCourse: mainCourses){
                                    mainCourseNames.add(mainCourse.getName());
                                }
                                itemSpinner.setEnabled(true);
                                loadToSpinner(mainCourseNames,itemSpinner);
                            }else{
                                ArrayList<String> mainCourseNames = new ArrayList<String>();
                                mainCourseNames.add("None");
                                for(MainCourse mainCourse: mainCourses){
                                    mainCourseNames.add(mainCourse.getName());
                                }
                                itemSpinner.setEnabled(true);
                                loadToSpinner(mainCourseNames,itemSpinner);
                            }
                            break;
                        case 3:
                            if(deserts.isEmpty()){
                                itemSpinner.setEnabled(false);

                                for (Orderable item: downloadedDeserts) {
                                    deserts.add((Deserts) item);
                                }

                                ArrayList<String> desertNames = new ArrayList<String>();
                                desertNames.add("None");
                                for(Deserts desert: deserts){
                                    desertNames.add(desert.getName());
                                }
                                itemSpinner.setEnabled(true);
                                loadToSpinner(desertNames,itemSpinner);
                            }else{
                                ArrayList<String> desertNames = new ArrayList<String>();
                                desertNames.add("None");
                                for(Deserts desert: deserts){
                                    desertNames.add(desert.getName());
                                }
                                itemSpinner.setEnabled(true);
                                loadToSpinner(desertNames,itemSpinner);
                            }
                            break;
                        case 4:
                            if(drinks.isEmpty()){
                                itemSpinner.setEnabled(false);

                                for (Orderable item: downloadedDrinks) {
                                    drinks.add((Drink) item);
                                }

                                ArrayList<String> drinkNames = new ArrayList<String>();
                                drinkNames.add("None");
                                for(Drink drink: drinks){
                                    drinkNames.add(drink.getName());
                                }
                                itemSpinner.setEnabled(true);
                                loadToSpinner(drinkNames,itemSpinner);
                            }else{
                                ArrayList<String> drinkNames = new ArrayList<String>();
                                drinkNames.add("None");
                                for(Drink drink: drinks){
                                    drinkNames.add(drink.getName());
                                }
                                itemSpinner.setEnabled(true);
                                loadToSpinner(drinkNames,itemSpinner);
                            }
                            break;
                    }
                }else{
                    findViewById(R.id.addOrderItem).setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        itemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0){
                    findViewById(R.id.addItemBtn).setVisibility(View.VISIBLE);
                }else{
                    findViewById(R.id.addItemBtn).setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final Button addItemButton = (Button)findViewById(R.id.addItemBtn);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = itemSpinner.getSelectedItemPosition();
                int typePosition = itemTypeSpinner.getSelectedItemPosition();

                switch(typePosition){
                    case 1:
                        Starter starterToAdd = starters.get(itemPosition-1);
                        addedItems.add(starterToAdd);

                        database.execSQL("CREATE TABLE IF NOT EXISTS OrderItems(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "NAME TEXT NOT NULL, PRICE REAL NOT NULL, NR_ITEMS INTEGER NOT NULL, ORDER_ID INTEGER NOT NULL,FOREIGN KEY(ORDER_ID) REFERENCES Orders(ID))");

                        if(checkIfRecordExists(database,starterToAdd.getName(),orderID)){
                            String getNrItemsSQL = "SELECT NR_ITEMS FROM OrderItems WHERE NAME="
                                    + "'" + starterToAdd.getName() + "' AND ORDER_ID=" + orderID;

                            Cursor nrItemsCursor = database.rawQuery(getNrItemsSQL,null);
                            nrItemsCursor.moveToFirst();

                            int nrItems = nrItemsCursor.getInt(0);
                            nrItems = nrItems + 1;

                            nrItemsCursor.close();

                            String updateSQL = "UPDATE OrderItems SET NR_ITEMS=" + nrItems + " WHERE NAME="
                                    + "'" + starterToAdd.getName() + "' AND ORDER_ID=" + orderID;

                            database.execSQL(updateSQL);
                        } else {
                            String insertSQL = "INSERT INTO OrderItems (NAME, PRICE, NR_ITEMS, ORDER_ID) VALUES('"+ starterToAdd.getName()
                                    + "'," + starterToAdd.getPrice() + ",1,"+ orderID +")";
                            database.execSQL(insertSQL);
                        }
                        break;
                    case 2:
                        MainCourse mainCourseToAdd = mainCourses.get(itemPosition-1);
                        addedItems.add(mainCourseToAdd);

                        database.execSQL("CREATE TABLE IF NOT EXISTS OrderItems(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "NAME TEXT NOT NULL, PRICE REAL NOT NULL, NR_ITEMS INTEGER NOT NULL, ORDER_ID INTEGER NOT NULL, FOREIGN KEY(ORDER_ID) REFERENCES Orders(ID))");

                        if(checkIfRecordExists(database,mainCourseToAdd.getName(),orderID)){
                            String getNrItemsSQL = "SELECT NR_ITEMS FROM OrderItems WHERE NAME="
                                    + "'" + mainCourseToAdd.getName() + "' AND ORDER_ID=" + orderID;

                            Cursor nrItemsCursor = database.rawQuery(getNrItemsSQL,null);
                            nrItemsCursor.moveToFirst();

                            int nrItems = nrItemsCursor.getInt(0);
                            nrItems = nrItems + 1;

                            nrItemsCursor.close();

                            String updateSQL = "UPDATE OrderItems SET NR_ITEMS=" + nrItems + " WHERE NAME="
                                    + "'" + mainCourseToAdd.getName() + "' AND ORDER_ID=" + orderID;

                            database.execSQL(updateSQL);
                        } else {
                            String insertSQL = "INSERT INTO OrderItems (NAME, PRICE, NR_ITEMS, ORDER_ID) VALUES('"+ mainCourseToAdd.getName()
                                    + "'," + mainCourseToAdd.getPrice() + ",1,"+ orderID +")";
                            database.execSQL(insertSQL);
                        }
                        break;
                    case 3:
                        Deserts desertToAdd = deserts.get(itemPosition-1);
                        addedItems.add(desertToAdd);

                        database.execSQL("CREATE TABLE IF NOT EXISTS OrderItems(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "NAME TEXT NOT NULL, PRICE REAL NOT NULL, NR_ITEMS INTEGER NOT NULL, ORDER_ID INTEGER NOT NULL, FOREIGN KEY(ORDER_ID) REFERENCES Orders(ID))");

                        if(checkIfRecordExists(database,desertToAdd.getName(),orderID)){
                            String getNrItemsSQL = "SELECT NR_ITEMS FROM OrderItems WHERE NAME="
                                    + "'" + desertToAdd.getName() + "' AND ORDER_ID=" + orderID;

                            Cursor nrItemsCursor = database.rawQuery(getNrItemsSQL,null);
                            nrItemsCursor.moveToFirst();

                            int nrItems = nrItemsCursor.getInt(0);
                            nrItems = nrItems + 1;

                            nrItemsCursor.close();

                            String updateSQL = "UPDATE OrderItems SET NR_ITEMS=" + nrItems + " WHERE NAME="
                                    + "'" + desertToAdd.getName() + "' AND ORDER_ID=" + orderID;

                            database.execSQL(updateSQL);
                        } else {
                            String insertSQL = "INSERT INTO OrderItems (NAME, PRICE, NR_ITEMS, ORDER_ID) VALUES('"+ desertToAdd.getName()
                                    + "'," + desertToAdd.getPrice() + ",1,"+ orderID +")";
                            database.execSQL(insertSQL);
                        }
                        break;
                    case 4:
                        Drink drinkToAdd = drinks.get(itemPosition-1);
                        addedItems.add(drinkToAdd);

                        database.execSQL("CREATE TABLE IF NOT EXISTS OrderItems(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "NAME TEXT NOT NULL, PRICE REAL NOT NULL, NR_ITEMS INTEGER NOT NULL, ORDER_ID INTEGER NOT NULL, FOREIGN KEY(ORDER_ID) REFERENCES Orders(ID))");

                        if(checkIfRecordExists(database,drinkToAdd.getName(),orderID)){
                            String getNrItemsSQL = "SELECT NR_ITEMS FROM OrderItems WHERE NAME="
                                    + "'" + drinkToAdd.getName() + "' AND ORDER_ID=" + orderID;

                            Cursor nrItemsCursor = database.rawQuery(getNrItemsSQL,null);
                            nrItemsCursor.moveToFirst();

                            int nrItems = nrItemsCursor.getInt(0);
                            nrItems = nrItems + 1;

                            nrItemsCursor.close();

                            String updateSQL = "UPDATE OrderItems SET NR_ITEMS=" + nrItems + " WHERE NAME="
                                    + "'" + drinkToAdd.getName() + "' AND ORDER_ID=" + orderID;

                            database.execSQL(updateSQL);
                        } else {
                            String insertSQL = "INSERT INTO OrderItems (NAME, PRICE, NR_ITEMS, ORDER_ID) VALUES('"+ drinkToAdd.getName()
                                    + "'," + drinkToAdd.getPrice() + ",1,"+ orderID +")";
                            database.execSQL(insertSQL);
                        }
                        break;
                }

                Button undoBtn = findViewById(R.id.deletePreviousBtn);
                if(undoBtn.getVisibility() == View.INVISIBLE){
                    undoBtn.setVisibility(View.VISIBLE);
                }

                Button finishBtn = findViewById(R.id.finishOrderBtn);
                finishBtn.setVisibility(View.VISIBLE);
            }
        });

        final Button undoPreviousBtn = findViewById(R.id.deletePreviousBtn);
        undoPreviousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Orderable item = addedItems.get(addedItems.size()-1);
                String name = item.getName();

                String numberOfItemSQL = "SELECT NR_ITEMS FROM OrderItems WHERE NAME="
                        + "'" + name + "' AND ORDER_ID=" + orderID;

                Cursor nrItemsCursor = database.rawQuery(numberOfItemSQL,null);
                nrItemsCursor.moveToFirst();

                int nrItems = nrItemsCursor.getInt(0);

                if(nrItems>1){
                    nrItems = nrItems-1;
                    String updateSQL = "UPDATE OrderItems SET NR_ITEMS=" + nrItems + " WHERE NAME="
                            + "'" + name + "' AND ORDER_ID=" + orderID;
                    database.execSQL(updateSQL);
                }else{
                    String deleteSQL = "DELETE FROM OrderItems WHERE NAME="
                            + "'" + name + "' AND ORDER_ID=" + orderID;
                    database.execSQL(deleteSQL);
                }

                addedItems.remove(addedItems.size()-1);

                if(addedItems.size()<=0){
                    undoPreviousBtn.setVisibility(View.INVISIBLE);
                    Button finishBtn = findViewById(R.id.finishOrderBtn);
                    finishBtn.setVisibility(View.INVISIBLE);
                }
            }
        });

        Button finishOrderBtn = findViewById(R.id.finishOrderBtn);
        finishOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

        Button discardBtn = findViewById(R.id.discardOrderBtn);
        discardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                while(addedItems.size()>0){
                    Orderable item = addedItems.get(addedItems.size()-1);
                    String name = item.getName();

                    String deleteSQL = "DELETE FROM OrderItems WHERE NAME="
                            + "'" + name + "' AND ORDER_ID=" + orderID;
                    database.execSQL(deleteSQL);
                    addedItems.remove(addedItems.size()-1);
                }

                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
    }

    public ArrayList<Orderable> downloadItemsFromFirebase(final ItemType itemType){
        final ArrayList<Orderable> returnList = new ArrayList<Orderable>();
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    final FirebaseUser user = mAuth.getCurrentUser();

                    firebaseDB = FirebaseDatabase.getInstance().getReference();
                    firebaseDB.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.child("Users").hasChild(myUID)){
                                DataSnapshot typeSnapshot = dataSnapshot.child("Users")
                                        .child(myUID).child("Menu").child(itemType.toString());

                                for(DataSnapshot itemSnapshot: typeSnapshot.getChildren()){
                                    String itemName = itemSnapshot.getKey();
                                    double price = Double.parseDouble(itemSnapshot.child("Price").getValue(String.class));

                                    switch(itemType){
                                        case STARTER_DISH:
                                            Starter starter = new Starter(itemName,price,false);
                                            returnList.add(starter);
                                            break;
                                        case MAIN_COURSE:
                                            MainCourse mainCourse = new MainCourse(itemName,price,0);
                                            returnList.add(mainCourse);
                                            break;
                                        case DESERT:
                                            Deserts desert = new Deserts(price,itemName,false);
                                            returnList.add(desert);
                                            break;
                                        case DRINKS:
                                            Drink drink = new Drink(price,itemName);
                                            returnList.add(drink);
                                            break;
                                    }
                                }



                            }else{
                                AddOrderContinueActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        return returnList;
    }

    public void loadToSpinner(ArrayList<String> itemNames, Spinner itemSpinner){
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, itemNames);
        itemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemSpinner.setAdapter(itemAdapter);
    }

    public boolean checkIfRecordExists(SQLiteDatabase database, String productName, int orderID){
        String query = "SELECT * FROM OrderItems WHERE NAME='" + productName + "' AND order_ID=" + orderID;
        Cursor cursor = database.rawQuery(query,null);

        if(cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }

        cursor.close();
        return true;
    }
}
