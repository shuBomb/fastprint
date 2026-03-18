//package com.app.fastprint.ui.cart.viewModel;
//
//import android.os.Handler;
//import android.os.Message;
//
//import androidx.annotation.NonNull;
//
//import com.app.fastprint.services.APIInterface;
//import com.app.fastprint.services.RetrofitCalls;
//import com.app.fastprint.ui.cart.interfaces.ICurrencyInfo;
//import com.app.fastprint.ui.cart.presenter.CCurrencyDetails;
//import com.app.fastprint.ui.cart.responseModel.CurrencyRateResponseModel;
//import com.app.fastprint.ui.productDetails.interfaces.IMProductDetails;
//import com.app.fastprint.ui.productDetails.presenter.PProductDetails;
//import com.app.fastprint.ui.productDetails.responseModel.ProductDetailsResponseModel;
//import com.app.fastprint.ui.productDetails.responseModel.variationResponse.VariationResponseModel;
//
//public
//class MCurrencyDetails implements ICurrencyInfo {
//    ICurrencyInfo ipCurrencyInfo;
//
//    public MCurrencyDetails(CCurrencyDetails cCurrencyDetails) {
//        this.ipCurrencyInfo = cCurrencyDetails;
//
//    }
//    @Override
//    public void getCurrencyRatesDetails() {
//        RetrofitCalls retrofitCalls = new RetrofitCalls();
//        retrofitCalls.getCurrencyRate(mHandler);
//    }
//
//
//
//    Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            switch (msg.what) {
//                case APIInterface.CURRENCY_RATE_SUCCESS:
//                    CurrencyRateResponseModel currencyRateResponseModel = ((CurrencyRateResponseModel) msg.obj);
//                    ipCurrencyInfo.successResponseFromPresenter(currencyRateResponseModel);
//                    break;
//                case APIInterface.CURRENCY_RATE_FAILED:
//                    String invalidRequest = String.valueOf(msg.obj);
//                    ipCurrencyInfo.errorResponseFromPresenter(invalidRequest);
//                    break;
//
//
//            }
//        }
//    };
//}
