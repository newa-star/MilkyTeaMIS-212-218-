package com.example.milkyteamis.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.milkyteamis.R;
import com.example.milkyteamis.adapter.OrderGoodListViewAdapter;
import com.example.milkyteamis.adapter.OrderListViewAdapter;
import com.example.milkyteamis.model.Good;
import com.example.milkyteamis.model.Order;
import com.example.milkyteamis.model.Orders;
import com.example.milkyteamis.server.ServerAddress;
import com.example.milkyteamis.view.MyListView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.apache.http.entity.StringEntity;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ShopCarActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private Toolbar toolbar;

    private MyListView shopCarListView;

    private OrderGoodListViewAdapter adapter;
    //接受商品详情
    private List<Order> goodList;
    //新建一个订单总体信息
    private Orders order;
    private TextView tv_shopcar_total;

    private ImageView iv_toolbar_back;

    private Button bt_shopcar_confirm;

    private EditText etNum;

    private int selectNum = 1;

    Intent intent = new Intent();
    //标识总价格
    double total = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopcar);
        super.setToolbarAndTitle("购物车",true);
        initView();
        initListView();
    }

    private void initView(){
        goodList = (List<Order>)getIntent().getSerializableExtra("goodlist");
        order = new Orders();
        tv_shopcar_total = findViewById(R.id.tv_shopcar_total);
        for(int i = 0;i<goodList.size();i++)
            total += goodList.get(i).getRealPrice()*goodList.get(i).getSoldNum();
        tv_shopcar_total.setText("结账：￥"+ Double.toString(total));
        iv_toolbar_back = findViewById(R.id.iv_toolbar_back);
        bt_shopcar_confirm = findViewById(R.id.bt_shopcar_confirm);
        iv_toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        bt_shopcar_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //操作用户的id，没做登录前默认是1
                order.setSoldUserId(1);
                order.setOrderGoods(goodList);
                //订单状态，默认是0：0-未付款，1-已付款
                order.setState(0);
                order.setTotal_price(total);
                addOrder(order);

            }
        });

    }

    //获取购物车商品列表
    private void initListView(){
        shopCarListView = (MyListView)findViewById(R.id.shopcar_listview_info);
        adapter = new OrderGoodListViewAdapter(ShopCarActivity.this,goodList);
        shopCarListView.setAdapter(adapter);
        shopCarListView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.shopcar_listview_info:
                showOrderGoodDetail(goodList.get(i),i);
        }
    }

    /**
     * 新增订单需求
     * @param order  新增的订单总信息
     */
    public void addOrder(Orders order){
        RequestParams params = new RequestParams();
        JsonObject jsonObject = new JsonObject();
        try{
            jsonObject.addProperty("userId",order.getSoldUserId());
            jsonObject.addProperty("totalPrice",order.getTotal_price());
            Gson gson = new Gson();
            jsonObject.addProperty("orderGood",gson.toJson(order.getOrderGoods()));
            params.setBodyEntity(new StringEntity(gson.toJson(jsonObject),"UTF-8"));
            params.setContentType("application/json");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, ServerAddress.SERVER_ADDRESS + ServerAddress.ADD_ORDER, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.i("TAG","----"+responseInfo.result);
                System.out.println("成功");
                goodList.clear();
                intent.putExtra("newGoodlist",(Serializable)goodList);
                ShopCarActivity.this.setResult(1,intent);
                AlertDialog.Builder builder = new AlertDialog.Builder(ShopCarActivity.this);
                AlertDialog dialog = builder.create();
                dialog.setTitle("提示");
                dialog.setMessage("成功提交订单，请尽快完成支付");
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                dialog.show();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.i("失败","--------"+s);
                Toast.makeText(ShopCarActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //通过alertdialog显示订单中商品的具体参数
    private void showOrderGoodDetail(final Order orderGood,final int id){
        selectNum = 1;
        AlertDialog.Builder builder = new AlertDialog.Builder(ShopCarActivity.this);
        View view = View.inflate(ShopCarActivity.this,R.layout.alertdialog_layout,null);
        TextView goodsName = view.findViewById(R.id.tv_alert_goodname);
        TextView goodsPrice = view.findViewById(R.id.tv_alert_price);
        TextView goodsType = view.findViewById(R.id.tv_alert_goodstype);
        ImageView addNum = view.findViewById(R.id.iv_alert_add);
        ImageView subNum = view.findViewById(R.id.iv_alert_sub);
        final CheckBox rb_good_pearl = view.findViewById(R.id.rb_good_pearl);
        final CheckBox rb_good_coconuts = view.findViewById(R.id.rb_good_coconuts);
        final RadioButton rb_good_sugar_full = view.findViewById(R.id.rb_good_sugar_full);
        final RadioButton rb_good_sugar_mid = view.findViewById(R.id.rb_good_sugar_mid);
        final RadioButton rb_good_sugar_none = view.findViewById(R.id.rb_good_none);
        final RadioButton rb_good_ice_full = view.findViewById(R.id.rb_good_ice_full);
        final RadioButton rb_good_ice_none = view.findViewById(R.id.rb_good_ice_none);
        final RadioButton rb_good_ice_normal = view.findViewById(R.id.rb_good_ice_normal);
        final RadioButton rb_good_ice_heat = view.findViewById(R.id.rb_good_ice_heat);
        goodsName.setText(orderGood.getGoodName());
        goodsPrice.setText(Double.toString(orderGood.getRealPrice()));
        if(orderGood.getType() == 0)
            goodsType.setText("奶茶");
        else if(orderGood.getType() == 1)
            goodsType.setText("果茶");
        else if(orderGood.getType() == 2)
            goodsType.setText("鲜茶");
        else if(orderGood.getType() == 3)
            goodsType.setText("芝士");
        //椰果设置
        if(orderGood.getIf_add_coconuts() == 1)
            rb_good_coconuts.setChecked(true);
        //珍珠设置
        if(orderGood.getIf_add_pearl() == 1)
            rb_good_pearl.setChecked(true);
        //甜度设置
        if(orderGood.getSugar() == 0)
            rb_good_sugar_full.setChecked(true);
        else if(orderGood.getSugar() == 1)
            rb_good_sugar_mid.setChecked(true);
        else if(orderGood.getSugar() == 2)
            rb_good_sugar_none.setChecked(true);

        //冰量设置
        if(orderGood.getTemperature() == 0)
            rb_good_ice_full.setChecked(true);
        else if(orderGood.getTemperature() == 1)
            rb_good_ice_none.setChecked(true);
        else if(orderGood.getTemperature() == 2)
            rb_good_ice_normal.setChecked(true);
        else if(orderGood.getTemperature() == 3)
            rb_good_ice_heat.setChecked(true);

        //数量设置
        selectNum = orderGood.getSoldNum();
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
                    Toast.makeText(ShopCarActivity.this,"已达最小购买数量",Toast.LENGTH_SHORT).show();
                }
                else {
                    selectNum--;
                    etNum.setText(""+selectNum);
                }
            }
        });
        builder.setTitle("商品详情");
        final AlertDialog dialog = builder.create();
        dialog.setView(view);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                goodList.get(id).setSoldNum(selectNum);

                if(rb_good_coconuts.isChecked())
                    goodList.get(id).setIf_add_coconuts(1);
                else
                    goodList.get(id).setIf_add_coconuts(0);
                if(rb_good_pearl.isChecked())
                    goodList.get(id).setIf_add_pearl(1);
                else
                    goodList.get(id).setIf_add_pearl(0);

                //甜度
                if(rb_good_sugar_full.isChecked())
                    goodList.get(id).setSugar(0);
                else if (rb_good_sugar_mid.isChecked())
                    goodList.get(id).setSugar(1);
                else if(rb_good_sugar_none.isChecked())
                    goodList.get(id).setSugar(2);

                //冰度
                if(rb_good_ice_full.isChecked())
                    goodList.get(id).setTemperature(0);
                else if(rb_good_ice_none.isChecked())
                    goodList.get(id).setTemperature(1);
                else if(rb_good_ice_normal.isChecked())
                    goodList.get(id).setTemperature(2);
                else if(rb_good_ice_heat.isChecked())
                    goodList.get(id).setTemperature(3);

                tv_shopcar_total.setText("结账：￥"+goodList.get(id).getRealPrice()*goodList.get(id).getSoldNum());
                initListView();
                intent.putExtra("newGoodlist",(Serializable)goodList);
                ShopCarActivity.this.setResult(1,intent);
                dialog.dismiss();
            }
        });

        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
