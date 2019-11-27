package com.example.milkyteamis.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.milkyteamis.R;
import com.example.milkyteamis.model.Orders;
import com.example.milkyteamis.view.BaseViewHolder;

import java.util.List;

public class OrderInfoListViewAdapter extends BaseAdapter {
    private Context mContext;
    private List<Orders> ordersInfoList;

    public OrderInfoListViewAdapter(Context mContext, List<Orders> ordersInfoList) {
        super();
        this.mContext = mContext;
        this.ordersInfoList = ordersInfoList;
    }

    @Override
    public int getCount() {
        return ordersInfoList.size();
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
    public View getView(int i, View convertview, ViewGroup viewGroup) {
        convertview = LayoutInflater.from(mContext).inflate(R.layout.main_listview_item,viewGroup,false);
        //显示订单号
        TextView title = BaseViewHolder.get(convertview,R.id.tv_main_list_item_title);
        //订单总价格
        TextView price = BaseViewHolder.get(convertview,R.id.tv_main_list_item_price);
        //订单商品数量
        TextView type = BaseViewHolder.get(convertview,R.id.tv_main_list_item_type);
        ImageView image = BaseViewHolder.get(convertview,R.id.iv_main_list_item);

        title.setText("订单"+Integer.toString(ordersInfoList.get(i).getId()));
        price.setText("总价："+Double.toString(ordersInfoList.get(i).getTotal_price()));
        type.setText("经手人："+Integer.toString(ordersInfoList.get(i).getSoldUserId()));
        return convertview;
    }
}
