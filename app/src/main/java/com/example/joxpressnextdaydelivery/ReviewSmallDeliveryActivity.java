package com.example.joxpressnextdaydelivery;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class ReviewSmallDeliveryActivity extends AppCompatActivity {



    TextView reviewType;
    TextView reviewWeight;
    TextView reviewLength;
    TextView reviewWidth;
    TextView reviewHeight;
    TextView senderinfo;
    TextView senderamount;
    TextView senderaddress;
    TextView reviewsmall_fee;
    TextView reviewsmall_fee_total;
    TextView receivers_fee;
    SharedPreferences pref;
    TextView reviewsmall_item_name,item_name,reviewsmall_item_amount,reviewsmall_sender_info,reviewsmall_sendernumber,reviewsmall_senderaddress,reviewspecific_address,receivername,receivernumber,receiveraddress,receiver_specifyaddress;
    Button review_delivery;
    TextView receivers_payment;
    private int fee_total = 0;
    private static final String KEY_PHONE = "phone";
    private static final String KEY_DATA = "data";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_ITEM_NAME = "item_name";
    //private static final String url="https://www.jogx.ph/api/v1/transaction/createOrder";
    RequestQueue requestQueue;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"WrongViewCast", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_small);
        pref = getSharedPreferences("user_details", MODE_PRIVATE);
        String customer_data = pref.getString(KEY_DATA, "");
        String customer_token = pref.getString(KEY_TOKEN, "");

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        String smallitem_province = getIntent().getExtras().getString("small_sender_province");
        String smallitem_city = getIntent().getExtras().getString("small_sender_city");

        String smallitem_amount = getIntent().getExtras().getString("small_item_amount");
        String item_name = getIntent().getExtras().getString("small_item_name");


        String smallitem_info = getIntent().getExtras().getString("small_sender_info");

        //Sender Info

        String smallitem_number = getIntent().getExtras().getString("small_sender_number");
        String specify_address = getIntent().getExtras().getString("small_specify_address");
        String pickup_address = getIntent().getExtras().getString("small_pickup_address");

        //Receipient Info
        String receiver_name = getIntent().getExtras().getString("receipient_name");
        String receiver_number = getIntent().getExtras().getString("receipient_number");
        String receiver_address = getIntent().getExtras().getString("receipient_address");
        String receiver_specify_address = getIntent().getExtras().getString("receipient_specify_address");
        String cod_payment = getIntent().getExtras().getString("cod_checked");

        if(cod_payment.equals("true")){
            receivers_fee = findViewById(R.id.reviewsmallreceiverfee);
            receivers_fee.setText(smallitem_amount);
            receivers_payment = findViewById(R.id.reviewsmallpayment);
            receivers_payment.setText("✓");
        }


        //Receiver
        receivername = findViewById(R.id.reviewsmalldeliveryname);
        receivernumber = findViewById(R.id.reviewsmalldeliverynumber);
        receiveraddress = findViewById(R.id.reviewsmalldeliverycompleteaddress);
        receiver_specifyaddress = findViewById(R.id.reviewsmalldeliverycompletespecificaddress);

        receivername.setText(receiver_name);
        receivernumber.setText(receiver_number);
        receiveraddress.setText(receiver_address);
        receiver_specifyaddress.setText(receiver_specify_address);


        senderamount = findViewById(R.id.reviewsmallpeso);
        senderamount.setText(smallitem_amount);



        reviewsmall_fee = findViewById(R.id.reviewsmallfee);
        reviewsmall_fee_total = findViewById(R.id.reviewsmalltotalfee);

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



        reviewType.setText("small");
        reviewWeight.setText("3 kg");
        reviewLength.setText("18 cm");
        reviewWidth.setText("12 cm");
        reviewHeight.setText("5 cm");

        //item_cod = findViewById(R.id.reviewsmallpayment);
        //


      getSupportActionBar().setTitle("Review your delivery");
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#fefefe")));

        String city_code = getIntent().getExtras().getString("small_city_code");
      //get fee
        StringRequest request = new StringRequest(Request.Method.GET, "https://www.jogx.ph/ratesAPI/"+city_code, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject obj = new JSONObject(response);
                    int fee_amount = obj.getInt("rate");
                    if(cod_payment.equals("true")){
                        int amount_item = Integer.parseInt(smallitem_amount);
                        int sum = fee_amount + amount_item;
                        reviewsmall_fee.setText(String.valueOf(fee_amount));
                        reviewsmall_fee_total.setText(String.valueOf(sum));
                    }else{
                        fee_total = Integer.parseInt(String.valueOf(fee_amount));
                        reviewsmall_fee.setText(String.valueOf(fee_amount));
                        reviewsmall_fee_total.setText(String.valueOf(fee_amount));
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

        review_delivery = findViewById(R.id.SmallDelivery);
        review_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deliver();
            }
        });

    }

    public void deliver() {
        String customer_token = pref.getString(KEY_TOKEN, "");
        String item_name = getIntent().getExtras().getString("small_item_name");
        String fee = reviewsmall_fee_total.getText().toString();
        String item_total = senderamount.getText().toString();

        String pickup_name = reviewsmall_item_name.getText().toString();
        String pickup_number = reviewsmall_sendernumber.getText().toString();
        String pickup_address = reviewsmall_senderaddress.getText().toString();
        String pickup_specify_address = reviewspecific_address.getText().toString();
        String receipient_name = receivername.getText().toString();
        String receipient_number = receivernumber.getText().toString();
        String receipient_address = receiveraddress.getText().toString();
        String receipient_specifyaddress = receiver_specifyaddress.getText().toString();


        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject object = new JSONObject();
        JSONObject Package = new JSONObject();
        try {
            object.put("fee", fee);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Package.put("type", "Small");
            Package.put("weight", "3");
            Package.put("length", "18");
            Package.put("width", "12");
            Package.put("height", "5");
            object.put("package", Package);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //items
        JSONObject items = new JSONObject();
        try {
            items.put("name", item_name);
            items.put("description", "All");
            items.put("worth", item_total);
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
            delivery_info.put("name", receipient_name);
            delivery_info.put("contact", receipient_number);
            delivery_info.put("address", receipient_address);
            delivery_info.put("addressSpecific", receipient_specifyaddress);
            object.put("delivery_info", delivery_info);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody = object.toString();

        String URL = "https://www.jogx.ph/api/v1/transaction/createOrder";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY",error.toString());
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