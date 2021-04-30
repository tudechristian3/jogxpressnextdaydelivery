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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
import java.util.List;

public class OrderActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    TextView Logout,customername;
    SharedPreferences pref;
    LinearLayout smallOrder,LargeOrder,createSmallOrder,createLargeOrder;
    EditText smallWeight,smallLength,smallWidth,smallHeight;
    EditText itemname,itemamount,senderinfo,sendernumber,txtspecificaddress,receiverinfo,receivernumber,receiverspecificaddress;
    RadioButton cod;
    Button smallDeliver,largeDeliver;

    AutoCompleteTextView senderProvince,senderCity,senderBarangay;
    AutoCompleteTextView receiverProvince,receiverCity,receiverBarangay;

    //Sender List
    ArrayList<String> provinceList = new ArrayList<>();
    ArrayList<String> cityList = new ArrayList<>();
    ArrayList<String> barangayList = new ArrayList<>();

    //Sender Adapter
    ArrayAdapter<String> provinceAdapter;
    ArrayAdapter<String> cityAdapter;
    ArrayAdapter<String> barangayAdapter;

    //Sender listcode
    ArrayList<String> provinceListCode = new ArrayList<>(); //province
    ArrayList<String> cityListCode = new ArrayList<>();

    //Receiver List
    ArrayList<String> receiverprovinceList = new ArrayList<>();
    ArrayList<String> receivercityList = new ArrayList<>();
    ArrayList<String> receiverbarangayList = new ArrayList<>();

    //Receiver Adapter
    ArrayAdapter<String> receiverprovinceAdapter;
    ArrayAdapter<String> receivercityAdapter;
    ArrayAdapter<String> receiverbarangayAdapter;

    //Receiver listcode
    ArrayList<String> receiverprovinceListCode = new ArrayList<>(); //province
    ArrayList<String> receivercityListCode = new ArrayList<>();


    List<addressprovinceList> list = new ArrayList<>();
    List<addresscityList> addresscitylist = new ArrayList<>();
    List<addressbarangayList> addressbarangayList = new ArrayList<>();

    RequestQueue requestQueue;


    private static final String KEY_PHONE = "phone";
    private static final String KEY_DATA = "data";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_ITEM_NAME = "item_name";
    private static final String url="https://www.jogx.ph/api/v1/user/login";
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        pref = getSharedPreferences("user_details", MODE_PRIVATE);
        String customer_data = pref.getString(KEY_DATA, "");



        customername = findViewById(R.id.customer_name);
        customername.setText(customer_data);

        getSupportActionBar().setTitle("Create an Order");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#fefefe")));


        requestQueue = Volley.newRequestQueue(this);

        smallOrder = findViewById(R.id.createOrder3kg);
        createSmallOrder = findViewById(R.id.CreateOrdersmall);

        //EditText small order
        smallWeight = findViewById(R.id.txtWeight);
        smallLength = findViewById(R.id.txtLength);
        smallWidth = findViewById(R.id.txtWidth);
        smallHeight = findViewById(R.id.txtHeight);

        smallWeight.setText("3");
        smallLength.setText("18");
        smallWidth.setText("12");
        smallHeight.setText("5");

        itemname = findViewById(R.id.txtItemName);
        itemamount = findViewById(R.id.txtValue);
        cod = findViewById(R.id.small_dimension);

        //EditTextPickup
        senderinfo = findViewById(R.id.txtSenderInformation);
        sendernumber = findViewById(R.id.txtSendersContact);
        senderProvince = findViewById(R.id.selectProvince);
        //Autocomplete
        senderCity = findViewById(R.id.selectcity);
        senderBarangay = findViewById(R.id.selectbarangay);
        txtspecificaddress = findViewById(R.id.txtSpecifiyAddress);

        //EditTextDeliver
        receiverinfo = findViewById(R.id.txtReceiverInformation);
        receivernumber = findViewById(R.id.txtReceiverContact);
        receiverProvince = findViewById(R.id.receiverselectprovince);
        receiverCity = findViewById(R.id.receiverselectcity);
        receiverBarangay = findViewById(R.id.receiverselectbarangay);
        receiverspecificaddress = findViewById(R.id.txtreceiverSpecifiyAddress);





        smallOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == smallOrder) {
                    createSmallOrder.setVisibility(View.VISIBLE);
                    createLargeOrder.setVisibility(View.GONE);
                }
            }
        });

        LargeOrder = findViewById(R.id.createOrder4kg);
        createLargeOrder = findViewById(R.id.CreateLargeOrder);

        LargeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    createLargeOrder.setVisibility(View.VISIBLE);
                    createSmallOrder.setVisibility(View.GONE);
            }
        });

        //Small Delivery Button
        smallDeliver = findViewById(R.id.smallDelivery);
        smallDeliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createSmallOrder = new Intent(OrderActivity.this, ReviewSmallDeliveryActivity.class);
                String item_name = itemname.getText().toString();
                String item_amount = itemamount.getText().toString();
                String sender_info = senderinfo.getText().toString();
                String sender_contact = sendernumber.getText().toString();
                String sender_province = senderProvince.getText().toString();
                String sender_city = senderCity.getText().toString();
                String sender_barangay = senderBarangay.getText().toString();
                String address = sender_barangay + ","+ sender_city + "," + sender_province;
                String sender_specify_address = txtspecificaddress.getText().toString();
                String cityCode = pref.getString("city_code", "");

                //Receiver info
                String receiver_name = receiverinfo.getText().toString();
                String receiver_number = receivernumber.getText().toString();
                String receiver_province = receiverProvince.getText().toString();
                String receiver_city = receiverCity.getText().toString();
                String receiver_barangay = receiverBarangay.getText().toString();
                String receiver_address = receiver_barangay + ","+ receiver_city + "," + receiver_province;
                String receiver_specify_address = receiverspecificaddress.getText().toString();

                createSmallOrder.putExtra("small_item_name", item_name);
                createSmallOrder.putExtra("small_item_amount", item_amount);
                createSmallOrder.putExtra("small_sender_info", sender_info);
                createSmallOrder.putExtra("small_sender_number", sender_contact);
                createSmallOrder.putExtra("small_sender_province", sender_province);
                createSmallOrder.putExtra("small_sender_city", sender_city);
                createSmallOrder.putExtra("small_sender_barangay", sender_barangay);
                createSmallOrder.putExtra("small_pickup_address", address);
                createSmallOrder.putExtra("small_specify_address", sender_specify_address);
                createSmallOrder.putExtra("small_city_code", cityCode);


                //Receiver Data
                createSmallOrder.putExtra("receipient_name", receiver_name);
                createSmallOrder.putExtra("receipient_number", receiver_number);
                createSmallOrder.putExtra("receipient_address", receiver_address);
                createSmallOrder.putExtra("receipient_specify_address", receiver_specify_address);
                startActivity(createSmallOrder);
            }
        });

        //Large Delivery Button
        largeDeliver = findViewById(R.id.LargeDelivery);
        largeDeliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createLargeOrder = new Intent(OrderActivity.this, ReviewLargeDeliveryActivity.class);
                startActivity(createLargeOrder);
            }
        });

        //Sender
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
                ArrayAdapter<addressprovinceList> adapter = new ArrayAdapter<addressprovinceList>(OrderActivity.this, android.R.layout.simple_spinner_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                senderProvince.setAdapter(adapter);
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }

        senderProvince.setOnItemClickListener(this);

        //Receiver
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
                ArrayAdapter<addressprovinceList> adapter = new ArrayAdapter<addressprovinceList>(OrderActivity.this, android.R.layout.simple_spinner_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                receiverProvince.setAdapter(adapter);

            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }

        receiverProvince.setOnItemClickListener(this);



    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        addressprovinceList selecteditem = list.get(position);
        String selectedProvinceCode = selecteditem.getProvCode();

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
                ArrayAdapter<addresscityList> adapter = new ArrayAdapter<addresscityList>(OrderActivity.this, android.R.layout.simple_spinner_item, addresscitylist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                senderCity.setAdapter(adapter);

            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }
        senderCity.setOnItemClickListener(this);
        addresscityList selecteditemcity = addresscitylist.get(position);
        String selectedCityCode = selecteditemcity.getCitymunCode();
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("city_code", selectedCityCode);
        editor.commit();

        try{
            URL url = new URL("https://www.jogx.ph/api/v1/getBarangayById/"+selectedCityCode);
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
                addressbarangayList.add(new addressbarangayList(barangay_id,barangay_code,barangay_desc,barangay_regcode,barangay_citycode,citycode));
                ArrayAdapter<addressbarangayList> adapter = new ArrayAdapter<addressbarangayList>(OrderActivity.this, android.R.layout.simple_spinner_item, addressbarangayList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                senderBarangay.setAdapter(adapter);

            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }

        //Receiver City
//        String receiverselectedProvinceCode = receiverprovinceListCode.get(position);
        addresscityList receiverselecteditemcity = addresscitylist.get(position);
        String receiverselectedCityCode = receiverselecteditemcity.getCitymunCode();
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
                ArrayAdapter<addresscityList> adapter = new ArrayAdapter<addresscityList>(OrderActivity.this, android.R.layout.simple_spinner_item, addresscitylist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                receiverCity.setAdapter(adapter);

            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }

        //Receiver
        receiverCity.setOnItemClickListener(this);
//        String receiverselectedcitycode = receivercityListCode.get(position);
        addresscityList receivercity = addresscitylist.get(position);
        String receiverCityCode = receivercity.getCitymunCode();

        try{
            URL url = new URL("https://www.jogx.ph/api/v1/getBarangayById/"+receiverCityCode);
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
                receiverbarangayList.add(province_desc);
                //barangayCodeList.add(province_code);
                receiverbarangayAdapter = new ArrayAdapter<>(OrderActivity.this, android.R.layout.simple_spinner_item, receiverbarangayList);
                receiverbarangayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                receiverBarangay.setAdapter(receiverbarangayAdapter);

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