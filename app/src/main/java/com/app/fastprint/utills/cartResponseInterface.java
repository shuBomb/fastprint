package com.app.fastprint.utills;

import com.app.fastprint.ui.cart.responseModel.CartListing;

import java.util.ArrayList;

public interface cartResponseInterface {
    void onDataGot(ArrayList<CartListing> cartListings);
    void onError(String error);
}
