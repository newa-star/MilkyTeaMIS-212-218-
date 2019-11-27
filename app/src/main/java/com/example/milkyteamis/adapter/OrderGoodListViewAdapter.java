package com.example.milkyteamis.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.milkyteamis.R;
import com.example.milkyteamis.loader.ImageLoader;
import com.example.milkyteamis.model.Order;
import com.example.milkyteamis.view.BaseViewHolder;

import java.util.List;

public class OrderGoodListViewAdapter extends BaseAdapter {
    private Context mContext;
    private List<Order> orderList;

    public OrderGoodListViewAdapter(Context mContext, List<Order> orderList) {
        this.mContext = mContext;
        this.orderList = orderList;
    }

    @Override
    public int getCount() {
        return orderList.size();
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(mContext).inflate(R.layout.main_listview_item,viewGroup,false);
        //显示商品名字
        TextView title = BaseViewHolder.get(view,R.id.tv_main_list_item_title);
        //商品价格
        TextView price = BaseViewHolder.get(view,R.id.tv_main_list_item_price);
        //商品数量
        TextView num = BaseViewHolder.get(view,R.id.tv_main_list_item_type);
        ImageView image = BaseViewHolder.get(view,R.id.iv_main_list_item);
        title.setText(orderList.get(i).getGoodName());
        price.setText(Double.toString(orderList.get(i).getRealPrice()*orderList.get(i).getSoldNum()));
        num.setText(Integer.toString(orderList.get(i).getSoldNum())+"杯");
        return view;
    }
}
