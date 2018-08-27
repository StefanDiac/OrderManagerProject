package com.example.air_book.ordermanagerproject.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.air_book.ordermanagerproject.Model.Table;
import com.example.air_book.ordermanagerproject.Model.TableAdapter;
import com.example.air_book.ordermanagerproject.R;

import java.util.ArrayList;

public class ViewTablesActivity extends AppCompatActivity {

    ArrayList<Table> tables;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tables);

        ListView listView = (ListView)findViewById(R.id.tablesLV);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        tables = bundle.getParcelableArrayList("tables");

        TableAdapter adapter = new TableAdapter(this,R.layout.listview_item_row_tables,tables);
        View header = (View)getLayoutInflater().inflate(R.layout.listview_header_row_tables, null);
        listView.addHeaderView(header);
        listView.setAdapter(adapter);

        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Remove item
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
