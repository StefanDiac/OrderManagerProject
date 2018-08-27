package com.example.air_book.ordermanagerproject.Model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.air_book.ordermanagerproject.R;

import java.util.ArrayList;

/**
 * Created by air_book on 12/4/17.
 */

public class DesertAdapter extends ArrayAdapter<Deserts> {

    Context context;
    int layoutResourceId;
    ArrayList<Deserts> deserts;
    ArrayList<Bitmap> images;

    public DesertAdapter(Context context, int layoutResourceId, ArrayList<Deserts> deserts, ArrayList<Bitmap> images) {
        super(context,layoutResourceId,deserts);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.deserts = deserts;
        this.images = images;
    }

    public static class DesertHolder{
        ImageView desertImage;
        TextView desertNameTV;
        TextView desertPriceTV;
        CheckBox desertStoreBoughtCB;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        DesertAdapter.DesertHolder holder = null;

        if(row==null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new DesertAdapter.DesertHolder();
            holder.desertImage = (ImageView) row.findViewById(R.id.desertLVImg);
            holder.desertNameTV = (TextView)row.findViewById(R.id.desertLVName);
            holder.desertPriceTV = (TextView)row.findViewById(R.id.desertLVPrice);
            holder.desertStoreBoughtCB = (CheckBox)row.findViewById(R.id.desertLVCB);

            row.setTag(holder);
        } else {
            holder = (DesertAdapter.DesertHolder)row.getTag();
        }

        Deserts desert = deserts.get(position);

        if(images.get(position)!=null){
            holder.desertImage.setImageBitmap(images.get(position));
        } else {
            holder.desertImage.setImageResource(R.drawable.common_google_signin_btn_text_disabled);
            //Add a custom photo to show an X as no image
        }
        holder.desertNameTV.setText(desert.getName());
        holder.desertPriceTV.setText(String.valueOf(desert.getPrice()));
        if(desert.isBought()){
            holder.desertStoreBoughtCB.setChecked(true);
        }else{
            holder.desertStoreBoughtCB.setChecked(false);
        }


        return row;
    }
}
