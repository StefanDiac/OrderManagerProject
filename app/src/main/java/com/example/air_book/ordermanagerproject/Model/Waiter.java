package com.example.air_book.ordermanagerproject.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by air_book on 10/29/17.
 */

public class Waiter implements Parcelable {
    private String firstName;
    private String lastName;
    private double salary;
    private int age;
    private Boolean isPFA;
    private String CNP;

    public Waiter(String firstName, String lastName, double salary, int age, boolean isPFA, String CNP) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.age = age;
        this.isPFA = isPFA;
        this.CNP = CNP;
    }

    public Waiter() {
        this.firstName = new String();
        this.lastName = new String();
        this.salary = 0;
        this.age = 0;
        this.isPFA = false;
        this.CNP = new String();
    }

    protected Waiter(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        salary = in.readDouble();
        age = in.readInt();
        isPFA = in.readByte() != 0;
        CNP = in.readString();
    }


    public static final Creator<Waiter> CREATOR = new Creator<Waiter>() {
        @Override
        public Waiter createFromParcel(Parcel in) {
            return new Waiter(in);
        }

        @Override
        public Waiter[] newArray(int size) {
            return new Waiter[size];
        }
    };

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public double getSalary() {
        return salary;
    }

    public int getAge() {
        return age;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Boolean getPFA() {
        return isPFA;
    }

    public void setPFA(Boolean PFA) {
        isPFA = PFA;
    }

    public String getCNP() {
        return CNP;
    }

    public void setCNP(String CNP) {
        this.CNP = CNP;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeDouble(salary);
        dest.writeInt(age);
        dest.writeByte((byte) (isPFA ? 1 : 0));
        dest.writeString(CNP);
    }
}
