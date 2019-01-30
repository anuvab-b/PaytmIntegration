package com.anuvab.projects.paytmintegration;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {
    EditText et_mobile;
    Button btn_pay;
    String strmobile="7777777777";
    String amount = "1";
    String CUSID = "786";
    String checksum ="";
    String orderId ="";


    public static final String MID = "Kolkat82634409237751";
    public static final String INDUSTRY_TYPE_ID = "Retail";
    public static final String CHANNEL_ID = "WAP";
    public static final String WEBSITE = "APPSTAGING";
    public static String CALLBACK_URL="https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_mobile = findViewById(R.id.etMobile);
        btn_pay = findViewById(R.id.btnPay);

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //strmobile = et_mobile.getText().toString().trim();
                if (strmobile.length() == 10) {
                    GenerateCheck();
                    btn_pay.setVisibility(GONE);
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter correct mobile number", Toast.LENGTH_SHORT).show();
                }
            }
        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
    @Override

    protected void onStart() {
        super.onStart();
        //initOrderId();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
    private void GenerateCheck() {
        Random r = new Random(System.currentTimeMillis());
        orderId = "ORDER" + (1 + r.nextInt(2)) * 10000 + r.nextInt(10000);
        CALLBACK_URL=CALLBACK_URL+orderId;
        String url = "https://kbtickets.000webhostapp.com/checkSum.php";
        Map<String, String> params = new HashMap<>();
        params.put("MID", MID);
        params.put("ORDER_ID", orderId);
        params.put("CUST_ID", CUSID);
        params.put("INDUSTRY_TYPE_ID", INDUSTRY_TYPE_ID);
        params.put("CHANNEL_ID", CHANNEL_ID);
        params.put("TXN_AMOUNT", amount);
        params.put("WEBSITE", WEBSITE);
        params.put("CALLBACK_URL", CALLBACK_URL);
        params.put("MOBILE_NO", strmobile);

        JSONObject json = new JSONObject(params);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                checksum=response.optString("CHECKSUMHASH");
                //if(checksum.trim().length()!=0){
                    onStartTransaction();
                //}
                Log.e("getResponse", String.valueOf(response));
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
    public void onStartTransaction() {

        PaytmPGService Service = PaytmPGService.getStagingService();
        Map<String, String> paramMap = new HashMap<>();


        // these are mandatory parameters
        paramMap.put("MID",MID);
        paramMap.put("ORDER_ID",orderId);
        paramMap.put("CUST_ID",CUSID);
        paramMap.put("INDUSTRY_TYPE_ID",INDUSTRY_TYPE_ID);
        paramMap.put("CHANNEL_ID",CHANNEL_ID);
        paramMap.put("TXN_AMOUNT","1");
        paramMap.put("WEBSITE",WEBSITE);
        paramMap.put("CALLBACK_URL",CALLBACK_URL);
        paramMap.put("MOBILE_NO",strmobile);
        paramMap.put("CHECKSUMHASH",checksum);
        //paramMap.put("EMAIL","abc@gmail.com");



/*
        paramMap.put("MID" , "WorldP64425807474247");
        paramMap.put("ORDER_ID" , "210lkldfka2a27");
        paramMap.put("CUST_ID" , "mkjNYC1227");
        paramMap.put("INDUSTRY_TYPE_ID" , "Retail");
        paramMap.put("CHANNEL_ID" , "WAP");
        paramMap.put("TXN_AMOUNT" , "1");
        paramMap.put("WEBSITE" , "worldpressplg");
        paramMap.put("CALLBACK_URL" , "https://pguat.paytm.com/paytmchecksum/paytmCheckSumVerify.jsp");*/
        PaytmOrder Order = new PaytmOrder(paramMap);
		/*PaytmMerchant Merchant = new PaytmMerchant(
				"https://pguat.paytm.com/paytmchecksum/paytmCheckSumGenerator.jsp",
				"https://pguat.paytm.com/paytmchecksum/paytmCheckSumVerify.jsp");*/
        Service.initialize(Order, null);
        Service.startPaymentTransaction(this, true, true, new PaytmPaymentTransactionCallback(){
                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {

                        // Some UI Error Occurred in Payment Gateway Activity.

                        // // This may be due to initialization of views in

                        // Payment Gateway Activity or may be due to //

                        // initialization of webview. // Error Message details

                        // the error occurred.

                    }
					/*@Override

					public void onTransactionSuccess(Bundle inResponse) {

						// After successful transaction this method gets called.

						// // Response bundle contains the merchant response

						// parameters.

						Log.d("LOG", "Payment Transaction is successful " + inResponse);

						Toast.makeText(getApplicationContext(), "Payment Transaction is successful ", Toast.LENGTH_LONG).show();

					}
					@Override

					public void onTransactionFailure(String inErrorMessage,

							Bundle inResponse) {

						// This method gets called if transaction failed. //

						// Here in this case transaction is completed, but with

						// a failure. // Error Message describes the reason for

						// failure. // Response bundle contains the merchant

						// response parameters.

						Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);

						Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();

					}*/
                    @Override
                    public void onTransactionResponse(Bundle inResponse) {
                        Log.d("LOG", "Payment Transaction is successful " + inResponse);
                        Toast.makeText(getApplicationContext(), "Payment Transaction response " + inResponse.toString(), Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void networkNotAvailable() { // If network is not
                        // available, then this
                        // method gets called.
                    }
                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {
                        // This method gets called if client authentication
                        // failed. // Failure may be due to following reasons //
                        // 1. Server error or downtime. // 2. Server unable to
                        // generate checksum or checksum response is not in
                        // proper format. // 3. Server failed to authenticate
                        // that client. That is value of payt_STATUS is 2. //
                        // Error Message describes the reason for failure.
                    }
                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {

                    }
                    // had to be added: NOTE
                    @Override
                    public void onBackPressedCancelTransaction() {
                        Toast.makeText(MainActivity.this,"Back pressed. Transaction cancelled",Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                        Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);
                        Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
