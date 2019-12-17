package com.example.milkyteamis.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.milkyteamis.R;
import com.example.milkyteamis.adapter.OrderListViewAdapter;
import com.example.milkyteamis.model.Good;
import com.example.milkyteamis.bean.ResultBean;
import com.example.milkyteamis.bean.ResultBean2;
import com.example.milkyteamis.server.ServerAddress;
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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GoodsActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private MyListView goodsListView;

    private OrderListViewAdapter adapter;

    private List<Good> goodlist = new ArrayList<>();

    private Good updateGood = new Good();


    android.support.v7.widget.Toolbar toolbar;

    private ImageView iv_toolbar_back;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);
        super.setToolbarAndTitle("商品列表",true);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        toolbar.requestLayout();
        toolbar.inflateMenu(R.menu.menu_good_insert);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.menu_new){
                    Intent intent = new Intent(GoodsActivity.this,AddGoodsActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
        iv_toolbar_back = findViewById(R.id.iv_toolbar_back);
        iv_toolbar_back.setVisibility(View.VISIBLE);
        getGoodsList();
        initListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        getGoodsList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getGoodsList();
        initListView();
    }

    @Override
    public void onClick(View view) {
        //未完成
        switch (view.getId()){
            case R.id.order_detail_layout:
                showGoodsInfo(goodlist.get(1));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.mainuser_listview_info:
                showGoodsInfo(goodlist.get(i));
        }
    }

    //初始化商品列表
    private  void initListView(){
        goodsListView = (MyListView)findViewById(R.id.mainuser_listview_info);
        adapter = new OrderListViewAdapter(GoodsActivity.this,goodlist);
        goodsListView.setAdapter(adapter);
        //暂时删去
        goodsListView.setOnItemClickListener(this);
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
                Toast.makeText(GoodsActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //点击商品通过alertdialog进行删除或修改
    public void showGoodsInfo(final Good good){
        AlertDialog.Builder builder = new AlertDialog.Builder(GoodsActivity.this);
        View view  = View.inflate(GoodsActivity.this,R.layout.alertdialog_good_info,null);
        final TextView goodsName = view.findViewById(R.id.tv_alert_goodinfo_goodname);
        final TextView goodsPrice = view.findViewById(R.id.tv_alert_goodinfo_price);
        final TextView goodsType = view.findViewById(R.id.tv_alert_goodinfo_goodstype);
        // 显示图片
        // ImageView

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
        builder.setTitle("请点击相应的商品属性进行修改");
        final AlertDialog dialog = builder.create();
        dialog.setView(view);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确认修改", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                updateGoodInfo(good.getId(),goodsName.getText().toString(),Double.valueOf(goodsPrice.getText().toString()),null);
                //还有分类和图片
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "删除商品", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteGood(good.getId());
            }
        });
        dialog.show();
    }

    /**
     * 更新商品
     * @param id 指定的商品的id
     * @param goodName 修改的商品名
     * @param price  修改的商品价格
     * @param picture  修改的商品图片
     */
    public void updateGoodInfo(int id,String goodName,double price,String picture){
        RequestParams paras = new RequestParams();
        JsonObject jsonObject = new JsonObject();
        try{
            jsonObject.addProperty("id",id);
            jsonObject.addProperty("goodName",goodName);
            jsonObject.addProperty("price",price);
            jsonObject.addProperty("picture",picture);
            Gson gson = new Gson();
            paras.setBodyEntity(new StringEntity(gson.toJson(jsonObject),"UTF-8"));
            paras.setContentType("application/json");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, ServerAddress.SERVER_ADDRESS + ServerAddress.UPDATE_GOODS, paras, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.i("TAG","更新成功-----"+responseInfo.result);
                Gson gson = new Gson();
                ResultBean2 resultBean = gson.fromJson(responseInfo.result,ResultBean2.class);
                updateGood = resultBean.getData();
                Toast.makeText(GoodsActivity.this,"已成功更新为"+updateGood.getName()+",需要重启刷新页面",Toast.LENGTH_SHORT).show();
                goodlist.set(updateGood.getId()-1,updateGood);
                initListView();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                System.out.println(s);
                Toast.makeText(GoodsActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     *
     * @param id  删除的商品的id
     */
    public void deleteGood(final int id){
        RequestParams params = new RequestParams();
        JsonObject jsonObject = new JsonObject();
        try{
            jsonObject.addProperty("id",id);
            Gson gson = new Gson();
            params.setBodyEntity(new StringEntity(gson.toJson(jsonObject),"UTF-8"));
            params.setContentType("application/json");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, ServerAddress.SERVER_ADDRESS + ServerAddress.DELETE_GOOD, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.i("TAG","删除成功-------"+responseInfo.result);
                Toast.makeText(GoodsActivity.this,"已成功删除该商品，需要重启刷新页面",Toast.LENGTH_SHORT).show();
                goodlist.remove(id-1);
                initListView();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(GoodsActivity.this,"删除该商品失败",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
