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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.TestOnly;
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

public class ProfileActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    EditText customer_fname,customer_lname,customer_email,customer_phone,customer_bank_name,customer_bank_number;
    TextView Logout,customername;
    SharedPreferences pref;
    AutoCompleteTextView province,municipality,barangay;
    List<addressprovinceList> list = new ArrayList<>();
    List<addresscityList> addresscitylist = new ArrayList<>();
    List<addressbarangayList> listBarangay = new ArrayList<>();
    private static final String KEY_PHONE = "phone";
    private static final String KEY_DATA = "data";
    private static final String url="https://www.jogx.ph/api/v1/user/login";
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        pref = getSharedPreferences("user_details", MODE_PRIVATE);
        String customer_info = pref.getString("customer_info", "");
        String customer_email_data = pref.getString("customer_email", "");
        String customer_phone_data = pref.getString("customer_phone", "");


       getSupportActionBar().setTitle("Profile");
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#fefefe")));

        customer_fname = findViewById(R.id.txtprofilefname);
        customer_lname = findViewById(R.id.txtprofileLname);
        customer_email = findViewById(R.id.txtprofileEmail);
        customer_phone = findViewById(R.id.txtprofilephone);
        customer_bank_name = findViewById(R.id.txtbankname);
        customer_bank_number = findViewById(R.id.txtprofilebankcard);

        province = findViewById(R.id.selectprofileaddress);
        municipality = findViewById(R.id.selectprofiletcity);
        barangay = findViewById(R.id.selectprofilebarangay);

        customer_email.setText(customer_email_data);
        customer_phone.setText(customer_phone_data);


        try {
            JSONObject customer_info_data = new JSONObject(customer_info);
            String user_id = customer_info_data.getString("user_id");
            String fname = customer_info_data.getString("fname");
            String lname = customer_info_data.getString("lname");
            String address = customer_info_data.getString("address");
            String bank_name = customer_info_data.getString("bank_name");
            String bank_number = customer_info_data.getString("bank_number");

            customer_fname.setText(fname);
            customer_lname.setText(lname);
            customer_bank_name.setText(bank_name);
            customer_bank_number.setText(bank_number);


            }
            catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }


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
                list.add(new addressprovinceList(province_id,province_code,province_desc,province_regcode,province_citycode));
                ArrayAdapter<addressprovinceList> adapter = new ArrayAdapter<addressprovinceList>(ProfileActivity.this, android.R.layout.simple_spinner_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                province.setAdapter(adapter);

            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }

        province.setOnItemClickListener(this);



    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        addressprovinceList selectedItem = list.get(position);
        String selectedProvinceCode = selectedItem.getProvCode();
        try{
//            URL url = new URL("http://192.168.43.118/washmycar/index.php/androidcontroller/get_carwash_station");
            URL url = new URL("https://www.jogx.ph/api/v1/getCitiesById/"+selectedProvinceCode);
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
                ArrayAdapter<addresscityList> adapter = new ArrayAdapter<addresscityList>(ProfileActivity.this, android.R.layout.simple_spinner_item, addresscitylist);
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

        municipality.setOnItemClickListener(this);
        addresscityList selectedCity = addresscitylist.get(position);
        String cityCode = selectedCity.getCitymunCode();
//        String selectedcitycode = cityCodeList.get(position);
        try{
            URL url = new URL("https://www.jogx.ph/api/v1/getBarangayById/"+cityCode);
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
                ArrayAdapter<addressbarangayList> adapter = new ArrayAdapter<addressbarangayList>(ProfileActivity.this, android.R.layout.simple_spinner_item, listBarangay);
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