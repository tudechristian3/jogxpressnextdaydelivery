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
import android.widget.RadioGroup;
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
    RadioButton dimension;
    Button smallDeliver,largeDeliver;
    //Small
    AutoCompleteTextView senderProvince,senderCity,senderBarangay;
    //Small Receiver
    AutoCompleteTextView receiverProvince,receiverCity,receiverBarangay;

    //Large
    AutoCompleteTextView largeSelectProvince,largeSelectCity,largeSelectBarangay;
    //Large Rerceiver
    AutoCompleteTextView largeSelectreceiverProvince,largereceiverSelectCity,largereceiverSelectBarangay;

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

    //Small
    List<addressprovinceList> list = new ArrayList<>();
    List<addresscityList> addresscitylist = new ArrayList<>();
    List<addressbarangayList> addressbarangayList = new ArrayList<>();

    //Large
    List<LargeaddressprovinceList> Largelist = new ArrayList<>();
    List<LargeaddresscityList> Largeaddresscitylist = new ArrayList<>();
    List<LargeaddressbarangayList> LargeaddressbarangayList = new ArrayList<>();

    //EditText Large Pacakge Detail
    EditText largeWeight,largeLength,largeWidth,largeHeight;
    //EditText Large Item Detail
    EditText largeItemName,largeItemAmount;
    //EditText Large Pickup
    EditText largeSenderInfo,largeSenderNumber,largeSenderSpecifyAddress;
    //EditText Large Receiver
    EditText largeReceiverInfo,largeReceiverNumber,largeReceiverSpecifyAddress;
    RadioGroup small_cod,large_cod;

    RadioButton small_payment_cod,large_payment_cod;
    Boolean checked = false;
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

        small_payment_cod = findViewById(R.id.cod_payment);
        large_payment_cod = findViewById(R.id.Largecod);

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
        dimension = findViewById(R.id.small_dimension);

        small_cod = findViewById(R.id.codGroup);

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



        //Large Sender
        largeSelectProvince = findViewById(R.id.Largeselectprovince);
        largeSelectCity = findViewById(R.id.Largeselectcity);
        largeSelectBarangay = findViewById(R.id.Largeselectbarangay);

        //Large Receiver
        largeSelectreceiverProvince = findViewById(R.id.Largereceiverselectprovince);
        largereceiverSelectCity = findViewById(R.id.Largereceiverselectcity);
        largereceiverSelectBarangay = findViewById(R.id.Largereceiverselectbarangay);


        //EditText Large Pacakge Detail
        largeWeight = findViewById(R.id.txtLargeWeight);
        largeLength = findViewById(R.id.txtLargeLength);
        largeWidth = findViewById(R.id.txtLargeWidth);
        largeHeight = findViewById(R.id.txLargeHeight);

        //EditText Large Item Detial
        largeItemName = findViewById(R.id.txtLargeItemName);
        largeItemAmount = findViewById(R.id.txtLargeValue);

        //EditText Large Pickup
        largeSenderInfo = findViewById(R.id.txtLargeSenderInformation);
        largeSenderNumber = findViewById(R.id.txtSendersLargeContact);
        largeSenderSpecifyAddress = findViewById(R.id.LargetxtSpecifiyAddress);

        //EditText Large Receiver
        largeReceiverInfo = findViewById(R.id.LargetxtReceiverInformation);
        largeReceiverNumber = findViewById(R.id.LargetxtReceiverContact);
        largeReceiverSpecifyAddress = findViewById(R.id.LargetxtreceiverSpecifiyAddress);

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
                if(small_payment_cod.isChecked()){
                    String payment = "true";
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("checked", payment);
                    editor.commit();
                }else{
                    String payment = "false";
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("checked", payment);
                    editor.commit();
                }
                if(item_name.equals("") || item_amount.equals("") || sender_info.equals("") || sender_contact.equals("") || sender_province.equals("") || sender_city.equals("") || sender_barangay.equals("")){
                    Toast.makeText(OrderActivity.this, "All Fields are required", Toast.LENGTH_SHORT).show();
                }
                else if(receiver_name.equals("") || receiver_number.equals("") || receiver_province.equals("") || receiver_city.equals("") || receiver_barangay.equals("") || receiver_specify_address.equals("")){
                    Toast.makeText(OrderActivity.this, "All Fields are required", Toast.LENGTH_SHORT).show();
                }
                else if(sender_contact.equals(receiver_number)){
                    Toast.makeText(OrderActivity.this, "Phone number must not equal", Toast.LENGTH_SHORT).show();
                }
                else{
                    String cod_true = pref.getString("checked", "");
                    createSmallOrder.putExtra("cod_checked",cod_true);

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

            }
        });

        //Large Delivery Button
        largeDeliver = findViewById(R.id.LargeDelivery);
        largeDeliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createLargeOrder = new Intent(OrderActivity.this, ReviewLargeDeliveryActivity.class);
                String cityCodeLarge = pref.getString("large_city_code", "");
                String selectProvince = largeSelectProvince.getText().toString();
                String selectCity = largeSelectCity.getText().toString();
                String selectBarangay = largeSelectBarangay.getText().toString();
                String sender_address = selectBarangay + ","+ selectCity + "," + selectProvince;

                String receiverSelectProvince = largeSelectreceiverProvince.getText().toString();
                String receiverSelectCity = largereceiverSelectCity.getText().toString();
                String receiverSelectBarangay = largereceiverSelectBarangay.getText().toString();
                String receiver_large_address = receiverSelectBarangay + ","+ receiverSelectCity + "," + receiverSelectProvince;

                String largeweight = largeWeight.getText().toString();
                String largelength = largeLength.getText().toString();
                String largewidth = largeWidth.getText().toString();
                String largeheight = largeHeight.getText().toString();

                String largeitemname = largeItemName.getText().toString();
                String largeitemamount = largeItemAmount.getText().toString();

                String pickupinfo = largeSenderInfo.getText().toString();
                String pickupnumber = largeSenderNumber.getText().toString();
                String pickupSpecifyAddress = largeSenderSpecifyAddress.getText().toString();

                String receiverinfo = largeReceiverInfo.getText().toString();
                String receivernumber = largeReceiverNumber.getText().toString();
                String receiverSpecifyAddress = largeReceiverSpecifyAddress.getText().toString();
                if(large_payment_cod.isChecked()){
                    String payment_large = "true";
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("checked_large", payment_large);
                    editor.commit();
                }else{
                    String payment_large = "false";
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("checked_large", payment_large);
                    editor.commit();
                }

                if(largeweight.equals("3")){
                    Toast.makeText(OrderActivity.this, "Weight must be greater than 3", Toast.LENGTH_SHORT).show();
                }
                else if(largeweight.equals("") || largelength.equals("") || largewidth.equals("") || largeheight.equals("")){
                    Toast.makeText(OrderActivity.this, "All Fields are required", Toast.LENGTH_SHORT).show();
                }
                else if(largeitemname.equals("") || largeitemamount.equals("")){
                    Toast.makeText(OrderActivity.this, "All Fields are required", Toast.LENGTH_SHORT).show();
                }
                else if(selectProvince.equals("") || selectCity.equals("") || selectProvince.equals("")){
                    Toast.makeText(OrderActivity.this, "All Fields are required", Toast.LENGTH_SHORT).show();
                }
                else if(receiverSelectProvince.equals("") || receiverSelectCity.equals("") || receiverSelectBarangay.equals("")){
                    Toast.makeText(OrderActivity.this, "All Fields are required", Toast.LENGTH_SHORT).show();
                }
                else if(pickupinfo.equals("") || pickupnumber.equals("") || pickupSpecifyAddress.equals("")){
                    Toast.makeText(OrderActivity.this, "All Fields are required", Toast.LENGTH_SHORT).show();
                }
                else if(receiverinfo.equals("") || receivernumber.equals("") || receiverSpecifyAddress.equals("")){
                    Toast.makeText(OrderActivity.this, "All Fields are required", Toast.LENGTH_SHORT).show();
                }
                else if(pickupnumber.equals(receivernumber)){
                    Toast.makeText(OrderActivity.this, "Phone must not be equal", Toast.LENGTH_SHORT).show();
                }
                else{
                    String cod_true = pref.getString("checked_large", "");
                    createLargeOrder.putExtra("cod_checked_large",cod_true);

                    //Package Detail
                    createLargeOrder.putExtra("large_weight", largeweight);
                    createLargeOrder.putExtra("large_length", largelength);
                    createLargeOrder.putExtra("large_width", largewidth);
                    createLargeOrder.putExtra("large_height", largeheight);
                    //Item Detail
                    createLargeOrder.putExtra("large_item_name", largeitemname);
                    createLargeOrder.putExtra("large_item_amount", largeitemamount);
                    //Pickup
                    createLargeOrder.putExtra("large_pickup_info", pickupinfo);
                    createLargeOrder.putExtra("large_pickup_number", pickupnumber);
                    createLargeOrder.putExtra("large_pickup_specify_address", pickupSpecifyAddress);
                    createLargeOrder.putExtra("largesenderaddress", sender_address);
                    //Receiver
                    createLargeOrder.putExtra("large_receiver_info", receiverinfo);
                    createLargeOrder.putExtra("large_receiver_number", receivernumber);
                    createLargeOrder.putExtra("large_receiver_specify_address", receiverSpecifyAddress);
                    createLargeOrder.putExtra("largereceiveraddress", receiver_large_address);
                    createLargeOrder.putExtra("largecitycode", cityCodeLarge);
                    startActivity(createLargeOrder);
                }


            }
        });

        //Large Sender Province
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
                Largelist.add(new LargeaddressprovinceList(province_id,province_code,province_desc,province_regcode,province_citycode));
                ArrayAdapter<LargeaddressprovinceList> adapter = new ArrayAdapter<LargeaddressprovinceList>(OrderActivity.this, android.R.layout.simple_spinner_item, Largelist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                largeSelectProvince.setAdapter(adapter);
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }

        largeSelectProvince.setOnItemClickListener(this);

        //Large Receiver
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
                Largelist.add(new LargeaddressprovinceList(province_id,province_code,province_desc,province_regcode,province_citycode));
                ArrayAdapter<LargeaddressprovinceList> adapter = new ArrayAdapter<LargeaddressprovinceList>(OrderActivity.this, android.R.layout.simple_spinner_item, Largelist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                largeSelectreceiverProvince.setAdapter(adapter);
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }

        largeSelectreceiverProvince.setOnItemClickListener(this);



        //Small Sender
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

        //Small Receiver
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
        //Small
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
        //Small
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

        //Small Receiver City
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

        //Small Receiver
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

        //Large Sender City
        LargeaddressprovinceList selected = Largelist.get(position);
        String LargeSelectedProvinceCode = selected.getProvCode();
        try{
//            URL url = new URL("http://192.168.43.118/washmycar/index.php/androidcontroller/get_carwash_station");
            URL url = new URL("https://www.jogx.ph/api/v1/getCitiesById/"+LargeSelectedProvinceCode);
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
                Largeaddresscitylist.add(new LargeaddresscityList(province_id,province_code,province_desc,province_regcode,province_citycode,citycode));
                ArrayAdapter<LargeaddresscityList> adapter = new ArrayAdapter<LargeaddresscityList>(OrderActivity.this, android.R.layout.simple_spinner_item, Largeaddresscitylist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                largeSelectCity.setAdapter(adapter);

            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }
        largeSelectCity.setOnItemClickListener(this);
        LargeaddresscityList Largereceivercity = Largeaddresscitylist.get(position);
        String largeCityCode = Largereceivercity.getCitymunCode();
        SharedPreferences.Editor editor2 = pref.edit();
        editor2.putString("large_city_code", largeCityCode);
        editor2.commit();
        //Large Sender Barangay
        try{
            URL url = new URL("https://www.jogx.ph/api/v1/getBarangayById/"+largeCityCode);
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
                LargeaddressbarangayList.add(new LargeaddressbarangayList(barangay_id,barangay_code,barangay_desc,barangay_regcode,barangay_citycode,citycode));
                ArrayAdapter<LargeaddressbarangayList> adapter = new ArrayAdapter<LargeaddressbarangayList>(OrderActivity.this, android.R.layout.simple_spinner_item, LargeaddressbarangayList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                largeSelectBarangay.setAdapter(adapter);

            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }

        //Large Receiver City
        try{
//            URL url = new URL("http://192.168.43.118/washmycar/index.php/androidcontroller/get_carwash_station");
            URL url = new URL("https://www.jogx.ph/api/v1/getCitiesById/"+LargeSelectedProvinceCode);
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
                Largeaddresscitylist.add(new LargeaddresscityList(province_id,province_code,province_desc,province_regcode,province_citycode,citycode));
                ArrayAdapter<LargeaddresscityList> adapter = new ArrayAdapter<LargeaddresscityList>(OrderActivity.this, android.R.layout.simple_spinner_item, Largeaddresscitylist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                largereceiverSelectCity .setAdapter(adapter);

            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }

        largereceiverSelectCity.setOnItemClickListener(this);
        LargeaddresscityList large = Largeaddresscitylist.get(position);
        String largeCode = large.getCitymunCode();

        //Large Receiver Barangay
        try{
            URL url = new URL("https://www.jogx.ph/api/v1/getBarangayById/"+largeCode);
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
                LargeaddressbarangayList.add(new LargeaddressbarangayList(barangay_id,barangay_code,barangay_desc,barangay_regcode,barangay_citycode,citycode));
                ArrayAdapter<LargeaddressbarangayList> adapter = new ArrayAdapter<LargeaddressbarangayList>(OrderActivity.this, android.R.layout.simple_spinner_item, LargeaddressbarangayList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                largereceiverSelectBarangay.setAdapter(adapter);

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