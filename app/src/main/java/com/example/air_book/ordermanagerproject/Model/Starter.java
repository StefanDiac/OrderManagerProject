package com.example.air_book.ordermanagerproject.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Created by air_book on 10/29/17.
 */

public class Starter implements Orderable, Parcelable {

    private String name;
    private double price;
    private boolean isCold;
    private final ItemType type = ItemType.STARTER_DISH;

    public Starter(String name, double price, boolean isCold) {
        this.name = name;
        this.price = price;
        this.isCold = isCold;
    }

    protected Starter(Parcel in) {
        name = in.readString();
        price = in.readDouble();
        isCold = in.readByte() != 0;
    }

    public static final Creator<Starter> CREATOR = new Creator<Starter>() {
        @Override
        public Starter createFromParcel(Parcel in) {
            return new Starter(in);
        }

        @Override
        public Starter[] newArray(int size) {
            return new Starter[size];
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

    public boolean isCold() {
        return isCold;
    }

    public void setCold(boolean cold) {
        isCold = cold;
    }

    @Override
    public double computePrice() {
        return VAT*getPrice();
    }

    @Override
    public ItemType getItemType() {
        return type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(price);
        dest.writeByte((byte) (isCold ? 1 : 0));
    }

    public HashMap<String,String> getStarterValues(){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("Price",Double.toString(getPrice()));
        hashMap.put("IsCold",Boolean.toString(isCold()));
        return hashMap;
    }
}
