package com.example.milkyteamis.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.milkyteamis.R;
import com.example.milkyteamis.adapter.OrderGridViewAdapter;
import com.example.milkyteamis.adapter.OrderListViewAdapter;
import com.example.milkyteamis.model.Good;
import com.example.milkyteamis.model.Order;
import com.example.milkyteamis.model.Orders;
import com.example.milkyteamis.model.ResultBean;
import com.example.milkyteamis.server.ServerAddress;
import com.example.milkyteamis.view.MyGridView;
import com.example.milkyteamis.view.MyListView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends BaseActivity  implements View.OnClickListener, AdapterView.OnItemClickListener {

    private MyGridView gridView;

    private MyListView listView;

    private OrderListViewAdapter adapter;

    private List<Good> goodlist = new ArrayList<>();

    //订单—商品
    private Order order_good = new Order();

    //总订单
    private Orders orders = new Orders();

    //订单详情的list
    public static List<Order> orderList = new ArrayList<>();

    //订单的list
    public static List<Orders> ordersList = new ArrayList<>();

    private int selectNum = 1;

    private EditText etNum;

    Toolbar toolbar;

    private LinearLayout order_detail_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_order);
        super.setToolbarAndTitle("收银",true);
        getGoodsList();
        initGridView();
    }

    //初始化这个页面的控件
    private void initView(){
        order_detail_layout = findViewById(R.id.order_detail_layout);
        order_detail_layout.setOnClickListener(this);
        toolbar = findViewById(R.id.toolbar);
    }

    //初始化导航菜单
    private void initGridView(){
        gridView = (MyGridView)findViewById(R.id.mainuser_gridview_menu);
        gridView.setAdapter(new OrderGridViewAdapter(this));
        gridView.setOnItemClickListener(this);
    }

    //初始化商品列表
    private  void initListView(){
        listView = (MyListView)findViewById(R.id.mainuser_listview_info);
        adapter = new OrderListViewAdapter(OrderActivity.this,goodlist);
        listView.setAdapter(adapter);
        //暂时删去
        listView.setOnItemClickListener(this);
    }
    //获取商品列表
    public void getGoodsList(){
        RequestParams params = new RequestParams();
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.GET, ServerAddress.SERVER_ADDRESS + ServerAddress.FIND_ALL_GOODS, null, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.i("TAG","----"+responseInfo.result);
                System.out.println("成功");
                Gson gson =new Gson();
                Type type = new TypeToken<ArrayList<Good>>(){}.getType();
                ResultBean resultBean = gson.fromJson(responseInfo.result,ResultBean.class);
                goodlist = resultBean.getData();
                initListView();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                System.out.println("失败");
                System.out.println(s);
                Toast.makeText(OrderActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //根据商品类别获取商品
    public void getGoodListByType(int classification){
        RequestParams paras = new RequestParams();
        JsonObject json = new JsonObject();
        try {
            json.addProperty("classify", Integer.toString(classification));
            Gson gson3 = new Gson();
            paras.setBodyEntity(new StringEntity(gson3.toJson(json),"UTF-8"));
            paras.setContentType("application/json");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, ServerAddress.SERVER_ADDRESS + ServerAddress.FIND_GOOD_BY_CLASSIFY, paras, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.i("TAG","----"+responseInfo.result);
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<Good>>(){}.getType();
                ResultBean resultBean = gson.fromJson(responseInfo.result,ResultBean.class);
                goodlist = resultBean.getData();
                if(goodlist.size() == 0){
                    Toast.makeText(OrderActivity.this,"该分类商品暂时无货",Toast.LENGTH_SHORT);

                }
                else {
                    initListView();
                    Toast.makeText(OrderActivity.this,"已成功显示该类别商品",Toast.LENGTH_SHORT);
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
                System.out.println(s);

                Toast.makeText(OrderActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.mainuser_gridview_menu:
                if(i == 0){
                    //奶茶类别
                    getGoodListByType(0);
                }
                else if(i == 1){
                    //果茶类别
                    getGoodListByType(1);
                }
                else if(i == 2){
                    //鲜茶类别
                    getGoodListByType(2);
                }
                else if(i == 3){
                    //芝士类别
                    getGoodListByType(3);
                }
                break;
            case  R.id.mainuser_listview_info:
                showAlertDialog(goodlist.get(i));
        }
    }

    @Override
    public void onClick(View view) {
        //未完成
        switch (view.getId()){
            case R.id.order_detail_layout:
                showAlertDialog(goodlist.get(1));
        }
    }


    //通过alertdialog显示具体的商品
    public void showAlertDialog(final Good good){
        selectNum = 1;
        order_good = new Order();
        //orders = new Orders();
        AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
        View view = View.inflate(OrderActivity.this,R.layout.alertdialog_layout,null);
        TextView goodsName = view.findViewById(R.id.tv_alert_goodname);
        TextView goodsPrice = view.findViewById(R.id.tv_alert_price);
        TextView goodsType = view.findViewById(R.id.tv_alert_goodstype);
        ImageView addNum = view.findViewById(R.id.iv_alert_add);
        final ImageView subNum = view.findViewById(R.id.iv_alert_sub);
        etNum = view.findViewById(R.id.et_alert_number);
        etNum.setText(""+selectNum);
        etNum.setClickable(false);
        etNum.setFocusable(false);
        etNum.setFocusableInTouchMode(false);
        addNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * 必须做一个关于库存的判断
                 */
                selectNum++;
                etNum.setText(""+selectNum);
            }
        });
        subNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectNum <= 1){
                    Toast.makeText(OrderActivity.this,"已达最小购买数量",Toast.LENGTH_SHORT);
                }
                else {
                    selectNum--;
                    etNum.setText(""+selectNum);
                }
            }
        });
        //显示商品信息
        goodsName.setText(good.getName());
        goodsPrice.setText(Double.toString(good.getPrice()));
        if(good.getClassfication() == 0)
            goodsType.setText("奶茶");
        else if(good.getClassfication() == 1)
            goodsType.setText("果茶");
        else if(good.getClassfication() == 2)
            goodsType.setText("鲜茶");
        else if(good.getClassfication() == 3)
            goodsType.setText("芝士");
        builder.setTitle("请选择数量");
        final AlertDialog dialog = builder.create();
        dialog.setView(view);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //订单详情条目信息
                order_good.setId(good.getId());
                order_good.setGoodName(good.getName());
                order_good.setSoldNum(Integer.valueOf(etNum.getText().toString().trim()));
                order_good.setSoldPrice(good.getPrice());
                orderList.add(order_good);
                dialog.dismiss();//关闭对话框
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();//关闭对话框
            }
        });
        dialog.show();
    }


}
