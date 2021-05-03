package com.example.joxpressnextdaydelivery;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    long mBackPressed;
    TextView Logout,customername;
    SharedPreferences pref;
    LinearLayout order,imgProfile,imgEarnings,imgTransaction,imgLogout;
    private static final String KEY_PHONE = "phone";
    private static final String KEY_DATA = "data";
    private static final String KEY_TOKEN = "token";
    private static final String url="https://www.jogx.ph/api/v1/user/login";
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        pref = getSharedPreferences("user_details", MODE_PRIVATE);
        String customer_data = pref.getString(KEY_DATA, "");
        String customer_token = pref.getString(KEY_TOKEN, "");

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
                Intent createOrder = new Intent(HomeActivity.this, OrderActivity.class);
                startActivity(createOrder);
            }
        });

        imgEarnings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent my_earnings = new Intent(HomeActivity.this, EarningsActivity.class);
                startActivity(my_earnings);
            }
        });

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent my_profile = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(my_profile);
            }
        });

        imgTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent my_transaction = new Intent(HomeActivity.this, TransactionActivity.class);
                startActivity(my_transaction);
            }
        });

        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickDone();
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

    @Override
    public void onBackPressed() {
        int count = this.getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            if (mBackPressed + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
            } else {
                clickDone();

            }
        } else {
            super.onBackPressed();
        }
    }

    public void clickDone() {
        new AlertDialog.Builder(this, R.style.DialogStyle)
                .setIcon(R.drawable.logo)
                .setTitle(getString(R.string.app_name))
                .setMessage(getString(R.string.exit))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}