package com.example.a95795.thegreenplant.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.a95795.thegreenplant.R;
import com.example.a95795.thegreenplant.custom.Machine;
import com.example.a95795.thegreenplant.custom.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WorkshopAdapter extends ArrayAdapter<User> {
    public List<User> list;
    public int workid;
    private int resourceId;

    //重写适配器
    public WorkshopAdapter(Context context, int textViewResourceId, List<User> objects) {
        super(context, textViewResourceId, objects);
        this.resourceId = textViewResourceId;
        this.list = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView textView1 = view.findViewById(R.id.WorkshoptextView2);
        TextView textView2 = view.findViewById(R.id.WorkshoptextView3);
        TextView textView3 = view.findViewById(R.id.WorkshoptextView4);
        TextView textView4 = view.findViewById(R.id.WorkshoptextView5);
        textView1.setText(user.getUserId() );
        textView2.setText(user.getUserName() );
        textView3.setText(user.getUserCall() );
        Date date= user.getUserFirstjob();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        textView4.setText((sdf.format(date)));


        return view;

    }
}