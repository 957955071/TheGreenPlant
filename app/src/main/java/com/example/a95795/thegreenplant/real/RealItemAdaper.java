package com.example.a95795.thegreenplant.real;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a95795.thegreenplant.R;

import java.util.List;

public class RealItemAdaper extends RecyclerView.Adapter<RealItemAdaper.ViewHoder> {
    private List<RealBean> lists;
    private Context context;
    private LayoutInflater layoutInflater;
    private RealBean rb;

    public RealItemAdaper(Context context,List<RealBean> lists) {
        this.layoutInflater = LayoutInflater.from(context);
        this.lists = lists;
        this.context = context;
    }
    @NonNull
    @Override
    public RealItemAdaper.ViewHoder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        //加载布局
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_real_shopwork,viewGroup,
                false);
        //创建实例
        RealItemAdaper.ViewHoder viewHoder = new RealItemAdaper.ViewHoder(view,viewGroup.getContext(),viewType);
        //返回实例
        return viewHoder;
    }

    @Override
    //绑定数据
    public void onBindViewHolder(@NonNull ViewHoder viewHoder, final int position) {
        rb = lists.get(position);

        viewHoder.tv_tmp.setText(rb.getTmp()+"");
        viewHoder.tv_hum.setText(rb.getHum()+"");
        viewHoder.tv_pm.setText(rb.getPm()+"");
        viewHoder.tv_shopwork_name.setText(rb.getShopWorkName());
//        ImageLoader.getInstance().displayImage("http://192.168.43.26:8080/" + product.getProductPicUrl(), viewHoder.productImage, MyApplication.getLoaderOptions());
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public RealBean getItem(int position) {
        return lists.get(position);
    }

    class ViewHoder extends RecyclerView.ViewHolder{
        TextView tv_pm;
        TextView tv_tmp;
        TextView tv_hum;
        TextView tv_shopwork_name;

        public ViewHoder(@NonNull View itemView, final Context context, int position) {
            super(itemView);
            tv_pm = itemView.findViewById(R.id.tv_pm_value);
            tv_tmp = itemView.findViewById(R.id.tv_tmp_value);
            tv_hum = itemView.findViewById(R.id.tv_hum_value);
            tv_shopwork_name = itemView.findViewById(R.id.tv_real_shopworkname);
        }
    }
}
