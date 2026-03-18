package com.app.fastprint.ui.cart.interfaces;

import com.app.fastprint.ui.cart.responseModel.CurrencyRateResponseModel;
import com.app.fastprint.ui.productDetails.responseModel.ProductDetailsResponseModel;
import com.app.fastprint.ui.productDetails.responseModel.variationResponse.VariationResponseModel;

public
interface ICurrencyInfo {

    void successResponseFromPresenter(CurrencyRateResponseModel currencyObject);
    void errorResponseFromPresenter(String message);



}
