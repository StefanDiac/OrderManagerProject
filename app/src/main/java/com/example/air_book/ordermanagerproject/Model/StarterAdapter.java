package com.example.air_book.ordermanagerproject.Model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
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

public class StarterAdapter extends ArrayAdapter<Starter> {
    Context context;
    int layoutResourceId;
    ArrayList<Starter> starters;
    ArrayList<Bitmap> images;

    public StarterAdapter(Context context, int layoutResourceId, ArrayList<Starter> starters, ArrayList<Bitmap> images){
        super(context, layoutResourceId, starters);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.starters = starters;
        this.images = images;
    }

    public static class StarterHolder{
        ImageView starterPhoto;
        TextView starterNameTV;
        TextView starterPriceTV;
        CheckBox starterColdCB;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        StarterHolder starterHolder = null;

        if(row==null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            starterHolder = new StarterHolder();
            starterHolder.starterPhoto = (ImageView)row.findViewById(R.id.starterLVImg);
            starterHolder.starterNameTV = (TextView)row.findViewById(R.id.starterLVName);
            starterHolder.starterPriceTV = (TextView)row.findViewById(R.id.starterLVPrice);
            starterHolder.starterColdCB = (CheckBox)row.findViewById(R.id.starterLVCB);

            row.setTag(starterHolder);
        } else {
            starterHolder = (StarterHolder)row.getTag();
        }

        Starter starter = starters.get(position);
        if(images.get(position)!=null){
            starterHolder.starterPhoto.setImageBitmap(images.get(position));
        } else {
            starterHolder.starterPhoto.setImageResource(R.drawable.common_google_signin_btn_text_disabled);
            //Add a custom photo to show an X as no image
        }

        starterHolder.starterNameTV.setText(starter.getName());
        starterHolder.starterPriceTV.setText(String.valueOf(starter.getPrice()));

        if(starter.isCold()){
            starterHolder.starterColdCB.setChecked(true);
        }else {
            starterHolder.starterColdCB.setChecked(false);
        }

        return row;
    }
}
