package com.example.milkyteamis.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;

import com.example.milkyteamis.R;
import com.example.milkyteamis.adapter.OrderGridViewAdapter;
import com.example.milkyteamis.adapter.OrderListViewAdapter;
import com.example.milkyteamis.model.Good;
import com.example.milkyteamis.view.MyGridView;
import com.example.milkyteamis.view.MyListView;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends Activity  implements View.OnClickListener, AdapterView.OnItemClickListener {

    private MyGridView gridView;

    private MyListView listView;

    private OrderListViewAdapter adapter;

    private List<Good> goodlist = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_order);
        initGridView();
    }

    //初始化这个页面的控件
    private void initView(){

    }

    //初始化导航菜单
    private void initGridView(){
        gridView = (MyGridView)findViewById(R.id.mainuser_gridview_menu);
        gridView.setAdapter(new OrderGridViewAdapter(this));
        //gridView.setOnClickListener(this);
    }

    //初始化商品列表
    private  void initListView(){
        listView = (MyListView)findViewById(R.id.mainuser_listview_info);
        adapter = new OrderListViewAdapter(OrderActivity.this,goodlist);
        listView.setAdapter(adapter);
        //暂时删去
        //listView.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onClick(View view) {

    }
}
