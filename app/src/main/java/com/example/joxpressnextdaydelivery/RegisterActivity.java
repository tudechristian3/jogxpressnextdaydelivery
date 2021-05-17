package com.example.joxpressnextdaydelivery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
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
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    Button signup;
    AutoCompleteTextView province,municipality,barangay,barangaycode;
    SharedPreferences prf;
    InputStream is;
    EditText first_name,last_name ,email_address ,phone_number ,customer_password ,customerconfirmpassword ,customerbankname ,customerbankcard ,customerbarangay,barangaycodetext ;

    ArrayList<addressprovinceList> list = new ArrayList<>();
    ArrayList<addresscityList> addresscitylist = new ArrayList<>();
    ArrayList<addressbarangayList> listBarangay = new ArrayList<>();

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
        String URL = "https://www.jogx.ph/api/v1/getAllProvince";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject json=new JSONObject(response);
                    JSONArray array = json.getJSONArray("data");
                    for(int i=0; i<array.length(); i++){
                        JSONObject item = array.getJSONObject(i);
                        String province_id = item.getString("id");
                        String province_code = item.getString("psgcCode");
                        String province_desc = item.getString("provDesc");
                        String province_regcode = item.getString("regCode");
                        String province_citycode = item.getString("provCode");
                        if(province_desc.equals("CEBU") || province_desc.equals("CAGAYAN") || province_desc.equals("NEGROS OCCIDENTAL") || province_desc.equals("BOHOL")){
                            list.add(new addressprovinceList(province_id,province_code,province_desc,province_regcode,province_citycode));
                            ArrayAdapter<addressprovinceList> adapter = new ArrayAdapter<addressprovinceList>(RegisterActivity.this, android.R.layout.simple_spinner_item, list);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            province.setAdapter(adapter);
                        }


                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Volley.newRequestQueue(RegisterActivity.this).add(stringRequest);
        province.setOnItemClickListener(this);
        //end of getData of province

    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String prov_cagayan = list.get(position).getProvDesc();
        String prov_negros = list.get(position).getProvDesc();
        String prov_bohol = list.get(position).getProvDesc();
        String prov_cebu = list.get(position).getProvDesc();

        String city_cagayan = list.get(position).getProvCode();
        String city_negros = list.get(position).getProvCode();
        String city_bohol = list.get(position).getProvCode();
        String city_cebu = list.get(position).getProvCode();

//        Toast.makeText(this, prov_cagayan, Toast.LENGTH_SHORT).show();

        if(prov_cagayan.equals(prov_cagayan)) {
            //Toast.makeText(this, city_cagayan, Toast.LENGTH_SHORT).show();
            try{
                URL url = new URL("https://www.jogx.ph/api/v1/getCitiesById/"+city_cagayan);
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
                        addresscitylist.add(new addresscityList(province_id,province_code,province_desc,province_regcode,province_citycode,citycode));
                        ArrayAdapter<addresscityList> adapter = new ArrayAdapter<addresscityList>(RegisterActivity.this, android.R.layout.simple_spinner_item, addresscitylist);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        municipality.setAdapter(adapter);
                    }

            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        else if(prov_negros.equals(prov_negros)){
            //Toast.makeText(this, city_negros, Toast.LENGTH_SHORT).show();
            try{
                URL url = new URL("https://www.jogx.ph/api/v1/getCitiesById/"+city_negros);
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
                    addresscitylist.add(new addresscityList(province_id,province_code,province_desc,province_regcode,province_citycode,citycode));
                    ArrayAdapter<addresscityList> adapter = new ArrayAdapter<addresscityList>(RegisterActivity.this, android.R.layout.simple_spinner_item, addresscitylist);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    municipality.setAdapter(adapter);
                }

            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        else if(prov_bohol.equals(prov_bohol)){
            //Toast.makeText(this, city_negros, Toast.LENGTH_SHORT).show();
            try{
                URL url = new URL("https://www.jogx.ph/api/v1/getCitiesById/"+city_bohol);
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
                    addresscitylist.add(new addresscityList(province_id,province_code,province_desc,province_regcode,province_citycode,citycode));
                    ArrayAdapter<addresscityList> adapter = new ArrayAdapter<addresscityList>(RegisterActivity.this, android.R.layout.simple_spinner_item, addresscitylist);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    municipality.setAdapter(adapter);
                }

            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        else if(prov_cebu.equals(prov_cebu)){
            //Toast.makeText(this, city_negros, Toast.LENGTH_SHORT).show();
            try{
                URL url = new URL("https://www.jogx.ph/api/v1/getCitiesById/"+city_cebu);
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
                    addresscitylist.add(new addresscityList(province_id,province_code,province_desc,province_regcode,province_citycode,citycode));
                    ArrayAdapter<addresscityList> adapter = new ArrayAdapter<addresscityList>(RegisterActivity.this, android.R.layout.simple_spinner_item, addresscitylist);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    municipality.setAdapter(adapter);
                }

            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }catch (JSONException e){
                e.printStackTrace();
            }
        }



//
//
//
//            String pro_code = prf.getString("checked_large", "");
//            Toast.makeText(this, pro_code, Toast.LENGTH_SHORT).show();

//        addressprovinceList selectedItem = list.get(3);
//        String selectedProvinceCode = selectedItem.getProvCode();

//        try{
//            URL url = new URL("https://www.jogx.ph/api/v1/getCitiesById/"+pro_code);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            InputStream is=conn.getInputStream();
//            BufferedReader br = new BufferedReader(new InputStreamReader(is));
//            String s=br.readLine();
//
//            is.close();
//            conn.disconnect();
//
//            Log.d("json data", s);
//                JSONObject json=new JSONObject(s);
//                JSONArray array = json.getJSONArray("data");
//                for(int i=0; i<array.length(); i++){
//                    JSONObject item = array.getJSONObject(i);
//                    String province_id = item.getString("id");
//                    String province_code = item.getString("psgcCode");
//                    String province_desc = item.getString("citymunDesc");
//                    String province_regcode = item.getString("regDesc");
//                    String province_citycode = item.getString("provCode");
//                    String citycode = item.getString("citymunCode");
//                    addresscitylist.add(new addresscityList(province_id,province_code,province_desc,province_regcode,province_citycode,citycode));
//                    ArrayAdapter<addresscityList> adapter = new ArrayAdapter<addresscityList>(RegisterActivity.this, android.R.layout.simple_spinner_item, addresscitylist);
//                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    municipality.setAdapter(adapter);
//                }
//
//        }catch (MalformedURLException e){
//            e.printStackTrace();
//        }catch (IOException e){
//            e.printStackTrace();
//        }catch (JSONException e){
//            e.printStackTrace();
//        }
        municipality.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String mun_cagayan = addresscitylist.get(position).getCitymunCode();
                String mun_negros = addresscitylist.get(position).getCitymunCode();
                String mun_bohol = addresscitylist.get(position).getCitymunCode();
                String mun_cebu = addresscitylist.get(position).getCitymunCode();

                String muni_code_cagayan = addresscitylist.get(position).getCitymunCode();
                String muni_code_negros = addresscitylist.get(position).getCitymunCode();
                String muni_code_bohol = addresscitylist.get(position).getCitymunCode();
                String muni_code_cebu = addresscitylist.get(position).getCitymunCode();

                if(mun_cagayan.equals(mun_cagayan)){
                    //Toast.makeText(RegisterActivity.this, muni_code_cagayan, Toast.LENGTH_SHORT).show();
                    try{
                        URL url = new URL("https://www.jogx.ph/api/v1/getBarangayById/"+muni_code_cagayan);
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
                            String barangay_id = item.getString("id");
                            String barangay_code = item.getString("brgyCode");
                            String barangay_desc = item.getString("brgyDesc");
                            String barangay_regcode = item.getString("regCode");
                            String barangay_citycode = item.getString("provCode");
                            String citycode = item.getString("citymunCode");
                            listBarangay.add(new addressbarangayList(barangay_id,barangay_code,barangay_desc,barangay_regcode,barangay_citycode,citycode));
                            ArrayAdapter<addressbarangayList> adapter = new ArrayAdapter<addressbarangayList>(RegisterActivity.this, android.R.layout.simple_spinner_item, listBarangay);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            barangay.setAdapter(adapter);

                        }
                    }catch (MalformedURLException e){
                        e.printStackTrace();
                    }catch (IOException e){
                        e.printStackTrace();
                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                }
                else if(mun_negros.equals(mun_negros)){
                    Toast.makeText(RegisterActivity.this, muni_code_negros, Toast.LENGTH_SHORT).show();
                    try{
                        URL url = new URL("https://www.jogx.ph/api/v1/getBarangayById/"+muni_code_negros);
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
                            String barangay_id = item.getString("id");
                            String barangay_code = item.getString("brgyCode");
                            String barangay_desc = item.getString("brgyDesc");
                            String barangay_regcode = item.getString("regCode");
                            String barangay_citycode = item.getString("provCode");
                            String citycode = item.getString("citymunCode");
                            listBarangay.add(new addressbarangayList(barangay_id,barangay_code,barangay_desc,barangay_regcode,barangay_citycode,citycode));
                            ArrayAdapter<addressbarangayList> adapter = new ArrayAdapter<addressbarangayList>(RegisterActivity.this, android.R.layout.simple_spinner_item, listBarangay);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            barangay.setAdapter(adapter);

                        }
                    }catch (MalformedURLException e){
                        e.printStackTrace();
                    }catch (IOException e){
                        e.printStackTrace();
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                else if(mun_bohol.equals(mun_bohol)){
                    Toast.makeText(RegisterActivity.this, muni_code_bohol, Toast.LENGTH_SHORT).show();
                    try{
                        URL url = new URL("https://www.jogx.ph/api/v1/getBarangayById/"+muni_code_bohol);
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
                            String barangay_id = item.getString("id");
                            String barangay_code = item.getString("brgyCode");
                            String barangay_desc = item.getString("brgyDesc");
                            String barangay_regcode = item.getString("regCode");
                            String barangay_citycode = item.getString("provCode");
                            String citycode = item.getString("citymunCode");
                            listBarangay.add(new addressbarangayList(barangay_id,barangay_code,barangay_desc,barangay_regcode,barangay_citycode,citycode));
                            ArrayAdapter<addressbarangayList> adapter = new ArrayAdapter<addressbarangayList>(RegisterActivity.this, android.R.layout.simple_spinner_item, listBarangay);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            barangay.setAdapter(adapter);

                        }
                    }catch (MalformedURLException e){
                        e.printStackTrace();
                    }catch (IOException e){
                        e.printStackTrace();
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                else if(mun_cebu.equals(muni_code_cebu)){
                    Toast.makeText(RegisterActivity.this, muni_code_cebu, Toast.LENGTH_SHORT).show();
                    try{
                        URL url = new URL("https://www.jogx.ph/api/v1/getBarangayById/"+muni_code_bohol);
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
                            String barangay_id = item.getString("id");
                            String barangay_code = item.getString("brgyCode");
                            String barangay_desc = item.getString("brgyDesc");
                            String barangay_regcode = item.getString("regCode");
                            String barangay_citycode = item.getString("provCode");
                            String citycode = item.getString("citymunCode");
                            listBarangay.add(new addressbarangayList(barangay_id,barangay_code,barangay_desc,barangay_regcode,barangay_citycode,citycode));
                            ArrayAdapter<addressbarangayList> adapter = new ArrayAdapter<addressbarangayList>(RegisterActivity.this, android.R.layout.simple_spinner_item, listBarangay);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            barangay.setAdapter(adapter);

                        }
                    }catch (MalformedURLException e){
                        e.printStackTrace();
                    }catch (IOException e){
                        e.printStackTrace();
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        });


//        municipality.setOnItemClickListener(this);
//        addresscityList selectedCity = addresscitylist.get(position);
//        String cityCode = selectedCity.getCitymunCode();
////        String selectedcitycode = cityCodeList.get(position);
//        try{
//            URL url = new URL("https://www.jogx.ph/api/v1/getBarangayById/"+cityCode);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            InputStream is=conn.getInputStream();
//            BufferedReader br = new BufferedReader(new InputStreamReader(is));
//            String s=br.readLine();
//
//            is.close();
//            conn.disconnect();
//
//            Log.d("json data", s);
//            JSONObject json=new JSONObject(s);
//            JSONArray array = json.getJSONArray("data");
//            for(int i=0; i<array.length(); i++){
//                JSONObject item = array.getJSONObject(i);
//                String barangay_id = item.getString("id");
//                String barangay_code = item.getString("brgyCode");
//                String barangay_desc = item.getString("brgyDesc");
//                String barangay_regcode = item.getString("regCode");
//                String barangay_citycode = item.getString("provCode");
//                String citycode = item.getString("citymunCode");
//                listBarangay.add(new addressbarangayList(barangay_id,barangay_code,barangay_desc,barangay_regcode,barangay_citycode,citycode));
//                ArrayAdapter<addressbarangayList> adapter = new ArrayAdapter<addressbarangayList>(RegisterActivity.this, android.R.layout.simple_spinner_item, listBarangay);
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                barangay.setAdapter(adapter);
//
//            }
//        }catch (MalformedURLException e){
//            e.printStackTrace();
//        }catch (IOException e){
//            e.printStackTrace();
//        }catch (JSONException e){
//            e.printStackTrace();
//        }

        barangay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addressbarangayList selectedBarangay = listBarangay.get(position);
                String barangaycode = selectedBarangay.getBrgyCode();
                SharedPreferences.Editor editor = prf.edit();
                editor.putString("barangaycode", barangaycode);
                editor.commit();
            }
        });
    }


    public void register(){

        String barangayCode = prf.getString("barangaycode", "");
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

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait....");

        if(!fname.equals("") || !lname.equals("") || !email.equals("") || !phone.equals("") || !password.equals("") || !password_confirmation.equals("") || !address_barangay.equals("") || !bank_name.equals("") || !bank_number.equals("")){
            if(!password.equals(password_confirmation)){
                Toast.makeText(this, "Password didn't match", Toast.LENGTH_SHORT).show();
            }else if (fname.equals("") || lname.equals("") || email.equals("") || phone.equals("") || password.equals("") || password_confirmation.equals("") || address_barangay.equals("") || bank_name.equals("") || bank_number.equals("")){
                Toast.makeText(this, "Please fill up other fields", Toast.LENGTH_SHORT).show();
            }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                email_address.setError("Please enter a valid email address");
            }
            else{
                progressDialog.show();
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //response.toString();
                        try {
                            JSONObject obj = new JSONObject(response);
                            String json_package = obj.getString("phone");

                            if(json_package.equals(json_package)){
                                Toast.makeText(RegisterActivity.this, "The phone has already been taken", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            JSONObject obj_email = new JSONObject(response);
                            String json_email = obj_email.getString("email");

                            if(json_email.equals(json_email)){
                                Toast.makeText(RegisterActivity.this, "The email has already been taken", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            JSONObject obj_response = new JSONObject(response);
                            String json_data = obj_response.getString("data");
                            if(json_data.equals(json_data)){
                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "Created Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Invalid input please check again", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> param = new HashMap<String, String>();
                        param.put("type", "customer");
                        param.put("fname", fname);
                        param.put("lname", lname);
                        param.put("phone", phone);
                        param.put("email", email);
                        param.put("password", password);
                        param.put("password_confirmation", password_confirmation);
                        param.put("address_barangay", barangayCode);
                        param.put("bank_name", bank_name);
                        param.put("bank_number", bank_number);
                        return param;
                    }
                };

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                queue.add(request);
            }

        }else{
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
        }

    }

}