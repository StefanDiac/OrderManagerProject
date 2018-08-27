package com.example.air_book.ordermanagerproject.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.air_book.ordermanagerproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

import static android.provider.Settings.Secure.ANDROID_ID;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String myUID = PreferenceManager.getDefaultSharedPreferences(this).getString("UID", "");

        if (myUID != "") {
            goToMainView(myUID);
        }
    }

    public void onNewUserClicked(View view) {
        findViewById(R.id.existingUserBtn).setVisibility(View.INVISIBLE);
        ((Button) findViewById(R.id.newUserBtn)).setText("Creating New User...");
        findViewById(R.id.newUserBtn).setEnabled(false);
        displayFields(false);
    }

    public void onLinkUserClicked(View view) {
        findViewById(R.id.newUserBtn).setVisibility(View.INVISIBLE);
        ((Button) findViewById(R.id.existingUserBtn)).setText("Linking...");
        findViewById(R.id.existingUserBtn).setEnabled(false);
        displayFields(true);
    }

    public void displayFields(boolean link) {
        if (!link) {
            findViewById(R.id.companyNameCreateTB).setVisibility(View.VISIBLE);
            findViewById(R.id.cancelBtn).setVisibility(View.VISIBLE);
            findViewById(R.id.submitBtn).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.linkIDTB).setVisibility(View.VISIBLE);
            findViewById(R.id.cancelBtn).setVisibility(View.VISIBLE);
            findViewById(R.id.submitBtn).setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onCancelBtnPressed(View view) {
        if (findViewById(R.id.newUserBtn).getVisibility() == View.VISIBLE) {
            ((Button) findViewById(R.id.newUserBtn)).setText("New User");
            findViewById(R.id.newUserBtn).setEnabled(true);
            ((EditText) findViewById(R.id.companyNameCreateTB)).setText("");
            findViewById(R.id.companyNameCreateTB).setVisibility(View.INVISIBLE);
            findViewById(R.id.cancelBtn).setVisibility(View.INVISIBLE);
            findViewById(R.id.submitBtn).setVisibility(View.INVISIBLE);
            findViewById(R.id.existingUserBtn).setVisibility(View.VISIBLE);
        } else {
            ((Button) findViewById(R.id.existingUserBtn)).setText("Link To Existing User");
            findViewById(R.id.existingUserBtn).setEnabled(true);
            ((EditText) findViewById(R.id.linkIDTB)).setText("");
            findViewById(R.id.linkIDTB).setVisibility(View.INVISIBLE);
            findViewById(R.id.cancelBtn).setVisibility(View.INVISIBLE);
            findViewById(R.id.submitBtn).setVisibility(View.INVISIBLE);
            findViewById(R.id.newUserBtn).setVisibility(View.VISIBLE);
        }
    }

    public void onSubmitBtnPressed(View view) {
            if (findViewById(R.id.newUserBtn).getVisibility() == View.VISIBLE) {
                String uniqueID = UUID.randomUUID().toString();
                String dataToSend = "0," + uniqueID + "," + ((EditText) findViewById(R.id.companyNameCreateTB)).getText();
                AsyncCaller asyncCaller = new AsyncCaller();
                asyncCaller.message = dataToSend;
                asyncCaller.type = 0;
                asyncCaller.uid = uniqueID;
                asyncCaller.execute();
            } else {
                String uid = ((EditText) findViewById(R.id.linkIDTB)).getText().toString();
                String dataToSend = "1," + uid;

                AsyncCaller asyncCaller = new AsyncCaller();
                asyncCaller.message = dataToSend;
                asyncCaller.type = 1;
                asyncCaller.uid = uid;
                asyncCaller.execute();
            }
    }

    public void goToMainView(String UID) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("UID", UID);
        startActivity(intent);
    }

    public void saveUID(String UID) {
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("UID", UID).apply();
    }

    private class AsyncCaller extends AsyncTask<Void, Void, Void> {

        public String message;
        public String response;
        public String uid;
        public int type;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try(Socket socket = new Socket("10.0.2.2",5050)) {
                BufferedReader receiver = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter sendData = new PrintWriter(socket.getOutputStream(), true);

                sendData.println(message);
                this.response = receiver.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            if(type==0) {
                if (Integer.parseInt(response) == 1) {
                    saveUID(uid);
                    goToMainView(uid);
                } else if (Integer.parseInt(response) == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setCancelable(true);
                    builder.setTitle("Same company found !");
                    builder.setMessage("There is already a company with the same unique id, data for it will be used instead");
                    builder.setPositiveButton("Understood", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            saveUID(uid);
                            goToMainView(uid);
                        }
                    });
                    builder.show();
                }
            } else {
                if (Integer.parseInt(response) == 1) {
                    saveUID(uid);
                    goToMainView(uid);
                } else if (Integer.parseInt(response) == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setCancelable(true);
                    builder.setTitle("Invalid Link ID");
                    builder.setMessage("No Instance Of The Given ID was found.");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                }
            }
        }

    }
}
