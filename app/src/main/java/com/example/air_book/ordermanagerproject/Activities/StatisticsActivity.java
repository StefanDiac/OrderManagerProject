package com.example.air_book.ordermanagerproject.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.air_book.ordermanagerproject.Manifest;
import com.example.air_book.ordermanagerproject.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class StatisticsActivity extends AppCompatActivity {

    SQLiteDatabase myDB;
    static final int MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        myDB = openOrCreateDatabase("orders",MODE_PRIVATE,null);

        Button generateSalesRep = findViewById(R.id.salesBtn);
        Button generateTopItemsRep = findViewById(R.id.topItemsBtn);

        generateSalesRep.setVisibility(View.INVISIBLE);
        generateTopItemsRep.setVisibility(View.INVISIBLE);

        Button barChartBtn = findViewById(R.id.barchartBtn);

        if(ContextCompat.checkSelfPermission(StatisticsActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(StatisticsActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
        }else{
            generateSalesRep.setVisibility(View.VISIBLE);
            generateTopItemsRep.setVisibility(View.VISIBLE);
        }

        generateSalesRep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = myDB.rawQuery("SELECT * FROM OrderItems",null);
                double total = 0;
                int itemsSold = 0;

                try{
                    while(cursor.moveToNext()){
                        double price = cursor.getDouble(2);
                        int nrOfItems = cursor.getInt(3);

                        total = total + price * nrOfItems;
                        itemsSold = itemsSold + nrOfItems;
                    }
                }finally {
                    cursor.close();
                }

                String root = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();

                File reportDir = new File(root + "/restaurant_report");
                reportDir.mkdirs();

                String fileName = "salesReport.txt";
                File file = new File(reportDir,fileName);

                if(file.exists()){
                    file.delete();
                }

                try(PrintWriter p = new PrintWriter(new FileOutputStream(file))){
                    p.println("Generate User Report For Restaurant - \n Total From Sales - " + total + "\n Total items sold - " + itemsSold);
                    Toast.makeText(getApplicationContext(),"Saved the sales report on the SD card !",Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        generateTopItemsRep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Cursor cursor = myDB.rawQuery("SELECT * FROM OrderItems",null);

                class ItemPair {
                    Integer numberOfItems;
                    String name;

                    public ItemPair(String name, Integer numberOfItems){
                        this.name = name;
                        this.numberOfItems = numberOfItems;
                    }

                    public Integer getNumberOfItems() {
                        return numberOfItems;
                    }

                    public void setNumberOfItems(Integer numberOfItems) {
                        this.numberOfItems = numberOfItems;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }
                }

                ArrayList<ItemPair> itemPairs = new ArrayList<ItemPair>();

                try{
                    while(cursor.moveToNext()){
                        String name = cursor.getString(1);
                        int numberOfItems = cursor.getInt(3);

                        ItemPair itemPair = new ItemPair(name,numberOfItems);

                        if(itemPairs.size()<=0){
                         itemPairs.add(itemPair);
                        }else{
                            boolean dupe = false;
                            for(ItemPair itemPairCheck : itemPairs){
                                if(itemPairCheck.getName().equals(itemPair.getName())){
                                    itemPairCheck.setNumberOfItems(itemPairCheck.getNumberOfItems() + itemPair.getNumberOfItems());
                                    dupe = true;
                                }
                            }

                            if(!dupe){
                                itemPairs.add(itemPair);
                            }
                        }
                    }
                }finally {
                    cursor.close();
                }

                class itemPairComparer implements Comparator<ItemPair> {
                    @Override
                    public int compare(ItemPair o1, ItemPair o2) {
                        return o1.getNumberOfItems().compareTo(o2.getNumberOfItems());
                    }
                }

                Collections.sort(itemPairs,new itemPairComparer());
                Collections.reverse(itemPairs);
                ArrayList<String> topItems = new ArrayList<String>();

                for(ItemPair itemPair: itemPairs){
                    topItems.add("" + itemPair.getName() + " - " + itemPair.getNumberOfItems());
                }

                String root = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();

                File reportDir = new File(root + "/restaurant_reports");
                reportDir.mkdirs();

                String fileName = "topItemsReport.txt";
                File file = new File(reportDir,fileName);

                if(file.exists()){
                    file.delete();
                }

                try(PrintWriter p = new PrintWriter(new FileOutputStream(file))){
                    p.println(topItems.toString());
                    Toast.makeText(getApplicationContext(),"Saved the top items report on your SD card !",Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        barChartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BarChartActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    findViewById(R.id.topItemsBtn).setVisibility(View.VISIBLE);
                    findViewById(R.id.salesBtn).setVisibility(View.VISIBLE);
                } else {
                    //show an error?
                }
                return;
            }
        }
    }
}
