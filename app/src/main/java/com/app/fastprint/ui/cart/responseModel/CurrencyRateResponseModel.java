package com.app.fastprint.ui.cart.responseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CurrencyRateResponseModel {

    @SerializedName("currencies")
    @Expose
    private Currencies currencies;

    public Currencies getCurrencies() {
        return currencies;
    }

    public void setCurrencies(Currencies currencies) {
        this.currencies = currencies;
    }

}

