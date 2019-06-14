package com.example.a95795.thegreenplant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.a95795.thegreenplant.R;
import com.example.a95795.thegreenplant.custom.Feedback;

import java.util.List;

public class FeedbackAdapter extends ArrayAdapter<Feedback> {
    private int resourceId;
    public List<Feedback> list;
    //重写适配器
    public FeedbackAdapter(Context context, int textViewResourceId, List<Feedback> objects) {
        super(context, textViewResourceId, objects);
        this.resourceId = textViewResourceId;
        this.list = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Feedback feedback = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView textView1 = view.findViewById(R.id.feedback1);
        TextView textView2 = view.findViewById(R.id.feedback2);
        TextView textView3 = view.findViewById(R.id.feedback3);
        TextView textView4 = view.findViewById(R.id.feedback4);
        TextView textView5 = view.findViewById(R.id.feedback5);
        textView1.setText(feedback.getId()+"");
        textView2.setText(feedback.getUserId() );
        textView3.setText(feedback.getUserName() );
        textView4.setText(feedback.getDate());
        textView5.setText(feedback.getUserFeedback());


        return view;

    }
}
