package com.example.joxpressnextdaydelivery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;

public class LoginActivity extends AppCompatActivity {

    EditText Loginphone,Loginpassword,customerfname;
    TextView signup;
    Button loginbutton;
    SharedPreferences pref;

    private static final String url="https://www.jogx.ph/api/v1/user/login";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_DATA = "data";
    private static final String KEY_TOKEN = "token";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);



        pref = getSharedPreferences("user_details", MODE_PRIVATE);
        String phonenumber = pref.getString(KEY_PHONE, "");

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        signup = findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                //login();
            }
        });

        loginbutton = findViewById(R.id.btnLogin);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        String customer_name = pref.getString(KEY_DATA, null);
        if(customer_name != null){
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
        }

    }

    public void login(){
        Loginphone = findViewById(R.id.txtphone);
        Loginpassword = findViewById(R.id.txtpassword);


        final String phone = Loginphone.getText().toString();
        final String password = Loginpassword.getText().toString();

        if(!phone.equals("") && !password.equals("")){
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try{
                        JSONObject obj = new JSONObject(response);
                        String customer_name = obj.getJSONObject("data").getString("name");
                        String token = obj.getString("authToken");
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString(KEY_DATA, customer_name);
                        editor.putString(KEY_TOKEN, token);
                        editor.commit();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LoginActivity.this, "Invalid phone number or password", Toast.LENGTH_LONG).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> param=new HashMap<String,String>();
                    param.put("phone", phone);
                    param.put("password", password);

                    return param;
                }
            };

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            queue.add(request);
        }else{
            Toast.makeText(getApplicationContext(), "Phone and Password can't be empty", LENGTH_SHORT).show();
        }




    }
}