package com.example.milkyteamis;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.milkyteamis.activity.BaseActivity;
import com.example.milkyteamis.activity.GoodsActivity;
import com.example.milkyteamis.activity.OrderActivity;
import com.example.milkyteamis.activity.OrderInfoActivity;
import com.example.milkyteamis.activity.ReportActivity;
import com.example.milkyteamis.activity.UpdatePasswordActivity;
import com.example.milkyteamis.bean.ResultBean_Msg;
import com.example.milkyteamis.model.User;
import com.example.milkyteamis.server.ServerAddress;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.apache.http.entity.StringEntity;

public class MainActivity extends BaseActivity {

    //启动从登陆界面开始

    //初始化页面控件
    ImageView iv_order,iv_report,iv_chpasswd,iv_goods,iv_ordersInfo,iv_toolbar_back;

    Toolbar toolbar;

    EditText et_oldpassword;

    //记录登陆者身份
    private User user;

    private AlertDialog.Builder builder;

    private AlertDialog dialog ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usermenu);
        toolbar = findViewById(R.id.toolbar);
        super.setToolbarAndTitle("菜单",true);
        builder = new AlertDialog.Builder(MainActivity.this);
        dialog = builder.create();
        user = (User)getIntent().getSerializableExtra("loginUser");
        initView();
        initListener();
    }

    private void initView(){
        iv_order = findViewById(R.id.iv_order);
        iv_chpasswd = findViewById(R.id.iv_chpasswd);
        iv_report = findViewById(R.id.iv_report);
        iv_goods = findViewById(R.id.iv_goods);
        iv_ordersInfo = findViewById(R.id.iv_orderInfo);
        iv_toolbar_back = findViewById(R.id.iv_toolbar_back);
        iv_toolbar_back.setVisibility(View.INVISIBLE);
        et_oldpassword = new EditText(MainActivity.this);

    }

    private void initListener(){
        iv_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, OrderActivity.class);
                startActivity(i);
            }
        });

        iv_ordersInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.getIdentity() == 0){
                Intent i = new Intent(MainActivity.this, OrderInfoActivity.class);
                startActivity(i);
                }
                else if(user.getIdentity() == 1){
                    Toast.makeText(MainActivity.this,"对不起，当前用户没有权限访问此项功能",Toast.LENGTH_SHORT).show();
                }
            }
        });

        iv_goods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.getIdentity() == 0) {
                    Intent i = new Intent(MainActivity.this, GoodsActivity.class);
                    startActivity(i);
                }
                else if(user.getIdentity() == 1){
                    Toast.makeText(MainActivity.this,"对不起，当前用户没有权限访问此项功能",Toast.LENGTH_SHORT).show();
                }
            }
        });
        iv_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.getIdentity() == 0) {
                    Intent intent = new Intent(MainActivity.this, ReportActivity.class);
                    startActivity(intent);
                }
                else if (user.getIdentity() == 1){
                    Toast.makeText(MainActivity.this,"对不起，当前用户没有权限访问此项功能",Toast.LENGTH_SHORT).show();
                }
            }
        });

        iv_chpasswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    et_oldpassword.setPadding(20, 20, 20, 20);
                    et_oldpassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    dialog.setTitle("请输入旧密码");
                    dialog.setView(et_oldpassword);
                    dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            /**
                             if(et_oldpassword.getText().toString() == user.getPassword()){
                             Intent intent = new Intent(MainActivity.this,UpdatePasswordActivity.class);
                             startActivity(intent);
                             }
                             else{

                             }*/
                            Toast.makeText(MainActivity.this,"正在验证",Toast.LENGTH_SHORT).show();
                            String oldpasswd = et_oldpassword.getText().toString();
                            et_oldpassword.setText("");
                            checkPassword(user.getCode(), oldpasswd);
                            dialog.dismiss();

                        }
                    });
                    dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            et_oldpassword.setText("");
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void checkPassword(String code,String oldPassword){
        RequestParams params = new RequestParams();
        JsonObject jsonObject = new JsonObject();
        try{
            jsonObject.addProperty("code",code);
            jsonObject.addProperty("oldPassword",oldPassword);
            Gson gson = new Gson();
            params.setBodyEntity(new StringEntity(gson.toJson(jsonObject),"UTF-8"));
            params.setContentType("application/json");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, ServerAddress.SERVER_ADDRESS + ServerAddress.CHECK_PASSWORD, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Toast.makeText(MainActivity.this,"验证成功",Toast.LENGTH_SHORT).show();
                Gson gson = new Gson();
                ResultBean_Msg resultBean_msg = gson.fromJson(responseInfo.result,ResultBean_Msg.class);
                if(resultBean_msg.getStatus() == 0) {
                    Intent intent = new Intent(MainActivity.this, UpdatePasswordActivity.class);
                    intent.putExtra("loginUser", user);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(MainActivity.this,resultBean_msg.getMsg(),Toast.LENGTH_SHORT).show();
                    et_oldpassword.setText("");
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(MainActivity.this,"密码错误",Toast.LENGTH_SHORT).show();
                et_oldpassword.setText("");
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
                Toast.makeText(MainActivity.this,"正在验证",Toast.LENGTH_SHORT).show();

            }
        });

    }

    public User getLoginUser(){
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
