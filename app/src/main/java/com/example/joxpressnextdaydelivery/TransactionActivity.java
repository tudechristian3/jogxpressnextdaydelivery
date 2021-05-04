package com.example.joxpressnextdaydelivery;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TransactionActivity extends AppCompatActivity {

    ListView lv;
    TextView track_code,track_fee;
    SharedPreferences pref;
    LinearLayout transaction_empty;
    private static final String KEY_PHONE = "phone";
    private static final String KEY_DATA = "data";
    private static final String KEY_TOKEN = "token";
    private static final String url="https://www.jogx.ph/api/v1/user/login";
    ArrayList<TransactionList> list = new ArrayList<>();
    TransactionAdapter adapter;
    private RequestQueue requestQueue;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        pref = getSharedPreferences("user_details", MODE_PRIVATE);
        String customer_token = pref.getString(KEY_TOKEN, "");
        String customer_data = pref.getString(KEY_DATA, "");
        transaction_empty = findViewById(R.id.transaction_data);

        lv = findViewById(R.id.TransactionList);
        adapter = new TransactionAdapter(this, list);
        lv.setAdapter(adapter);
        lv.setEmptyView(transaction_empty);

        track_code = findViewById(R.id.track_code);
      getSupportActionBar().setTitle("Transactions");
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#fefefe")));
      requestQueue = Volley.newRequestQueue(this);
      String URL = "https://www.jogx.ph/api/v1/transaction/list";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    try{
                        JSONObject json=new JSONObject(response);
                        JSONArray array = json.getJSONArray("data");
                        for(int i=0; i<array.length(); i++){
                            JSONObject item = array.getJSONObject(i);
                            String transaction_id = item.getString("id");
                            String transaction_code = item.getString("tracking_code");
                            String transaction_fee = item.getString("fee");
                            list.add(new TransactionList(transaction_id,transaction_code,transaction_fee));
                            adapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e){
                        e.printStackTrace();
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
        Volley.newRequestQueue(TransactionActivity.this).add(stringRequest);

    }

}