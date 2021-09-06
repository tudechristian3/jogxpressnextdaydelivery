package com.example.joxpressnextdaydelivery;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ReviewLargeDeliveryActivity extends AppCompatActivity {


    TextView Logout,customername;
    SharedPreferences pref;
    LinearLayout order,imgProfile,imgEarnings,imgTransaction,imgLogout;
    //TextView Package
    TextView p_weight,p_length,p_width,p_height;
    //TextView Item
    TextView i_amount;
    //TextView Pickup
    TextView p_sendername,p_sendernumber,p_senderaddress,p_senderspecifyaddress;
    //TextView Receiver
    TextView r_receivername,r_receviernumber,r_receiveraddress,r_receiverspecifyaddress;
    //TextView
    TextView large_fee,large_total_fee;
    TextView receiver_fee_large;
    TextView cod_receiver;
    Button btnDeliver;
    private int fee_total = 0;
    private static final String KEY_PHONE = "phone";
    private static final String KEY_DATA = "data";
    private static final String KEY_TOKEN = "token";
    private static final String url="https://www.deliveeri.xyz/api/v1/user/login";
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_large);
        pref = getSharedPreferences("user_details", MODE_PRIVATE);
        String customer_data = pref.getString(KEY_DATA, "");

//        customername = findViewById(R.id.customer_name);
//        customername.setText(customer_data);

      getSupportActionBar().setTitle("Review your delivery");
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#fefefe")));


        //Package Detail
        String package_weight = getIntent().getExtras().getString("large_weight");
        String package_length = getIntent().getExtras().getString("large_length");
        String package_width = getIntent().getExtras().getString("large_width");
        String package_height = getIntent().getExtras().getString("large_height");
        //Item Detail

        String item_amount = getIntent().getExtras().getString("large_item_amount");
        //Pickup
        String pickup_name = getIntent().getExtras().getString("large_pickup_info");
        String pickup_number = getIntent().getExtras().getString("large_pickup_number");
        String pickup_address = getIntent().getExtras().getString("largesenderaddress");
        String pickup_specify_address = getIntent().getExtras().getString("large_pickup_specify_address");
        //Receiver
        String receiver_name = getIntent().getExtras().getString("large_receiver_info");
        String receiver_number = getIntent().getExtras().getString("large_receiver_number");
        String receiver_address = getIntent().getExtras().getString("largereceiveraddress");
        String receiver_specify_address = getIntent().getExtras().getString("large_receiver_specify_address");
        //CityCode
        String CityCode = getIntent().getExtras().getString("largecitycode");
        //COD checked
        String cod_payment = getIntent().getExtras().getString("cod_checked_large");

        if(cod_payment.equals("true")){
            receiver_fee_large = findViewById(R.id.reviewlargreceiverfee);
            receiver_fee_large.setText(item_amount);
            cod_receiver = findViewById(R.id.reviewlargpayment);
            cod_receiver.setText("✓");
        }



        p_weight = findViewById(R.id.reviewlargweight);
        p_length = findViewById(R.id.reviewlarglength);
        p_width = findViewById(R.id.reviewlargwidth);
        p_height = findViewById(R.id.reviewlargheight);


        //Pacakge
        p_weight.setText(package_weight);
        p_length.setText(package_length);
        p_width.setText(package_width);
        p_height.setText(package_height);

        //Item
        i_amount = findViewById(R.id.reviewlargpeso);
        i_amount.setText(item_amount);

        //Pickup
        p_sendername = findViewById(R.id.reviewlargename);
        p_sendernumber = findViewById(R.id.reviewlargenumber);
        p_senderaddress = findViewById(R.id.reviewlargcompleteaddress);
        p_senderspecifyaddress = findViewById(R.id.reviewlargcompletespecificaddress);

        p_sendername.setText(pickup_name);
        p_sendernumber.setText(pickup_number);
        p_senderaddress.setText(pickup_address);
        p_senderspecifyaddress.setText(pickup_specify_address);


        //Receiver
        r_receivername = findViewById(R.id.reviewlargdeliveryname);
        r_receviernumber = findViewById(R.id.reviewlargdeliverynumber);
        r_receiveraddress = findViewById(R.id.reviewlargdeliverycompleteaddress);
        r_receiverspecifyaddress = findViewById(R.id.reviewlargdeliverycompletespecificaddress);

        r_receivername.setText(receiver_name);
        r_receviernumber.setText(receiver_number);
        r_receiveraddress.setText(receiver_address);
        r_receiverspecifyaddress.setText(receiver_specify_address);

        large_fee = findViewById(R.id.reviewlargfee);
        large_total_fee = findViewById(R.id.reviewlargtotalfee);

        //get fee
        StringRequest request = new StringRequest(Request.Method.GET, "https://www.deliveeri.xyz/ratesAPI/"+CityCode+"/Large/4", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject obj = new JSONObject(response);
                    int fee_amount = obj.getInt("rate");
                    if(cod_payment.equals("true")){
                        int amount_item = Integer.parseInt(item_amount);
                        int sum = fee_amount + amount_item;
                        large_fee.setText(String.valueOf(fee_amount));
                        large_total_fee.setText(String.valueOf(sum));
                    }else{
                        fee_total = Integer.parseInt(String.valueOf(fee_amount));
                        large_fee.setText(String.valueOf(fee_amount));
                        large_total_fee.setText(String.valueOf(fee_amount));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);


        btnDeliver = findViewById(R.id.LargeDeliverySubmit);
        btnDeliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                largeDelivery();
            }
        });

    }

    public void largeDelivery(){
        //token
        String customer_token = pref.getString(KEY_TOKEN, "");
        //fee
        String fee = large_total_fee.getText().toString();
        //package
        String weight = p_weight.getText().toString();
        String length = p_length.getText().toString();
        String width = p_width.getText().toString();
        String height = p_height.getText().toString();
        //item
        String itemName = getIntent().getExtras().getString("large_item_name");
        String item_amount = i_amount.getText().toString();
        //Pickup
        String pickup_name = p_sendername.getText().toString();
        String pickup_number = p_sendernumber.getText().toString();
        String pickup_address = p_senderaddress.getText().toString();
        String pickup_specify_address = p_senderspecifyaddress.getText().toString();
        //Receiver
        String receiver_name = r_receivername.getText().toString();
        String receiver_number = r_receviernumber.getText().toString();
        String receiver_address = r_receiveraddress.getText().toString();
        String receiver_specify_address = r_receiverspecifyaddress.getText().toString();

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject object = new JSONObject();
        JSONObject Package = new JSONObject();
        try {
            object.put("fee", fee);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Package.put("type", "Large");
            Package.put("weight", weight);
            Package.put("length", length);
            Package.put("width", width);
            Package.put("height", height);
            object.put("package", Package);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //items
        JSONObject items = new JSONObject();
        try {
            items.put("name", itemName);
            items.put("description", "All");
            items.put("worth", item_amount);
            items.put("payment", "false");
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(items);
            object.put("items", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //pickup
        JSONObject pickup_info = new JSONObject();
        try {
            pickup_info.put("name", pickup_name);
            pickup_info.put("contact", pickup_number);
            pickup_info.put("address", pickup_address);
            pickup_info.put("addressSpecific", pickup_specify_address);
            object.put("pickup_info", pickup_info);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //delivery
        JSONObject delivery_info = new JSONObject();
        try {
            delivery_info.put("name", receiver_name);
            delivery_info.put("contact", receiver_number);
            delivery_info.put("address", receiver_address);
            delivery_info.put("addressSpecific", receiver_specify_address);
            object.put("delivery_info", delivery_info);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody = object.toString();

        String URL = "https://www.deliveeri.xyz/api/v1/transaction/createOrder";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getApplicationContext(),"Created Successfully",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ReviewLargeDeliveryActivity.this, TransactionActivity.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY",error.toString());
                Toast.makeText(ReviewLargeDeliveryActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + customer_token);
                return headers;


//                Map<String, String> headers = new HashMap<String, String>();
//                headers.put("content-type", "application/x-www-form-urlencoded");
//                headers.put("Bearer Token","eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIzIiwianRpIjoiMTAxOGIyYjcxNzU1ZGRjYTU2MzkzNWIwZTg4NzViNDRlZDYwMDg1OWUyYWY4MDliZjNjMDA2ZDkzMmNkZDNlMjI3NWVlODI5ZTFjZjQ3NjciLCJpYXQiOjE2MTk3NDUwNjYsIm5iZiI6MTYxOTc0NTA2NiwiZXhwIjoxNjUxMjgxMDY2LCJzdWIiOiIzNTciLCJzY29wZXMiOltdfQ.W0KtcPXu92_tSZG5hUBsKYHkuU2UbNW9t0yWqYLZLbdjWEsyvwud9X4B8z4bJM2LxlTVJfGX5XxvwCHeKr8yaXDyPaRyziyfpP0pdr9-JLxMHXqPypCUN1VKHG42WlNQgjqgFGfmA9_hJBmK6bObG9qDbGbhSy5q0LCPfUEtv1GuUeXkZh9sMxUIF_tIInRpC01X4MVnHB8ssWnpdRnLT1WHhU-VS4ny1FfQ4Gprtob8EdaCJUbwH-BI8XxWOBoQLV1EES4fXH6D6lIZ61-JAZb6bPJ-yDZdcvVLE0muv6hyncwy2juVbZCmvcM5XjZJ-JgepkxOv84I6UTUuLxQIa4IH6GV98bf5_G8kof-7dDsMRioKawewRc_XE0ERxx35PVVAfAuO0W7Rd-KMHescA7ptL74KLxzy5g4TYL6nrnFiQJkSntYHl5YNxQ9K2g8XPcTI7zRREmQXKGUNBnZj9ERB4SYCrifWJvjozRQZE4gbapgc1DAIvN5bPuLMbPdbr54L-_oP5vR-P3AorcNGFFvXdNPL_0Y66jgZXxCziN9xtUuouT9bn7EpaCa6VA_uptZv0nSnWCQA-RbTbZp-79q5AxEu6aW7K5yPRlDNhDY8T6Pb8uM_MGFZk8UB_y8QqxnUPQ8u_re04WwBMwLcdZrQ7lVZec-kspYB9PJXuo");
//                return super.getHeaders();
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody(){
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            mRequestBody, "utf-8");
                    return null;
                }
            }
        };

        requestQueue.add(request);
    }

}