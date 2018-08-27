package com.example.air_book.ordermanagerproject.Model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

/**
 * Created by air_book on 10/29/17.
 */

public class MainCourse implements Orderable, Parcelable {

    private String name;
    private double price;
    private final ItemType itemType = ItemType.MAIN_COURSE;
    private int spiceLevel;

    public MainCourse(String name, double price, int spiceLevel) {
        this.name = name;
        this.price = price;
        if(spiceLevel >= 10) {
            this.spiceLevel = 10;
        } else {
            this.spiceLevel = spiceLevel;
        }
    }

    protected MainCourse(Parcel in) {
        name = in.readString();
        price = in.readDouble();
        spiceLevel = in.readInt();
    }

    public static final Creator<MainCourse> CREATOR = new Creator<MainCourse>() {
        @Override
        public MainCourse createFromParcel(Parcel in) {
            return new MainCourse(in);
        }

        @Override
        public MainCourse[] newArray(int size) {
            return new MainCourse[size];
        }
    };

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSpiceLevel() {
        return spiceLevel;
    }

    public void setSpiceLevel(int spiceLevel) {
        this.spiceLevel = spiceLevel;
    }

    @Override
    public double computePrice() {
        return VAT*getPrice();
    }

    @Override
    public ItemType getItemType() {
        return itemType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(price);
        dest.writeInt(spiceLevel);
    }

    public HashMap<String,String> getCourseValues(){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("Price",Double.toString(getPrice()));
        hashMap.put("SpiceLevel", Integer.toString(getSpiceLevel()));
        return hashMap;
    }
}
