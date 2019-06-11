package com.example.a95795.thegreenplant.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a95795.thegreenplant.R;
import com.example.a95795.thegreenplant.custom.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BoosWorkshopAdapter extends RecyclerView.Adapter<BoosWorkshopAdapter.ViewHolder> {
    private List<User> muser;




    static class  ViewHolder extends RecyclerView.ViewHolder{
        TextView textView1;
        TextView textView2;
        TextView textView3;
        TextView textView4;
        public ViewHolder(View view){
            super(view);
             textView1 = view.findViewById(R.id.WorkshoptextView2);
             textView2 = view.findViewById(R.id.WorkshoptextView3);
             textView3 = view.findViewById(R.id.WorkshoptextView4);
             textView4 = view.findViewById(R.id.WorkshoptextView5);
        }
    }
    public BoosWorkshopAdapter(List<User> users){
        muser = users;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workshop,parent,false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        User user = muser.get(position);
        holder.textView1.setText(user.getUserId() );
        holder.textView2.setText(user.getUserName() );
        holder.textView3.setText(user.getUserCall() );
        Date date= user.getUserFirstjob();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        holder.textView4.setText((sdf.format(date)));
    }

    @Override
    public int getItemCount() {
        return muser.size();
    }
}
