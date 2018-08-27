package com.example.air_book.ordermanagerproject.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by air_book on 10/29/17.
 */

public class Order implements Parcelable{
    private final Table table;
    private final Waiter waiter;
    private final int orderNumber;
    private double total;
    private double tipPercentage;
    private Orderable[] orderItems;

    public Order(Table table, Waiter waiter, int orderNumber, double total, double tipPercentage, Orderable[] orderItems) {
        this.table = table;
        this.waiter = waiter;
        this.orderNumber = orderNumber;
        this.total = total;
        this.tipPercentage = tipPercentage;
        this.orderItems = orderItems;
    }

    protected Order(Parcel in) {
        table = in.readParcelable(Table.class.getClassLoader());
        waiter = in.readParcelable(Waiter.class.getClassLoader());
        orderNumber = in.readInt();
        total = in.readDouble();
        tipPercentage = in.readDouble();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    public Table getTable() {
        return table;
    }

    public Waiter getWaiter() {
        return waiter;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public double getTotal() {
        return total;
    }

    public double getTipPercentage() {
        return tipPercentage;
    }

    public Orderable[] getOrderItems() {
        return orderItems;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setTipPercentage(double tipPercentage) {
        this.tipPercentage = tipPercentage;
    }

    public void setOrderItems(Orderable[] orderItems) {
        this.orderItems = orderItems;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(table, flags);
        dest.writeParcelable(waiter, flags);
        dest.writeInt(orderNumber);
        dest.writeDouble(total);
        dest.writeDouble(tipPercentage);
    }
}
