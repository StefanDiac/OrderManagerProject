package com.example.air_book.ordermanagerproject.Model;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.air_book.ordermanagerproject.R;

import java.util.ArrayList;

/**
 * Created by air_book on 10/31/17.
 */

public class TableAdapter extends ArrayAdapter<Table> {

    Context context;
    int layoutResourceId;
    ArrayList<Table> tables;

    public TableAdapter(Context context, int layoutResourceId, ArrayList<Table> tables) {
        super(context, layoutResourceId, tables);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.tables = tables;
    }

    public static class TableHolder{
        TextView tableNumberTV;
        TextView smokingTV;
        ImageView numberOfSeatsImg;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        TableHolder holder = null;

        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new TableHolder();
            holder.numberOfSeatsImg = (ImageView)row.findViewById(R.id.tableSeatsImg);
            holder.smokingTV = (TextView)row.findViewById(R.id.smokingTV);
            holder.tableNumberTV = (TextView)row.findViewById(R.id.tableNumberTV);

            row.setTag(holder);
        } else {
            holder = (TableHolder)row.getTag();
        }

        Table table = tables.get(position);
        holder.tableNumberTV.setText(String.valueOf(table.getNumber()));

        if(table.isSmokable()) {
            holder.smokingTV.setText("Smoking");
        } else {
            holder.smokingTV.setText("Non Smoking");
        }

        int nrSeats = table.getNumberOfSeats();

        if(nrSeats <=2) {
            holder.numberOfSeatsImg.setImageResource(R.drawable.x2);
        } else if (nrSeats <=4) {
            holder.numberOfSeatsImg.setImageResource(R.drawable.x4);
        } else if (nrSeats <=6) {
            holder.numberOfSeatsImg.setImageResource(R.drawable.x6);
        } else if (nrSeats <=8) {
            holder.numberOfSeatsImg.setImageResource(R.drawable.x8);
        } else {
            holder.numberOfSeatsImg.setImageResource(R.drawable.more8);
        }

        return row;
    }
}
