package com.example.air_book.ordermanagerproject.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.example.air_book.ordermanagerproject.Model.Waiter;
import com.example.air_book.ordermanagerproject.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;

public class AddWaiterActivity extends AppCompatActivity {

    Waiter waiterToAdd;
    Intent intent;
    Calendar calendar = Calendar.getInstance();
    int startYear = calendar.get(Calendar.YEAR);
    int startMonth = calendar.get(Calendar.MONTH);
    int startDay = calendar.get(Calendar.DAY_OF_MONTH);
    boolean hasFirstName = false;
    boolean hasLastName = false;
    boolean hasCNP = false;
    boolean hasAgeSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_waiter);
        waiterToAdd = new Waiter();
        intent = getIntent();

        (findViewById(R.id.firstNameTB)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText editText = (EditText) v;

                if(!hasFocus) {
                    if(editText.getText().toString().length() != 0) {
                        waiterToAdd.setFirstName(editText.getText().toString());
                        hasFirstName = true;
                    }
                }
            }
        });


        (findViewById(R.id.lastNameTB)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText editText = (EditText) v;

                if(!hasFocus) {
                    if(editText.getText().toString().length() != 0) {
                        waiterToAdd.setLastName(editText.getText().toString());
                        hasLastName = true;
                    }
                }
            }
        });


        (findViewById(R.id.salaryTB)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText editText = (EditText) v;

                if(!hasFocus) {
                    try {
                        waiterToAdd.setSalary(Double.parseDouble(editText.getText().toString()));
                    }catch (Exception e){
                        waiterToAdd.setSalary(0.0);
                    }
                }
            }
        });

        ((Switch)findViewById(R.id.isPFASwitch)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    waiterToAdd.setPFA(true);
                } else {
                    waiterToAdd.setPFA(false);
                }
            }
        });

        (findViewById(R.id.setBirthDaybtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener datePickerL = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        int age = startYear - year;

                        if(month > startMonth) {
                            age--;
                        } else if (dayOfMonth > startDay) {
                            age--;
                        }

                        waiterToAdd.setAge(age);
                        hasAgeSet = true;
                    }
                };

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddWaiterActivity.this, datePickerL, startYear, startMonth, startDay);
                datePickerDialog.show();
            }
        });

        (findViewById(R.id.CNPEditBox)).setOnFocusChangeListener(new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText editText = (EditText)v;

                if(!hasFocus) {
                    waiterToAdd.setCNP(editText.getText().toString());
                }
            }
        });

        ((EditText)findViewById(R.id.CNPEditBox)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId==EditorInfo.IME_ACTION_DONE) {
                    if(v.getText().length()>0) {
                        waiterToAdd.setCNP(v.getText().toString());
                        hasCNP = true;
                    }
                }
                return false;
            }
        });
    }

    public void showError(int errorCode) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(AddWaiterActivity.this);
        builder.setCancelable(false);
        if (errorCode == 1) {
            builder.setTitle("Unable to create a new waiter");
            builder.setMessage("Some fields are still empty !");
        } else if (errorCode == 2) {
            builder.setTitle("Waiter already exists !");
            builder.setMessage("A waiter with the same CNP already exists");
        } else if(errorCode == 3) {
            builder.setTitle("Could not connect to database !");
            builder.setMessage("Unable to establish a link to the database. " +
                    "Please check if the servers are online");
        }
        builder.setPositiveButton("Understood", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public void onCreateWaiterBtnPressed(View view) {

        if(!hasCNP || !hasAgeSet || !hasLastName || !hasFirstName) {
            showError(1);
        } else {
            Bundle bundle = intent.getExtras();
            String UUID = bundle.getString("UID");
            AsyncCaller asyncCaller = new AsyncCaller();
            asyncCaller.message = "2," + UUID + "," + waiterToAdd.getFirstName() + "," + waiterToAdd.getLastName() + "," +
                    waiterToAdd.getSalary() + "," + waiterToAdd.getAge() + "," +
                    ((waiterToAdd.getPFA()) ? "0" : "1") + "," + waiterToAdd.getCNP();
            asyncCaller.execute();
        }
    }

    private class AsyncCaller extends AsyncTask<Void,Void,Void> {
        String message;
        String response;

        @Override
        protected Void doInBackground(Void... params) {
            try(Socket socket = new Socket("10.0.2.2",5050)){
                BufferedReader receiver = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter sendData = new PrintWriter(socket.getOutputStream(), true);

                sendData.println(message);
                this.response = receiver.readLine();
            }catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(response.equals("1")) {
                showError(2);
            } else if (response.equals("0")){
                finish();
            } else {
                showError(3);
            }
        }
    }

}
