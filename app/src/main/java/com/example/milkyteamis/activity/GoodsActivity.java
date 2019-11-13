package com.example.milkyteamis.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.milkyteamis.R;
import com.example.milkyteamis.adapter.OrderListViewAdapter;
import com.example.milkyteamis.model.Good;
import com.example.milkyteamis.model.ResultBean;
import com.example.milkyteamis.server.ServerAddress;
import com.example.milkyteamis.view.MyListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GoodsActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private MyListView goodsListView;

    private OrderListViewAdapter adapter;

    private List<Good> goodlist = new ArrayList<>();

    android.support.v7.widget.Toolbar toolbar;


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
                    System.out.println("hello");
                }
                return true;
            }
        });
        getGoodsList();

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    //初始化商品列表
    private  void initListView(){
        goodsListView = (MyListView)findViewById(R.id.mainuser_listview_info);
        adapter = new OrderListViewAdapter(GoodsActivity.this,goodlist);
        goodsListView.setAdapter(adapter);
        //暂时删去
        //listView.setOnClickListener(this);
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

    //点击商品进行删除或修改
    public void showGoodsInfo(final Good good){

    }
}
