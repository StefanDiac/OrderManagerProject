package com.example.air_book.ordermanagerproject.Model;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.air_book.ordermanagerproject.R;

import java.util.ArrayList;


/**
 * Created by air_book on 10/30/17.
 */

public class WaiterAdapter extends ArrayAdapter<Waiter> {
    Context context;
    int layoutResourceId;
    ArrayList<Waiter> waiters;

    public WaiterAdapter(Context context, int layoutResourceId, ArrayList<Waiter> waiters) {
        super(context, layoutResourceId, waiters);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.waiters = waiters;
    }

    public static class ViewHolder {
        public TextView firstNameTV;
        public TextView lastNameTV;
        public TextView salaryTV;
        public TextView pfaTV;
        public TextView ageTV;
        public TextView CNPTV;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;

        ViewHolder holder = null;

        if(row==null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ViewHolder();
            holder.firstNameTV = (TextView)row.findViewById(R.id.firstNameTV);
            holder.lastNameTV = (TextView)row.findViewById(R.id.lastNameTV);
            holder.salaryTV = (TextView)row.findViewById(R.id.salaryTV);
            holder.ageTV = (TextView)row.findViewById(R.id.ageTV);
            holder.pfaTV = (TextView)row.findViewById(R.id.pfaTV);
            holder.CNPTV = row.findViewById(R.id.CNPTV);

            row.setTag(holder);

        } else {
            holder = (ViewHolder)row.getTag();
        }

        Waiter waiter = waiters.get(position);
        holder.firstNameTV.setText(waiter.getFirstName());
        holder.lastNameTV.setText(waiter.getLastName());
        holder.ageTV.setText(String.valueOf(waiter.getAge()));
        holder.salaryTV.setText(String.valueOf(waiter.getSalary()));
        if(waiter.getPFA()) {
            holder.pfaTV.setText("Legal Person");
        } else {
            holder.pfaTV.setText("Individual");
        }
        holder.CNPTV.setText(waiter.getCNP());
        return row;
    }
}
