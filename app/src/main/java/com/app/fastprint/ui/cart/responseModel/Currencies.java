package com.app.fastprint.ui.cart.responseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Currencies {

    @SerializedName("JOD")
    @Expose
    private Jod jod;
    @SerializedName("USD")
    @Expose
    private Usd usd;

    public Jod getJod() {
        return jod;
    }

    public void setJod(Jod jod) {
        this.jod = jod;
    }

    public Usd getUsd() {
        return usd;
    }

    public void setUsd(Usd usd) {
        this.usd = usd;
    }

}
