package com.app.fastprint.ui.productDetails.interfaces;

public
interface IMProductDetails {
    void getProductDetailsRestCalls(String id);
    void getPriceRestCalls(String id,String quantity,String pages,String paperType,String cover,String color,String type);
}
