package com.example.joxpressnextdaydelivery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TransactionAdapter extends BaseAdapter {

    Context context;
    ArrayList<TransactionList> list;
    LayoutInflater inflater;

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
        TransactionHander handler = null;
        if(convertView==null)
        {
            convertView = inflater.inflate(R.layout.activity_transaction_data, null);
            handler = new TransactionHander();
            handler.tracking_code =  convertView.findViewById(R.id.track_code);
            handler.tracking_fee =  convertView.findViewById(R.id.track_fee);
            convertView.setTag(handler);
        }else

            handler=(TransactionHander) convertView.getTag();
            handler.tracking_code.setText(list.get(position).getTrack_code());
            handler.tracking_fee.setText(list.get(position).getTrack_fee());




        return convertView;
    }

    static class TransactionHander
    {
        TextView tracking_code,tracking_fee;
    }
}
