package com.example.joxpressnextdaydelivery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    Button signup;
    AutoCompleteTextView province,municipality,barangay,barangaycode;
    SharedPreferences prf;
    InputStream is;
    EditText first_name,last_name ,email_address ,phone_number ,customer_password ,customerconfirmpassword ,customerbankname ,customerbankcard ,customerbarangay,barangaycodetext ;

    //province
    ArrayList<String> provinceList = new ArrayList<>(); //List
    ArrayList<String> provinceListId = new ArrayList<>(); //List
    ArrayAdapter<String> provinceAdapter; //Adapter

    //City
    ArrayList<String> cityList = new ArrayList<>(); //List
    ArrayList<String> cityCodeList = new ArrayList<>(); //List
    ArrayAdapter<String> cityAdapter; //Adapter

    //Barangay
    ArrayList<String> barangayList = new ArrayList<>(); //List
    ArrayList<String> barangayCodeList = new ArrayList<>(); //List

    ArrayAdapter<String> barangayAdapter;
    ArrayAdapter<String> barangayCodeAdapter;

    //insert url
    private static final String url="https://www.jogx.ph/api/v1/user/register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        prf = getSharedPreferences("user_details", MODE_PRIVATE);

        province = findViewById(R.id.selectaddress);
        municipality = findViewById(R.id.selectcity);
        barangay = findViewById(R.id.selectbarangay);
        barangaycode = findViewById(R.id.selectbarangaycode);

//        fname = findViewById(R.id.txtfname);
//        lname = findViewById(R.id.txtLname);
//        email = findViewById(R.id.txtemail);
//        phone = findViewById(R.id.txtphone);
//        password = findViewById(R.id.txtpassword);
//        confirmpassword = findViewById(R.id.txconfirmpassword);
//        bankname = findViewById(R.id.txtbankname);
//        bankcard = findViewById(R.id.txtbankcard);
//        barangaycodetext = findViewById(R.id.txtbarangaycode);



        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        signup = findViewById(R.id.register);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        //get Data of province
        try{
//            URL url = new URL("http://192.168.43.118/washmycar/index.php/androidcontroller/get_carwash_station");
            URL url = new URL("https://www.jogx.ph/api/v1/getAllProvince");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream is=conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String s=br.readLine();

            is.close();
            conn.disconnect();

            Log.d("json data", s);
            JSONObject json=new JSONObject(s);
            JSONArray array = json.getJSONArray("data");
            for(int i=0; i<array.length(); i++){
                JSONObject item = array.getJSONObject(i);
                String province_id = item.getString("id");
                String province_code = item.getString("psgcCode");
                String province_desc = item.getString("provDesc");
                String province_regcode = item.getString("regCode");
                String province_citycode = item.getString("provCode");
                provinceList.add(province_desc);
                provinceListId.add(province_citycode);
                //provinceList.add(province_citycode);
                provinceAdapter = new ArrayAdapter<>(RegisterActivity.this, android.R.layout.simple_spinner_item, provinceList);
                provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                province.setAdapter(provinceAdapter);

            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }

        province.setOnItemClickListener(this);
        //end of getData of province

    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String selectedId = provinceListId.get(position);
        try{
//            URL url = new URL("http://192.168.43.118/washmycar/index.php/androidcontroller/get_carwash_station");
            URL url = new URL("https://www.jogx.ph/api/v1/getCitiesById/"+selectedId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream is=conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String s=br.readLine();

            is.close();
            conn.disconnect();

            Log.d("json data", s);
            JSONObject json=new JSONObject(s);
            JSONArray array = json.getJSONArray("data");
            for(int i=0; i<array.length(); i++){
                JSONObject item = array.getJSONObject(i);
                String province_id = item.getString("id");
                String province_code = item.getString("psgcCode");
                String province_desc = item.getString("citymunDesc");
                String province_regcode = item.getString("regDesc");
                String province_citycode = item.getString("provCode");
                String citycode = item.getString("citymunCode");
                cityList.add(province_desc);
                cityCodeList.add(citycode);
                //provinceList.add(province_citycode);
                cityAdapter = new ArrayAdapter<>(RegisterActivity.this, android.R.layout.simple_spinner_item, cityList);
                cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                municipality.setAdapter(cityAdapter);

            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }

        municipality.setOnItemClickListener(this);
        String selectedcitycode = cityCodeList.get(position);



        try{
            URL url = new URL("https://www.jogx.ph/api/v1/getBarangayById/"+selectedcitycode);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream is=conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String s=br.readLine();

            is.close();
            conn.disconnect();

            Log.d("json data", s);
            JSONObject json=new JSONObject(s);
            JSONArray array = json.getJSONArray("data");
            for(int i=0; i<array.length(); i++){
                JSONObject item = array.getJSONObject(i);
                String province_id = item.getString("id");
                String province_code = item.getString("brgyCode");
                String province_desc = item.getString("brgyDesc");
                String province_regcode = item.getString("regCode");
                String province_citycode = item.getString("provCode");
                String citycode = item.getString("citymunCode");
                barangayList.add(province_code);
                barangayCodeList.add(province_code);
                barangayAdapter = new ArrayAdapter<>(RegisterActivity.this, android.R.layout.simple_spinner_item, barangayList);
                barangayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                barangay.setAdapter(barangayAdapter);

            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }

        //barangay.setOnItemClickListener(this);

        //barangaycodetext.setText(barangaycode);



    }

    public void register(){

        first_name = findViewById(R.id.txtfname);
        last_name = findViewById(R.id.txtLname);
        email_address = findViewById(R.id.txtemail);
        phone_number = findViewById(R.id.txtphone);
        customer_password = findViewById(R.id.txtpassword);
        customerconfirmpassword = findViewById(R.id.txconfirmpassword);
        customerbankname = findViewById(R.id.txtbankname);
        customerbankcard = findViewById(R.id.txtbankcard);
        customerbarangay = findViewById(R.id.selectbarangay);
        barangaycodetext = findViewById(R.id.txtbarangaycode);

        barangaycodetext.setText("customer");

        final String type = barangaycodetext.getText().toString();
        final String fname = first_name.getText().toString().trim();
        final String lname = last_name.getText().toString().trim();
        final String email = email_address.getText().toString().trim();
        final String phone = phone_number.getText().toString().trim();
        final String password = customer_password.getText().toString().trim();
        final String password_confirmation = customerconfirmpassword.getText().toString().trim();
        final String address_barangay = barangay.getText().toString().trim();
        final String bank_name = customerbankname.getText().toString().trim();
        final String bank_number = customerbankcard.getText().toString().trim();






        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //response.toString();

                Toast.makeText(RegisterActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param=new HashMap<String,String>();
                param.put("type", "customer");
                param.put("fname", fname);
                param.put("lname", lname);
                param.put("phone", phone);
                param.put("email", email);
                param.put("password", password);
                param.put("password_confirmation", password_confirmation);
                param.put("address_barangay", address_barangay);
                param.put("bank_name", bank_name);
                param.put("bank_number", bank_number);
                return param;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);

    }

}