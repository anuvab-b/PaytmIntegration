package com.anuvab.projects.paytmintegration;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText et_mobile;
    Button btn_pay;
    String strmobile;


    public static final String MID="Kolkat82634409237751";
    public static final String INDUSTRY_TYPE_ID="Retail";
    public static final String CHANNEL_ID="WAP";
    public static final String WEBSITE="APPSTAGING";
    public static final String CALLBACK_URL="Kolkat82634409237751";
    //public static final String TAX_AMOUNT="Kolkat82634409237751";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_mobile=findViewById(R.id.etMobile);
        btn_pay=findViewById(R.id.btnPay);

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strmobile=et_mobile.getText().toString().trim();
                if(strmobile.length()==10){
                    GenerateCheck();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please enter correct mobile number",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void GenerateCheck() {

    }
}
