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

public class DrinkAdapter extends ArrayAdapter<Drink>{
    Context context;
    int layoutResourceId;
    ArrayList<Drink> drinks;
    ArrayList<Bitmap> images;

    public DrinkAdapter(Context context, int layoutResourceId, ArrayList<Drink> drinks, ArrayList<Bitmap> images){
        super(context,layoutResourceId,drinks);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.drinks = drinks;
        this.images = images;
    }

    public static class DrinkHolder{
        ImageView drinkImage;
        TextView drinkNameTV;
        TextView drinkPriceTV;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        DrinkAdapter.DrinkHolder holder = null;

        if(row==null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new DrinkAdapter.DrinkHolder();
            holder.drinkImage = (ImageView) row.findViewById(R.id.drinkLVImg);
            holder.drinkNameTV = (TextView)row.findViewById(R.id.drinkLVName);
            holder.drinkPriceTV = (TextView)row.findViewById(R.id.drinkLVPrice);

            row.setTag(holder);
        }else{
            holder = (DrinkAdapter.DrinkHolder)row.getTag();
        }

        Drink drink = drinks.get(position);

        if(images.get(position)!=null){
            holder.drinkImage.setImageBitmap(images.get(position));
        } else {
            holder.drinkImage.setImageResource(R.drawable.common_google_signin_btn_text_disabled);
            //Add a custom photo to show an X as no image
        }
        holder.drinkNameTV.setText(drink.getName());
        holder.drinkPriceTV.setText(String.valueOf(drink.getPrice()));

        return row;
    }
}
