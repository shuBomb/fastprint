package com.app.fastprint.ui.productDetails.presenter;

import com.app.fastprint.ui.productDetails.ProductDetailsActivity;
import com.app.fastprint.ui.productDetails.interfaces.IMProductDetails;
import com.app.fastprint.ui.productDetails.interfaces.IPProductDetails;
import com.app.fastprint.ui.productDetails.interfaces.IProductDetails;
import com.app.fastprint.ui.productDetails.responseModel.ProductDetailsResponseModel;
import com.app.fastprint.ui.productDetails.responseModel.variationResponse.VariationResponseModel;
import com.app.fastprint.ui.productDetails.veiwModel.MProductDetails;

public
class PProductDetails implements IPProductDetails{
    IProductDetails iProductDetails;
    IMProductDetails imProductDetails;
    public PProductDetails(ProductDetailsActivity productDetailsActivity) {
        this.iProductDetails=productDetailsActivity;
    }

    @Override
    public void getProductDetails(String id) {
        imProductDetails=new MProductDetails(this);
        imProductDetails.getProductDetailsRestCalls(id);
    }

    @Override
    public void successResponseFromModel(ProductDetailsResponseModel productDetailsResponseModel) {
        iProductDetails.successResponseFromPresenter(productDetailsResponseModel);
    }

    @Override
    public void errorResponseFromModel(String message) {
        iProductDetails.errorResponseFromPresenter(message);
    }

    @Override
    public void getPrice(String id, String quantity, String pages,String paperType, String cover,
                         String color,String type) {
        imProductDetails=new MProductDetails(this);
        imProductDetails.getPriceRestCalls(id,quantity,pages,paperType,cover,
                color,type);
    }

    @Override
    public void successPriceResponseFromModel(VariationResponseModel priceResponseModel) {
        iProductDetails.successPriceResponseFromPresenter(priceResponseModel);
    }

    @Override
    public void errorPriceResponseFromModel(String message) {
        iProductDetails.errorPriceResponseFromPresenter(message);
    }
}
