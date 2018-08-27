package com.example.air_book.ordermanagerproject.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

/**
 * Created by air_book on 10/29/17.
 */

public class Drink implements Orderable, Parcelable {

    private double price;
    private String name;
    private final ItemType itemType = ItemType.DRINKS;

    public Drink(double price, String name) {
        this.price = price;
        this.name = name;
    }

    protected Drink(Parcel in) {
        price = in.readDouble();
        name = in.readString();
    }

    public static final Creator<Drink> CREATOR = new Creator<Drink>() {
        @Override
        public Drink createFromParcel(Parcel in) {
            return new Drink(in);
        }

        @Override
        public Drink[] newArray(int size) {
            return new Drink[size];
        }
    };

    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }


    public void setPrice(double price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
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
        dest.writeDouble(price);
        dest.writeString(name);
    }

    public HashMap<String,String> getDrinkValues(){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("Price",Double.toString(getPrice()));
        return hashMap;
    }
}
