package com.example.joxpressnextdaydelivery;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    TextView Logout,customername;
    SharedPreferences pref;
    LinearLayout order,imgProfile,imgEarnings,imgTransaction,imgLogout;
    private static final String KEY_PHONE = "phone";
    private static final String KEY_DATA = "data";
    private static final String url="https://www.jogx.ph/api/v1/user/login";
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        pref = getSharedPreferences("user_details", MODE_PRIVATE);
        String customer_data = pref.getString(KEY_DATA, "");

        customername = findViewById(R.id.customer_name);
        customername.setText(customer_data);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        order = findViewById(R.id.createOrder);
        imgEarnings = findViewById(R.id.my_earnings);
        imgProfile = findViewById(R.id.my_profile);
        imgTransaction = findViewById(R.id.my_transacitons);
        imgLogout = findViewById(R.id.my_logout);

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Create Order Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        imgEarnings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Earnings Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Profile Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        imgTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Transaction Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Logout Clicked", Toast.LENGTH_SHORT).show();
            }
        });


//        Logout = findViewById(R.id.txtlogout);
//        Logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                SharedPreferences.Editor editor = pref.edit();
//                editor.putString(KEY_PHONE, null);
//                editor.clear();
//                editor.commit();
//                Toast.makeText(HomeActivity.this, "Logout Successfully", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        });

    }

}