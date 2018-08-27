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
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.air_book.ordermanagerproject.R;

import java.util.ArrayList;

/**
 * Created by air_book on 12/4/17.
 */

public class MainCourseAdapter extends ArrayAdapter<MainCourse> {

    Context context;
    int layoutResourceId;
    ArrayList<MainCourse> mainCourses;
    ArrayList<Bitmap> images;

    public MainCourseAdapter(Context context, int layoutResourceId, ArrayList<MainCourse> mainCourses, ArrayList<Bitmap> images){
        super(context, layoutResourceId, mainCourses);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.mainCourses = mainCourses;
        this.images = images;
    }

    public static class CourseHolder{
        ImageView coursePhoto;
        TextView courseNameTV;
        TextView coursePriceTV;
        RatingBar courseRB;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        MainCourseAdapter.CourseHolder courseHolder = null;

        if(row==null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            courseHolder = new MainCourseAdapter.CourseHolder();
            courseHolder.coursePhoto = (ImageView)row.findViewById(R.id.mainCourseLVImg);
            courseHolder.courseNameTV = (TextView)row.findViewById(R.id.mainCourseLVName);
            courseHolder.coursePriceTV = (TextView)row.findViewById(R.id.mainCourseLVPrice);
            courseHolder.courseRB = (RatingBar) row.findViewById(R.id.mainCourseLVRB);

            row.setTag(courseHolder);
        } else {
            courseHolder = (MainCourseAdapter.CourseHolder)row.getTag();
        }

        MainCourse mainCourse = mainCourses.get(position);
        if(images.get(position)!=null){
            courseHolder.coursePhoto.setImageBitmap(images.get(position));
        } else {
            courseHolder.coursePhoto.setImageResource(R.drawable.common_google_signin_btn_text_disabled);
            //Add a custom photo to show an X as no image
        }

        courseHolder.courseNameTV.setText(mainCourse.getName());
        courseHolder.coursePriceTV.setText(String.valueOf(mainCourse.getPrice()));
        if(mainCourse.getSpiceLevel()>10){
            courseHolder.courseRB.setRating(5);
        }else{
            courseHolder.courseRB.setRating(mainCourse.getSpiceLevel()/2);
        }

        return row;
    }
}
