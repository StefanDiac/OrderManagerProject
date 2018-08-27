package com.example.air_book.ordermanagerproject.Model;

/**
 * Created by air_book on 10/29/17.
 */

public interface Orderable {
    double VAT = 0.21;

    double computePrice();
    ItemType getItemType();

    String getName();
}
