package com.example.joxpressnextdaydelivery;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class EarningsActivity extends AppCompatActivity {


    TextView Logout,customername;
    SharedPreferences pref;
    LinearLayout order,imgProfile,imgEarnings,imgTransaction,imgLogout;
    private static final String KEY_PHONE = "phone";
    private static final String KEY_DATA = "data";
    private static final String url="https://www.deliveeri.xyz/api/v1/user/login";
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earnings);
        pref = getSharedPreferences("user_details", MODE_PRIVATE);
        String customer_data = pref.getString(KEY_DATA, "");

//        customername = findViewById(R.id.customer_name);
//        customername.setText(customer_data);

      getSupportActionBar().setTitle("Earnings");
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#fefefe")));



    }

}