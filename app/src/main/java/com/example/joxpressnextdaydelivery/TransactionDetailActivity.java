package com.example.joxpressnextdaydelivery;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TransactionDetailActivity extends AppCompatActivity {

    //ListView lv;
    TextView track_code,track_fee,track_status;
    TextView track_type,track_weight,track_length,track_wdith,track_height;
    TextView track_item_name,track_item_description,track_item_amount;
    TextView track_pickup_name,track_pickup_number,track_pickup_address,track_pickup_specify;
    TextView track_delivery_name,track_delivery_number,track_delivery_address,track_delivery_specify;
    SharedPreferences pref;
    LinearLayout transaction_empty;
    private static final String KEY_PHONE = "phone";
    private static final String KEY_DATA = "data";
    private static final String KEY_TOKEN = "token";
    private static final String url="https://www.jogx.ph/api/v1/user/login";
//    ArrayList<TransactionList> list = new ArrayList<>();
//    TransactionAdapter adapter;
    private RequestQueue requestQueue;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);
        pref = getSharedPreferences("user_details", MODE_PRIVATE);
        String customer_token = pref.getString(KEY_TOKEN, "");
        String customer_data = pref.getString(KEY_DATA, "");
        //String tracking_code = pref.getString("track_code", "");
        String tracking_code = getIntent().getExtras().getString("track_code");

        //feee and status
        track_code = findViewById(R.id.transaction_tracking_code);
        track_fee = findViewById(R.id.transaction_tracking_fee);
        track_status = findViewById(R.id.transaction_tracking_status);

        //set text sa fee and status
        track_code.setText(tracking_code);

        //Package
        track_type = findViewById(R.id.transaction_package_type);
        track_weight = findViewById(R.id.transaction_package_weight);
        track_length = findViewById(R.id.transaction_package_length);
        track_wdith = findViewById(R.id.transaction_package_width);
        track_height = findViewById(R.id.transaction_package_height);

        //Items
        track_item_name = findViewById(R.id.transaction_item_name);
        track_item_description = findViewById(R.id.transaction_item_description);
        track_item_amount = findViewById(R.id.transaction_item_amount);

        //Pickup info
        track_pickup_name = findViewById(R.id.transaction_pickup_name);
        track_pickup_number = findViewById(R.id.transaction_pickup_number);
        track_pickup_address = findViewById(R.id.transaction_pickup_address);
        track_pickup_specify = findViewById(R.id.transaction_pickup_specify);

        //Delivery info
        track_delivery_name = findViewById(R.id.transaction_delivery_name);
        track_delivery_number = findViewById(R.id.transaction_delivery_number);
        track_delivery_address = findViewById(R.id.transaction_delivery_address);
        track_delivery_specify = findViewById(R.id.transaction_delivery_specify);

        getSupportActionBar().setTitle("Transaction Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#fefefe")));

        String URL = "https://www.jogx.ph/api/v1/transaction/"+tracking_code;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    //fee and status
                    String transaction_fee = obj.getJSONObject("data").getString("fee");
                    String transaction_status = obj.getJSONObject("data").getString("status");
                    track_fee.setText(transaction_fee);
                    track_status.setText(transaction_status);

                    //package
                    String json_package = obj.getJSONObject("data").getString("package");
                    JSONObject package_json = new JSONObject(json_package);
                    String transaction_type = package_json.getString("type");
                    String transaction_weight = package_json.getString("weight");
                    String transaction_length = package_json.getString("length");
                    String transaction_width = package_json.getString("width");
                    String transaction_height = package_json.getString("height");
                    track_type.setText(transaction_type);
                    track_weight.setText(transaction_weight);
                    track_length.setText(transaction_length);
                    track_wdith.setText(transaction_width);
                    track_height.setText(transaction_height);

                    //Items
                    String json_items = obj.getJSONObject("data").getString("items");
                    JSONArray items_json = new JSONArray(json_items);
                    for(int i=0; i<items_json.length(); i++){
                        JSONObject item = items_json.getJSONObject(i);
                        String transaction_item_name = item.getString("name");
                        String transaction_item_description = item.getString("description");
                        String transaction_item_amount = item.getString("worth");
                        track_item_name.setText(transaction_item_name);
                        track_item_description.setText(transaction_item_description);
                        track_item_amount.setText(transaction_item_amount);

                    }

                    //pickup
                    String json_pickup = obj.getJSONObject("data").getString("pickup_info");
                    JSONObject pickup_json = new JSONObject(json_pickup);
                    String transaction_pickup_name = pickup_json.getString("name");
                    String transaction_pickup_number = pickup_json.getString("contact");
                    String transaction_pickup_address = pickup_json.getString("address");
                    String transaction_pickup_specify = pickup_json.getString("addressSpecific");
                    track_pickup_name.setText(transaction_pickup_name);
                    track_pickup_number.setText(transaction_pickup_number);
                    track_pickup_address.setText(transaction_pickup_address);
                    track_pickup_specify.setText(transaction_pickup_specify);

                    //delivery
                    String json_delivery = obj.getJSONObject("data").getString("delivery_info");
                    JSONObject delivery_json = new JSONObject(json_delivery);
                    String transaction_delivery_name = delivery_json.getString("name");
                    String transaction_delivery_number = delivery_json.getString("contact");
                    String transaction_delivery_address = delivery_json.getString("address");
                    String transaction_delivery_specify = delivery_json.getString("addressSpecific");
                    track_delivery_name.setText(transaction_delivery_name);
                    track_delivery_number.setText(transaction_delivery_number);
                    track_delivery_address.setText(transaction_delivery_address);
                    track_delivery_specify.setText(transaction_delivery_specify);

                }
                catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + customer_token);
                return headers;
            }
        };

        Volley.newRequestQueue(TransactionDetailActivity.this).add(stringRequest);
    }
}