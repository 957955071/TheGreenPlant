package com.example.a95795.thegreenplant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.a95795.thegreenplant.R;
import com.example.a95795.thegreenplant.custom.Log;


import java.util.List;

public class LogAdapter extends ArrayAdapter<Log> {
    private int resourceId;
    public List<Log> list;
    //重写适配器
    public LogAdapter(Context context, int textViewResourceId, List<Log> objects) {
        super(context, textViewResourceId, objects);
        this.resourceId = textViewResourceId;
        this.list = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log log = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView textView1 = view.findViewById(R.id.log1);
        TextView textView2 = view.findViewById(R.id.log2);
        TextView textView3 = view.findViewById(R.id.log3);
        TextView textView4 = view.findViewById(R.id.log4);
        textView1.setText(log.getUserId() );
        textView2.setText(log.getUserName() );
        textView3.setText(log.getDate());
        textView4.setText(log.getLog());


        return view;

    }
}
