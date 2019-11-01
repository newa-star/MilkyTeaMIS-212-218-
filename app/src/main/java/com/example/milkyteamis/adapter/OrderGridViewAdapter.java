package com.example.milkyteamis.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.milkyteamis.R;
import com.example.milkyteamis.view.BaseViewHolder;

public class OrderGridViewAdapter extends BaseAdapter {
    private  Context mContext;
    public String[] img_text = {"奶茶","果茶","鲜茶","芝士"};
    public int[] imgs = {R.mipmap.milktea,R.mipmap.fruittea,R.mipmap.juice,R.mipmap.cheese};

    public OrderGridViewAdapter(Context mContext){
        super();
        this.mContext = mContext;
    }
    @Override
    public int getCount() {
        return img_text.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertview, ViewGroup parent) {
        if(convertview == null){
            convertview = LayoutInflater.from(mContext).inflate(R.layout.mainuser_gridview_item,parent,false);
        }
        TextView tv = BaseViewHolder.get(convertview,R.id.tv_mainuser_grid_item);
        ImageView iv = BaseViewHolder.get(convertview,R.id.iv_mainuser_grid_item);
        iv.setBackgroundResource(imgs[i]);
        tv.setText(img_text[i]);
        return convertview;
    }
}
