package com.example.air_book.ordermanagerproject.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.air_book.ordermanagerproject.Model.Table;
import com.example.air_book.ordermanagerproject.R;

import java.util.ArrayList;

public class AddTableActivity extends AppCompatActivity {

    Table tableToAdd;
    Intent intent;
    boolean hasNumber = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_table);

        intent = getIntent();
        tableToAdd = new Table();


        ((SeekBar)findViewById(R.id.tableSeatsSeek)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int numberOfSeats = seekBar.getProgress();
                tableToAdd.setNumberOfSeats(numberOfSeats);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ((RadioButton)findViewById(R.id.smokingRB)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    tableToAdd.setSmokable(true);
                } else {
                    tableToAdd.setSmokable(false);
                }
            }
        });

        ((EditText)findViewById(R.id.tableNumerTB)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText editText = (EditText)v;
                if(editText.getText().length()>0) {
                    try{
                        tableToAdd.setNumber(Integer.parseInt(editText.getText().toString()));
                        hasFocus = true;
                        if (tableToAdd.getNumber()<=0) {
                            hasFocus = false;
                        }
                    }catch (Exception e) {
                        tableToAdd.setNumber(0);
                    }
                }
            }
        });

        ((EditText)findViewById(R.id.tableNumerTB)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    if(v.getText().length()>0) {
                        try{
                            tableToAdd.setNumber(Integer.parseInt(v.getText().toString()));
                            hasNumber = true;
                            if (tableToAdd.getNumber()<=0) {
                                hasNumber = false;
                            }
                            return hasNumber;
                        }catch (Exception e) {
                            tableToAdd.setNumber(0);
                        }
                    }
                }
                return false;
            }
        });

    }

    public void showError(int errorType) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AddTableActivity.this);
        if (errorType == 2) {
            alertBuilder.setTitle("No Table Number Has Been Set !");
            alertBuilder.setMessage("A Valid table number must be set in order to create one.");
        } else {
            alertBuilder.setTitle("Duplicate Key Was Found");
            alertBuilder.setMessage("The key " + tableToAdd.getNumber() + " has already been used for a table ! Check the other tables" +
            " in the list of tables, by pressing the View Tables Button in the Manager Menu !");
        }
        alertBuilder.setPositiveButton("Understood", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertBuilder.setCancelable(false);
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    public void onCreateTableBtnPressed(View view) {

        if (hasNumber) {
            Bundle bundle = intent.getExtras();
            ArrayList<Table> tables = bundle.getParcelableArrayList("tables");
            boolean dupe = false;
            for (Table table : tables) {
                if (table.getNumber() == tableToAdd.getNumber()) {
                    showError(1);
                    dupe = true;
                }
            }
            if (!dupe) {
                tables.add(tableToAdd);
                intent.putExtra("tables", tables);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        } else {
            showError(2);
        }
    }
}
