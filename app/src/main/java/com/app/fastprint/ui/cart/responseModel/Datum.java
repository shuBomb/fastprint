package com.app.fastprint.ui.cart.responseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Datum {

    @SerializedName("currency_symbol")
    @Expose
    private String currencySymbol;
    @SerializedName("rating_count")
    @Expose
    private Float ratingCount;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_price")
    @Expose
    private String productPrice;
    @SerializedName("product_pending_qty")
    @Expose
    private Integer productPendingQty;
    @SerializedName("product_image")
    @Expose
    private String productImage;
    @SerializedName("variation_id")
    @Expose
    private Integer variationId;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("line_total")
    @Expose
    private String lineTotal;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("cart_id")
    @Expose
    private String cartId;

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public Float getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Float ratingCount) {
        this.ratingCount = ratingCount;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getProductPendingQty() {
        return productPendingQty;
    }

    public void setProductPendingQty(Integer productPendingQty) {
        this.productPendingQty = productPendingQty;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public Integer getVariationId() {
        return variationId;
    }

    public void setVariationId(Integer variationId) {
        this.variationId = variationId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(String lineTotal) {
        this.lineTotal = lineTotal;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

}