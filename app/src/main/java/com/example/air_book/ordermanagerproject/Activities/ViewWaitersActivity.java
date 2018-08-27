package com.example.air_book.ordermanagerproject.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.air_book.ordermanagerproject.Model.Waiter;
import com.example.air_book.ordermanagerproject.Model.WaiterAdapter;
import com.example.air_book.ordermanagerproject.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ViewWaitersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_waiters);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String UID = bundle.getString("UID");

        WaiterDownloader waiterDownloader = new WaiterDownloader();
        waiterDownloader.UID = UID;
        waiterDownloader.waiters = new ArrayList<>();
        waiterDownloader.execute();


    }


    private class WaiterDownloader extends AsyncTask <Void,Integer,Void> {

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
                            publishProgress(100);
                        } else if (responseWaiter.equals("2")) {
                            showError();
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
                showError();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            (findViewById(R.id.getWaitersProgress)).setVisibility(View.INVISIBLE);

            ListView listView = (ListView)findViewById(R.id.waitersLV);

            WaiterAdapter waiterAdapter = new WaiterAdapter(ViewWaitersActivity.this,R.layout.listview_item_row_waiters,waiters);

            View header = (View)getLayoutInflater().inflate(R.layout.listview_header_row_waiters, null);
            listView.addHeaderView(header);
            listView.setAdapter(waiterAdapter);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            ((ProgressBar)findViewById(R.id.getWaitersProgress)).setProgress(values[0]);
        }

    }

    private void showError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ViewWaitersActivity.this);
                builder.setCancelable(false);
                builder.setTitle("Error downloading data !");
                builder.setMessage("Something went wrong while downloading the data !");
                builder.setPositiveButton("Understood", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

}
