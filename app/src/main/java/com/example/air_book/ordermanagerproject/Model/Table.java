package com.example.air_book.ordermanagerproject.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by air_book on 10/29/17.
 */

public class Table implements Parcelable {

    private int number;
    private int numberOfSeats;
    private boolean smokable;

    public Table(int number, int numberOfSeats, boolean smokable) {
        this.number = number;
        this.numberOfSeats = numberOfSeats;
        this.smokable = smokable;
    }

    public Table() {
        this.number = 0;
        this.numberOfSeats = 0;
        this.smokable = false;
    }

    protected Table(Parcel in) {
        number = in.readInt();
        numberOfSeats = in.readInt();
        smokable = in.readByte() != 0;
    }

    public static final Creator<Table> CREATOR = new Creator<Table>() {
        @Override
        public Table createFromParcel(Parcel in) {
            return new Table(in);
        }

        @Override
        public Table[] newArray(int size) {
            return new Table[size];
        }
    };

    public int getNumber() {
        return number;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public boolean isSmokable() {
        return smokable;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public void setSmokable(boolean smokable) {
        this.smokable = smokable;
    }

    @Override
    public String toString() {
        return "Table{" +
                "number=" + number +
                ", numberOfSeats=" + numberOfSeats +
                ", smokable=" + smokable +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(number);
        dest.writeInt(numberOfSeats);
        dest.writeByte((byte) (smokable ? 1 : 0));
    }
}
