package com.example.milkyteamis.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.milkyteamis.R;
import com.example.milkyteamis.adapter.OrderDetailListviewAdapter;
import com.example.milkyteamis.adapter.OrderInfoListViewAdapter;
import com.example.milkyteamis.model.Order;
import com.example.milkyteamis.model.Orders;
import com.example.milkyteamis.bean.ResultBean_Order;
import com.example.milkyteamis.bean.ResultBean_Order_Detail;
import com.example.milkyteamis.server.ServerAddress;
import com.example.milkyteamis.view.MyListView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.apache.http.entity.StringEntity;

import java.util.ArrayList;
import java.util.List;

public class OrderInfoActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private Toolbar toolbar;

    private ImageView iv_toolbar_back;

    private List<Orders> orderInfoList = new ArrayList<>();

    private MyListView orderInfoListview;

    private OrderInfoListViewAdapter adapter;

    private OrderDetailListviewAdapter adapter_ordee_detail;

    private MyListView orderDetailListview;

    private List<Order> orderDetailList = new ArrayList<>();

    private TextView orderInfo_id,time,staff;

    private int orderId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);
        super.setToolbarAndTitle("订单信息",true);
        toolbar = findViewById(R.id.toolbar);
        iv_toolbar_back = findViewById(R.id.iv_toolbar_back);
        //time = findViewById(R.id.tv_orderinfo_time);
        //staff = findViewById(R.id.tv_orderinfo_staff);
        iv_toolbar_back.setVisibility(View.VISIBLE);
        iv_toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getOrderList();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.mainuser_listview_info:
                showOrderDetail(orderInfoList.get(i).getId());
                orderId = orderInfoList.get(i).getId();
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
        orderInfoListview.setOnItemClickListener(this);
    }

    //获取订单详情
    public void showOrderDetail(int orderId){
        RequestParams params = new RequestParams();
        HttpUtils http = new HttpUtils();
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("orderId", orderId);
            Gson gson = new Gson();
            params.setBodyEntity(new StringEntity(gson.toJson(jsonObject), "UTF-8"));
            params.setContentType("application/json");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        http.send(HttpRequest.HttpMethod.POST, ServerAddress.SERVER_ADDRESS + ServerAddress.ORDER_DETAIL, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.i("TAG","------"+responseInfo.result);
                Gson gson = new Gson();
                ResultBean_Order_Detail resultBean_order_detail = gson.fromJson(responseInfo.result,ResultBean_Order_Detail.class);
                orderDetailList = resultBean_order_detail.getData();
                /**
                 * 通过alertdialog展示
                 */
                ShowOrdersDetail_By_AlertDialog(orderDetailList);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                System.out.println("失败");
                Toast.makeText(OrderInfoActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ShowOrdersDetail_By_AlertDialog(List<Order> orderDetailList){
        AlertDialog.Builder builder = new AlertDialog.Builder(OrderInfoActivity.this);
        View view = View.inflate(OrderInfoActivity.this,R.layout.alertdialog_orderinfo_detail,null);
        //看看layout文件的view
        orderInfo_id = view.findViewById(R.id.tv_orderdetail_id);
        orderInfo_id.setText(Integer.toString(orderId));
        orderDetailListview = view.findViewById(R.id.order_listview_detail);
        adapter_ordee_detail = new OrderDetailListviewAdapter(OrderInfoActivity.this,orderDetailList);
        orderDetailListview.setAdapter(adapter_ordee_detail);
        builder.setTitle("订单详情");
        final AlertDialog dialog = builder.create();
        dialog.setView(view);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
