package com.example.milkyteamis.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.milkyteamis.R;
import com.example.milkyteamis.model.Order;
import com.example.milkyteamis.view.BaseViewHolder;

import java.util.List;

public class OrderDetailListviewAdapter extends BaseAdapter {
    private Context mContext;
    private List<Order> orderDetailList;

    public OrderDetailListviewAdapter(Context mContext, List<Order> orderDetailList) {
        this.mContext = mContext;
        this.orderDetailList = orderDetailList;
    }

    @Override
    public int getCount() {
        return orderDetailList.size();
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
        //view = LayoutInflater.from(mContext).inflate(R.layout.orderinfo_listview_item,viewGroup,false);
        view = View.inflate(mContext,R.layout.orderinfo_listview_item,null);
        //订单号
        //TextView id = BaseViewHolder.get(view,R.id.tv_orderinfo_id);
        //TextView id = view.findViewById(R.id.tv_orderinfo_id);
        //下单时间
        //TextView time = BaseViewHolder.get(view,R.id.tv_orderinfo_time);
        //经手人
       // TextView staff = BaseViewHolder.get(view,R.id.tv_orderinfo_staff);
        //产品名
        TextView goodName = BaseViewHolder.get(view,R.id.tv_orderinfo_goodname);
        //价格
        TextView price  = BaseViewHolder.get(view,R.id.tv_orderinfo_price);
        //数量
        TextView num = BaseViewHolder.get(view,R.id.tv_orderinfo_num);
        //温度
        TextView tem = BaseViewHolder.get(view,R.id.tv_orderinfo_tem);
        //甜度
        TextView sugar = BaseViewHolder.get(view,R.id.tv_orderinfo_sugar);
        //加珍珠
        TextView pearl = BaseViewHolder.get(view,R.id.tv_orderinfo_pearl);
        //加椰果
        TextView coconut = BaseViewHolder.get(view,R.id.tv_orderinfo_coconut);

        //id.setText(Integer.toString(orderDetailList.get(i).getOrderId()));
        //下单时间
        //经手人
        goodName.setText(orderDetailList.get(i).getGoodName());
        price.setText(Double.toString(orderDetailList.get(i).getRealPrice()));
        num.setText(orderDetailList.get(i).getSoldNum() + "杯");
        switch (orderDetailList.get(i).getTemperature()){
            case 0:
                tem.setText("正常冰");
            case 1:
                tem.setText("去冰");
            case 2:
                tem.setText("常温");
            case 3:
                tem.setText("加热");
        }
        switch (orderDetailList.get(i).getSugar()){
            case 0:
                sugar.setText("正常糖");
            case 1:
                sugar.setText("五分糖");
            case 2:
                sugar.setText("无糖");
        }
        switch (orderDetailList.get(i).getIf_add_pearl()){
            case 0:
                pearl.setText("否");
            case 1:
                pearl.setText("是");
        }
        switch (orderDetailList.get(i).getIf_add_coconuts()){
            case 0 :
                coconut.setText("否");
            case 1:
                coconut.setText("是");
        }
        return view;
    }
}
