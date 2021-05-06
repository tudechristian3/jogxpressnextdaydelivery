package com.example.joxpressnextdaydelivery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static androidx.core.content.ContextCompat.startActivity;

public class TransactionAdapter extends BaseAdapter {

    Context context;
    ArrayList<TransactionList> list;
    LayoutInflater inflater;
    SharedPreferences pref;

    public TransactionAdapter(Context context, ArrayList<TransactionList> list) {
        super();
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        TransactionHander handler = null;
        TransactionAdapter.TransactionHander handler = null;
        if(convertView==null)
        {
            convertView = inflater.inflate(R.layout.activity_transaction_data, null);
            handler = new TransactionHander();
            handler.tracking_code =  convertView.findViewById(R.id.track_code);
            handler.tracking_fee =  convertView.findViewById(R.id.track_fee);
            handler.tracking_status =  convertView.findViewById(R.id.track_status);



            convertView.setTag(handler);
        }else

            handler=(TransactionHander) convertView.getTag();
            handler.tracking_code.setText(list.get(position).getTrack_code());
            handler.tracking_fee.setText(list.get(position).getTrack_fee());
            handler.tracking_status.setText(list.get(position).getTrack_status());
            Button btnTrack = convertView.findViewById(R.id.btnViewTransaction);
        btnTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tracking_code_intent = new Intent(context, TransactionDetailActivity.class);
                String track_code = list.get(position).getTrack_code();
                tracking_code_intent.putExtra("track_code", track_code);
                context.startActivity(tracking_code_intent);
                //Toast.makeText(context, list.get(position).getTrack_code(), Toast.LENGTH_SHORT).show();
            }
        });


        return convertView;
    }

    static class TransactionHander
    {
        TextView tracking_code,tracking_fee,tracking_status;
        Button btnTrack_code;
    }
}
