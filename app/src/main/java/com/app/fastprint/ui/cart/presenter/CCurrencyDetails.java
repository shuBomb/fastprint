//package com.app.fastprint.ui.cart.presenter;
//
//import com.app.fastprint.ui.cart.CartActivity;
//import com.app.fastprint.ui.cart.interfaces.ICurrencyInfo;
//import com.app.fastprint.ui.cart.responseModel.CurrencyRateResponseModel;
//import com.app.fastprint.ui.product.viewModel.MProductListing;
//import com.app.fastprint.ui.productDetails.ProductDetailsActivity;
//import com.app.fastprint.ui.productDetails.interfaces.IMProductDetails;
//import com.app.fastprint.ui.productDetails.interfaces.IPProductDetails;
//import com.app.fastprint.ui.productDetails.interfaces.IProductDetails;
//import com.app.fastprint.ui.productDetails.responseModel.ProductDetailsResponseModel;
//import com.app.fastprint.ui.productDetails.responseModel.variationResponse.VariationResponseModel;
//import com.app.fastprint.ui.productDetails.veiwModel.MProductDetails;
//
//public
//class CCurrencyDetails implements ICurrencyInfo{
//    ICurrencyInfo isCurrencyDetails;
//
//    public CCurrencyDetails(CartActivity cartActivity) {
//        this.isCurrencyDetails=cartActivity;
//    }
//
//
//
//    @Override
//    public void successResponseFromPresenter(CurrencyRateResponseModel currencyRateResponseModel) {
//        isCurrencyDetails.successResponseFromPresenter(currencyRateResponseModel);
//    }
//
//    @Override
//    public void errorResponseFromPresenter(String message) {
//        isCurrencyDetails.errorResponseFromPresenter(message);
//    }
//
//}
