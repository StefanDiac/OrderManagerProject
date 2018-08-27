package com.example.air_book.ordermanagerproject.Model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

/**
 * Created by air_book on 10/29/17.
 */

public class Deserts implements Orderable, Parcelable {

    private double price;
    private String name;
    private final ItemType itemType = ItemType.DESERT;
    private boolean isBought;

    public Deserts(double price, String name, boolean isBought) {
        this.price = price;
        this.name = name;
        this.isBought = isBought;
    }

    protected Deserts(Parcel in) {
        price = in.readDouble();
        name = in.readString();
        isBought = in.readByte() != 0;
    }

    public static final Creator<Deserts> CREATOR = new Creator<Deserts>() {
        @Override
        public Deserts createFromParcel(Parcel in) {
            return new Deserts(in);
        }

        @Override
        public Deserts[] newArray(int size) {
            return new Deserts[size];
        }
    };

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBought() {
        return isBought;
    }

    public void setBought(boolean bought) {
        isBought = bought;
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
        dest.writeByte((byte) (isBought ? 1 : 0));
    }

    public HashMap<String,String> getDesertValues(){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("Price",Double.toString(getPrice()));
        hashMap.put("StoreBought", Boolean.toString(isBought()));
        return hashMap;
    }
}
