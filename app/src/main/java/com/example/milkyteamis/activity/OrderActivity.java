package com.example.milkyteamis.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.milkyteamis.R;
import com.example.milkyteamis.adapter.OrderGridViewAdapter;
import com.example.milkyteamis.adapter.OrderListViewAdapter;
import com.example.milkyteamis.model.Good;
import com.example.milkyteamis.model.Order;
import com.example.milkyteamis.model.Orders;
import com.example.milkyteamis.bean.ResultBean;
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

import java.io.Serializable;
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

    private int selectNum = 1;

    private EditText etNum;

    private EditText et_main_search;

    Toolbar toolbar;


    private ImageView iv_toolbar_shopcar,iv_toolbar_back;


    //记录商品温度
    private int temperature_detail = 0;

    //记录商品甜度
    private int sugar_detail = 0;
    //记录商品是否加珍珠
    private int pearl = 0;
    //记录商品是否加椰果
    private int coconuts = 0;

    private ArrayAdapter<String> tem_adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_order);
        super.setToolbarAndTitle("收银",true);
        initView();
        getGoodsList();
        initGridView();
    }

    //初始化这个页面的控件
    private void initView(){
        //order_detail_layout = findViewById(R.id.order_detail_layout);
        //order_detail_layout.setOnClickListener(this);
        toolbar = findViewById(R.id.toolbar);
        iv_toolbar_shopcar = findViewById(R.id.iv_toobar_shopcar);
        iv_toolbar_shopcar.setVisibility(View.VISIBLE);
        iv_toolbar_back = findViewById(R.id.iv_toolbar_back);
        et_main_search = findViewById(R.id.et_main_search);
        iv_toolbar_shopcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderActivity.this,ShopCarActivity.class);
                intent.putExtra("goodlist",(Serializable)orderList);
                startActivityForResult(intent,1);
            }
        });
        iv_toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        et_main_search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                InputMethodManager imm = (InputMethodManager) OrderActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(OrderActivity.this.getWindow().getDecorView(),InputMethodManager.SHOW_FORCED);
                searchGoodByKey(et_main_search.getText().toString());
            }
        });
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
                    Toast.makeText(OrderActivity.this,"该分类商品暂时无货",Toast.LENGTH_SHORT).show();

                }
                else {
                    initListView();
                    Toast.makeText(OrderActivity.this,"已成功显示该类别商品",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
                System.out.println(s);
                Toast.makeText(OrderActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     *  按照搜索的关键词搜索相关商品
     * @param key  关键词
     */
    public void searchGoodByKey(final String key){
        RequestParams params = new RequestParams();
        HttpUtils http = new HttpUtils();
        try{
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("key",key);
            Gson gson = new Gson();
            params.setBodyEntity(new StringEntity(gson.toJson(jsonObject), "UTF-8"));
            params.setContentType("application/json");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        http.send(HttpRequest.HttpMethod.POST, ServerAddress.SERVER_ADDRESS + ServerAddress.FIND_GOOD_BY_KEY, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.i("TAG","------"+responseInfo.result);
                Gson gson = new Gson();
                ResultBean resultBean = gson.fromJson(responseInfo.result,ResultBean.class);
                if(resultBean.getData().get(0) == null && key != "")
                    Toast.makeText(OrderActivity.this,"没有找到具有该关键字的商品",Toast.LENGTH_SHORT).show();
                else {
                    InputMethodManager imm = (InputMethodManager)OrderActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(OrderActivity.this.getCurrentFocus().getWindowToken(),0);
                    Toast.makeText(OrderActivity.this,"成功找到以下商品",Toast.LENGTH_SHORT).show();
                    goodlist = resultBean.getData();
                    initListView();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                //if (key != "")
                    //Toast.makeText(OrderActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
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
        /**
        //未完成
        switch (view.getId()){
            case R.id.tv_toobar_shopcar:
                finish();
        }*/
    }


    //通过alertdialog显示具体的商品
    public void showAlertDialog(final Good good){
        selectNum = 1;
        order_good = new Order();
        AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
        View view = View.inflate(OrderActivity.this,R.layout.alertdialog_layout,null);
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
                    Toast.makeText(OrderActivity.this,"已达最小购买数量",Toast.LENGTH_SHORT).show();
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
                order_good.setGoodId(good.getId());
                order_good.setGoodName(good.getName());
                order_good.setType(good.getClassfication());
                order_good.setSoldNum(Integer.valueOf(etNum.getText().toString().trim()));
                order_good.setRealPrice(good.getPrice());
                order_good.setTemperature(temperature_detail);
                if(rb_good_coconuts.isChecked())
                    coconuts = 1;
                if(rb_good_pearl.isChecked())
                    pearl = 1;
                if(rb_good_sugar_full.isChecked())
                    sugar_detail = 0;
                else if (rb_good_sugar_mid.isChecked())
                    sugar_detail = 1;
                else if(rb_good_sugar_none.isChecked())
                    sugar_detail = 2;
                if(rb_good_ice_full.isChecked())
                    temperature_detail = 0;
                else if(rb_good_ice_none.isChecked())
                    temperature_detail = 1;
                else if(rb_good_ice_normal.isChecked())
                    temperature_detail = 2;
                else if(rb_good_ice_heat.isChecked())
                    temperature_detail = 3;
                order_good.setTemperature(temperature_detail);
                order_good.setIf_add_pearl(pearl);
                order_good.setSoldNum(selectNum);
                order_good.setIf_add_coconuts(coconuts);
                order_good.setSugar(sugar_detail);
                orderList.add(order_good);
                Toast.makeText(OrderActivity.this,"已成功添加"+order_good.getGoodName()+"到购物车",Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            try {
                orderList = (List<Order>) data.getExtras().getSerializable("newGoodlist");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
