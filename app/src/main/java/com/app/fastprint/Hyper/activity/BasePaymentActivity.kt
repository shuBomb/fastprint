package com.app.fastprint.Hyper.activity

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.widget.Toast
import com.app.fastprint.Hyper.common.Constants
import com.app.fastprint.Hyper.model.CheckoutModel
import com.app.fastprint.R
import com.app.fastprint.services.APIClient
import com.app.fastprint.services.APIInterface
import com.app.fastprint.ui.thankyou.ThankyouActivity
import com.app.fastprint.utills.AppControler
import com.google.gson.Gson
import com.oppwa.mobile.connect.checkout.meta.CheckoutActivityResult
import com.oppwa.mobile.connect.checkout.meta.CheckoutActivityResultContract
import com.oppwa.mobile.connect.checkout.meta.CheckoutSettings
import com.oppwa.mobile.connect.checkout.meta.CheckoutSkipCVVMode
import com.oppwa.mobile.connect.exception.PaymentError
import com.oppwa.mobile.connect.provider.Connect
import com.oppwa.mobile.connect.provider.Transaction
import com.oppwa.mobile.connect.provider.TransactionType
import com.oppwa.mobile.connect.utils.googlepay.CardPaymentMethodJsonBuilder
import com.oppwa.mobile.connect.utils.googlepay.PaymentDataRequestJsonBuilder
import com.oppwa.mobile.connect.utils.googlepay.TransactionInfoJsonBuilder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


open class BasePaymentActivity  : BaseActivity() {

    var resourcePath: String? = null
    var orderID: String? = ""
    var consumerKey: String? = ""
    var consumerSecret: String? = ""
    // LIVE
    var authToken : String = "OGFjOWE0Yzk4ODRjOWYxNzAxODg2MTY1ZjQ4ZjU3ODV8ck0zMkdlQ3dRaw=="
    // DEV
    //var authToken : String = "OGFjN2E0Y2E4NmNjMjRmMTAxODZkNDQ5YjMzNTA2NTF8OWZTTWhYTjVjcA=="

    // LIVE
    var entityID : String = "8ac9a4c9884c9f170188616715ba578d"
    // DEV
    //var authToken : String = "8ac7a4ca86cc24f10186d44a32790655"

    protected val checkoutLauncher = registerForActivityResult(
        CheckoutActivityResultContract()
    ) {
            result: CheckoutActivityResult -> this.handleCheckoutActivityResult(result)
    }

    protected var isAsyncCallbackReceived = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val policy = ThreadPolicy.Builder().permitAll().build()

        StrictMode.setThreadPolicy(policy)
        if (savedInstanceState != null) {
            resourcePath = savedInstanceState.getString("STATE_RESOURCE_PATH")
        }
    }

    @ExperimentalCoroutinesApi
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        setIntent(intent)

        /* Check of the intent contains the callback scheme */
        if (hasCallBackScheme(intent)) {
            isAsyncCallbackReceived = true

            if (resourcePath != null) {
                requestPaymentStatus(resourcePath!!)
            }
        }
    }

    private fun handleCheckoutActivityResult(result: CheckoutActivityResult) {
        hideProgressBar()

        if (result.isCanceled) {
            return
        }

        if (result.isErrored) {
            val error: PaymentError? = result.paymentError
            error?.let { showAlertDialog(it.errorMessage) }

            return
        }

        /* Transaction completed */
        val transaction: Transaction? = result.transaction

        resourcePath = result.resourcePath

        /* Check the transaction type */
        if (transaction != null) {
            if (transaction.transactionType == TransactionType.SYNC) {
                /* Check the status of synchronous transaction */
                requestPaymentStatus(resourcePath!!)
            } else if (isAsyncCallbackReceived) {
                /* Asynchronous transaction is processed in the onNewIntent()
                   NOTE: Prior API level 29 the onNewIntent() will be called before ActivityResultCallback */
                requestPaymentStatus(resourcePath!!)
            }
        }
    }

    open fun onCheckoutIdReceived(checkoutId: String?) {
        if (checkoutId == null) {
            hideProgressBar()
            showAlertDialog(R.string.error_message)
        } else {
            resourcePath = null
            isAsyncCallbackReceived = false
        }
    }

    protected fun requestCheckoutId(mOrderid:String,amount:String,currency:String,consumerKey:String,consumerSecret:String) {
        this.orderID = mOrderid;
        this.consumerSecret = consumerSecret;
        this.consumerKey = consumerKey;
        showProgressBar()

        runOnUiThread {

            val apiService = APIClient.getRetrofitInstance().create(
                APIInterface::class.java)

            val timeStampFormat = SimpleDateFormat("yyyyMMddHHmmssSS")
            val myDate = Date()
            val uniqueName: String = timeStampFormat.format(myDate)


//            val params = HashMap<String, String>()
//            params["amount"] = amount
//            params["currency"] = currency
//            params["entityId"] = entityID
//            params["paymentType"] = "DB"
//            //params["testMode"] = "EXTERNAL"
//            params["merchantTransactionId"] = uniqueName
//            params["customer.email"] = AppControler.getInstance(this).getString(AppControler.Key.USER_EMAIL)
//            params["billing.street1"] = "King fawad Road"
//            params["billing.city"] = "Riyadh"
//            params["billing.state"] = "Riyadh"
//            params["billing.country"] = "SA"
//            params["billing.postcode"] = "12211"
//            params["customer.givenName"] = AppControler.getInstance(this).getString(AppControler.Key.USER_FIRST_NAME)
//            params["customer.surname"] = AppControler.getInstance(this).getString(AppControler.Key.USER_LAST_NAME)


            val params = HashMap<String, String>()
            params["amount"] = amount
            params["currency"] = currency
            params["entityId"] = entityID
            params["paymentType"] = "DB"
            //params["testMode"] = "EXTERNAL"
            params["merchantTransactionId"] = uniqueName
            params["email"] = AppControler.getInstance(this).getString(AppControler.Key.USER_EMAIL)
            params["street1"] = "King fawad Road"
            params["city"] = "Riyadh"
            params["state"] = "Riyadh"
            params["country"] = "SA"
            params["postcode"] = "12211"
            params["givenName"] = AppControler.getInstance(this).getString(AppControler.Key.USER_FIRST_NAME)
            params["surname"] = AppControler.getInstance(this).getString(AppControler.Key.USER_LAST_NAME)


            var bearerToken = "Bearer "+authToken
            val callGenerateToken = apiService.getCheckoutID(bearerToken,params)

            callGenerateToken.enqueue(object : Callback<String> {
                override fun onResponse(
                    call: Call<String>,
                    response: Response<String>
                ) {

                    if(response.body() != null && !response.body().equals("")){

                        val gson = Gson()
                        var checkoutModel =  gson.fromJson(response.body().toString(), CheckoutModel::class.java)

                        onCheckoutIdReceived(checkoutModel.id)
                    }else{
                        Toast.makeText(this@BasePaymentActivity,  "Invalid Card", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    hideProgressBar()
                    Toast.makeText(this@BasePaymentActivity, t.message + "", Toast.LENGTH_SHORT).show()
                }
            })


            //var checkoutIDFromServier = requestForCheckoutID(currency,amount)

        }

        /*MerchantServerApplication.requestCheckoutId(
            amount,
            currency,
            "PA",
            ServerMode.TEST,
            mapOf("notificationUrl" to Constants.NOTIFICATION_URL)
        ) { checkoutId, _ -> runOnUiThread { onCheckoutIdReceived(checkoutId) } }*/
    }


    protected fun requestPaymentStatus(resourcePath: String) {

        Log.e("Shubham-Resource",resourcePath)
        showProgressBar()

        var resourcePathUpdated = resourcePath.substring(14)
        var getCheckoutIDFromResource = resourcePathUpdated.replace("/payment","")

        checkPaymentStatus(getCheckoutIDFromResource)
        /*MerchantServerApplication.requestPaymentStatus(
            resourcePath
        ) { isSuccessful, _ -> runOnUiThread { onPaymentStatusReceived(isSuccessful) } }*/
    }

    protected fun checkPaymentStatus(checkoutId:String) {
        showProgressBar()

        runOnUiThread {

            val apiService = APIClient.getHyperPayApiClient().create(
                APIInterface::class.java)


            var bearerToken = "Bearer "+authToken
            val callGenerateToken = apiService.checkPaymentStatus(bearerToken,checkoutId,entityID)

            callGenerateToken.enqueue(object : Callback<CheckoutModel> {
                override fun onResponse(
                    call: Call<CheckoutModel>,
                    response: Response<CheckoutModel>
                ) {
                    hideProgressBar()
                    if(response.code() == 200){
                        if(response.body()!!.result != null && response.body()!!.result.code.equals("000.000.000")){
                            onPaymentStatusReceived(true,response.body()!!.result.description)
                        }else{
                            if(response.body()!!.result != null){
                                onPaymentStatusReceived(false,response.body()!!.result.description)
                            }else{
                                onPaymentStatusReceived(false,response.body()!!.result.description)
                            }
                        }
                    }else{
                        onPaymentStatusReceived(false,"")
                    }
                }

                override fun onFailure(call: Call<CheckoutModel>, t: Throwable) {
                    hideProgressBar()
                    onPaymentStatusReceived(false,"")
                    Toast.makeText(this@BasePaymentActivity, t.message + "", Toast.LENGTH_SHORT).show()
                }
            })
            //var checkoutIDFromServier = requestForCheckoutID(currency,amount)
        }

    }


    protected fun createCheckoutSettings(checkoutId: String, callbackScheme: String): CheckoutSettings {
        return CheckoutSettings(checkoutId, Constants.Config.PAYMENT_BRANDS,
            Connect.ProviderMode.LIVE)
            .setSkipCVVMode(CheckoutSkipCVVMode.FOR_STORED_CARDS)
            .setShopperResultUrl("$callbackScheme://callback")
            .setGooglePayPaymentDataRequestJson(getGooglePayPaymentDataRequestJson())
            /* Set componentName if you want to receive callbacks from the checkout */
            .setComponentName(ComponentName(packageName, CheckoutBroadcastReceiver::class.java.name))
    }

    private fun hasCallBackScheme(intent: Intent): Boolean {
        return intent.scheme == getString(R.string.checkout_ui_callback_scheme) ||
                intent.scheme == getString(R.string.payment_button_callback_scheme) ||
                intent.scheme == getString(R.string.custom_ui_callback_scheme)
    }

    private fun onPaymentStatusReceived(isSuccessful: Boolean,msg: String) {
        hideProgressBar()
        if (isSuccessful) {

            val intentPayNow = Intent(this, ThankyouActivity::class.java)
            intentPayNow.putExtra("orderId", orderID)
            intentPayNow.putExtra("consumerKey", consumerKey)
            intentPayNow.putExtra("consumerSecret", consumerSecret)
            startActivity(intentPayNow)
            finishAffinity()
            //var displayMsg = getString(R.string.message_successful_payment)+" and transaction ID "+ transactionID;
            //showAlertDialog(displayMsg+"")
        } else {
            if(msg.equals("")){
                showErrorDialogNew(getString(R.string.message_unsuccessful_payment))
            }else{
                showErrorDialogNew(msg)
            }
        }
    }
    private fun getGooglePayPaymentDataRequestJson() : String {
        val allowedPaymentMethods = JSONArray()
            .put(
                CardPaymentMethodJsonBuilder()
                .setAllowedAuthMethods(
                    JSONArray()
                    .put("PAN_ONLY")
                    .put("CRYPTOGRAM_3DS")
                )
                .setAllowedCardNetworks(
                    JSONArray()
                    .put("VISA")
                    .put("MASTERCARD")
                    .put("AMEX")
                    .put("DISCOVER")
                    .put("JCB")
                )
                .setGatewayMerchantId(Constants.MERCHANT_ID)
                .toJson()
            )

        val transactionInfo = TransactionInfoJsonBuilder()
            .setCurrencyCode(Constants.Config.CURRENCY)
            .setTotalPriceStatus("FINAL")
            .setTotalPrice(Constants.Config.AMOUNT)
            .toJson()

        val paymentDataRequest = PaymentDataRequestJsonBuilder()
            .setAllowedPaymentMethods(allowedPaymentMethods)
            .setTransactionInfo(transactionInfo)
            .toJson()

        return paymentDataRequest.toString()
    }

}