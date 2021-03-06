package com.example.joxpressnextdaydelivery;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String url="https://www.jogx.ph/api/v1/user/login";
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        pref = getSharedPreferences("user_details", MODE_PRIVATE);
        String customer_data = pref.getString(KEY_DATA, "");
       String customer_token = pref.getString(KEY_TOKEN, "");
//
//        customername = findViewById(R.id.customer_name);
//        customername.setText(customer_data);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        order = findViewById(R.id.createOrder);
        imgEarnings = findViewById(R.id.my_earnings);
        imgProfile = findViewById(R.id.my_profile);
        imgTransaction = findViewById(R.id.my_transacitons);
        imgLogout = findViewById(R.id.my_logout);

        checkInternet();

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(HomeActivity.this);
                progressDialog.setMessage("Please wait....");
                progressDialog.show();

                Thread timer = new Thread(){
                    @Override
                    public void run() {
                        try{
                            sleep(2500);
                            Intent createOrder = new Intent(HomeActivity.this, OrderActivity.class);
                            createOrder.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(createOrder);
                            progressDialog.dismiss();
                            //finish();
                            super.run();
                        } catch(InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                };
                timer.start();


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
                my_profile.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                clickLogout();
            }
        });

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
                .setIcon(R.drawable.deliveeri_nextday)
                .setTitle(getString(R.string.app_name))
                .setMessage("Are you sure you want to exit the applicaiton?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        SharedPreferences.Editor editor = pref.edit();
                        editor.clear();
                        editor.apply();
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

    public void clickLogout(){
        new AlertDialog.Builder(this, R.style.DialogStyle)
                .setIcon(R.drawable.deliveeri_nextday)
                .setTitle(getString(R.string.app_name))
                .setMessage("Are you sure you want to Logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        SharedPreferences.Editor editor = pref.edit();
                        editor.clear();
                        editor.apply();
                        Toast.makeText(HomeActivity.this, "Logout", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                        startActivity(intent);
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

    public void checkInternet(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()){
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.activity_internet_connection);
            dialog.setCancelable(false);
            dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
            Button btnOk = dialog.findViewById(R.id.close_wifi);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            dialog.show();
        }
    }
}