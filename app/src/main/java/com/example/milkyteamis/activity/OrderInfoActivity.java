package com.example.milkyteamis.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.milkyteamis.R;
import com.example.milkyteamis.adapter.OrderInfoListViewAdapter;
import com.example.milkyteamis.model.Orders;
import com.example.milkyteamis.model.ResultBean_Order;
import com.example.milkyteamis.server.ServerAddress;
import com.example.milkyteamis.view.MyListView;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

public class OrderInfoActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private Toolbar toolbar;

    private ImageView iv_toolbar_back;

    private List<Orders> orderInfoList = new ArrayList<>();

    private MyListView orderInfoListview;

    private OrderInfoListViewAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);
        super.setToolbarAndTitle("订单信息",true);
        toolbar = findViewById(R.id.toolbar);
        iv_toolbar_back = findViewById(R.id.iv_toolbar_back);
        iv_toolbar_back.setVisibility(View.VISIBLE);
        getOrderList();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.mainuser_listview_info:

        }
    }

    //获取订单信息
    public void getOrderList(){
        RequestParams params = new RequestParams();
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.GET, ServerAddress.SERVER_ADDRESS + ServerAddress.FIND_ALL_ORDER_INFO, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.i("TAG","-----"+responseInfo.result);
                Toast.makeText(OrderInfoActivity.this,"成功",Toast.LENGTH_SHORT).show();
                Gson gson = new Gson();
                ResultBean_Order resultBean_order = gson.fromJson(responseInfo.result,ResultBean_Order.class);
                orderInfoList = resultBean_order.getData();
                initListView();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(OrderInfoActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //初始化订单列表
    private void initListView(){
        orderInfoListview = findViewById(R.id.mainuser_listview_info);
        adapter = new OrderInfoListViewAdapter(OrderInfoActivity.this,orderInfoList);
        orderInfoListview.setAdapter(adapter);
    }

    //获取订单详情
    public void showOrderDetail(int orderId){

    }
}
