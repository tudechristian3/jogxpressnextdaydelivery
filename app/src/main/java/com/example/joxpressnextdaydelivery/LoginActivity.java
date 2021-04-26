package com.example.joxpressnextdaydelivery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
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

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText Loginphone,Loginpassword;
    TextView signup;
    Button loginbutton;
    SharedPreferences pref;

    private static final String url="https://www.jogx.ph/api/v1/user/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        pref = getSharedPreferences("user_details", MODE_PRIVATE);

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
                    Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

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
            Toast.makeText(getApplicationContext(), "Phone and Password can't be empty", Toast.LENGTH_SHORT).show();
        }




    }
}