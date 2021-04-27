package com.example.joxpressnextdaydelivery;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class ReviewSmallDeliveryActivity extends AppCompatActivity {


    TextView reviewType,reviewWeight,reviewLength,reviewWidth,reviewHeight,senderinfo,senderamount,senderaddress;
    SharedPreferences pref;
    TextView reviewsmall_item_name,reviewsmall_item_amount,reviewsmall_sender_info,reviewsmall_sendernumber,reviewsmall_senderaddress,reviewspecific_address,reviewsmall_sendercity,reviewsmall_senderbarangay;
    private static final String KEY_PHONE = "phone";
    private static final String KEY_DATA = "data";
    private static final String url="https://www.jogx.ph/api/v1/user/login";
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_small);
        pref = getSharedPreferences("user_details", MODE_PRIVATE);
        String customer_data = pref.getString(KEY_DATA, "");


        String smallitem_province = getIntent().getExtras().getString("small_sender_province");
        String smallitem_city = getIntent().getExtras().getString("small_sender_city");

        String smallitem_amount = getIntent().getExtras().getString("small_item_amount");
        String smallitem_info = getIntent().getExtras().getString("small_sender_info");

        //Sender Info
        String smallitem_name = getIntent().getExtras().getString("small_item_name");
        String smallitem_number = getIntent().getExtras().getString("small_sender_number");
        String specify_address = getIntent().getExtras().getString("small_specify_address");
        String pickup_address = getIntent().getExtras().getString("small_pickup_address");



        reviewType = findViewById(R.id.reviewsmalltype);
        reviewWeight = findViewById(R.id.reviewsmallweight);
        reviewLength = findViewById(R.id.reviewsmalllength);
        reviewWidth = findViewById(R.id.reviewsmallwidth);
        reviewHeight = findViewById(R.id.reviewsmallheight);

        reviewsmall_item_name = findViewById(R.id.reviewsmallname);
        reviewsmall_item_name.setText(smallitem_info);

        reviewsmall_sendernumber = findViewById(R.id.reviewsmallnumber);
        reviewsmall_sendernumber.setText(smallitem_number);

        reviewsmall_senderaddress = findViewById(R.id.reviewsmallcompleteaddress);
        reviewsmall_senderaddress.setText(pickup_address);

        reviewspecific_address = findViewById(R.id.reviewsmallcompletespecificaddress);
        reviewspecific_address.setText(specify_address);



        reviewType.setText("Small");
        reviewWeight.setText("3 Kg");
        reviewLength.setText("18 cm");
        reviewWidth.setText("12 cm");
        reviewHeight.setText("5cm");

//        customername = findViewById(R.id.customer_name);
//        customername.setText(customer_data);

      getSupportActionBar().setTitle("Review your delivery");
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#fefefe")));



    }

}