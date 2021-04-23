package com.example.joxpressnextdaydelivery;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    Button signup;
    AutoCompleteTextView province,municipality,barangay;
    SharedPreferences prf;
    InputStream is;

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
    ArrayAdapter<String> barangayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        prf = getSharedPreferences("user_details", MODE_PRIVATE);

        province = findViewById(R.id.selectaddress);
        municipality = findViewById(R.id.selectcity);
        barangay = findViewById(R.id.selectbarangay);

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

    public void register(){
        Toast.makeText(getApplicationContext(), "Register Button Click", Toast.LENGTH_SHORT).show();
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
//            URL url = new URL("http://192.168.43.118/washmycar/index.php/androidcontroller/get_carwash_station");
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
                barangayList.add(province_desc);
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






    }

}