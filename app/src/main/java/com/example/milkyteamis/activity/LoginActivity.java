package com.example.milkyteamis.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.milkyteamis.MainActivity;
import com.example.milkyteamis.R;
import com.example.milkyteamis.bean.ResultBean_User;
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

import java.io.Serializable;

public class LoginActivity extends BaseActivity {

    private EditText et_login_account;

    private EditText et_login_password;

    private Button bt_login_login,bt_login_register;

    private User loginUser;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        et_login_account = findViewById(R.id.et_login_account);
        et_login_password = findViewById(R.id.et_login_password);
        bt_login_login = findViewById(R.id.bt_login_login);
        bt_login_register = findViewById(R.id.bt_login_register);
        loginUser = new User();
        bt_login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User parmUser = new User();
                parmUser.setCode(et_login_account.getText().toString());
                parmUser.setPassword(et_login_password.getText().toString());
                Login(parmUser);
            }
        });
        bt_login_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void Login(final User user){
        RequestParams params = new RequestParams();
        HttpUtils http = new HttpUtils();
        JsonObject jsonObject = new JsonObject();
        try{
            jsonObject.addProperty("code",user.getCode());
            jsonObject.addProperty("password",user.getPassword());
            Gson gson = new Gson();
            params.setBodyEntity(new StringEntity(gson.toJson(jsonObject),"UTF-8"));
            params.setContentType("application/json");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        http.send(HttpRequest.HttpMethod.POST, ServerAddress.SERVER_ADDRESS + ServerAddress.LOGIN, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.i("TAG","--------"+responseInfo.result);
                Gson gson = new Gson();
                ResultBean_User resultBean_user = gson.fromJson(responseInfo.result,ResultBean_User.class);
                loginUser = resultBean_user.getData();
                if(loginUser == null){
                    Toast.makeText(LoginActivity.this,"账号或密码错误，请重新登录",Toast.LENGTH_SHORT).show();
                    et_login_account.setText("");
                    et_login_password.setText("");
                }
                else {
                    Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("loginUser",(Serializable)loginUser);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.i("失败","-------"+s);
                Toast.makeText(LoginActivity.this,"网络错误,登录失败",Toast.LENGTH_SHORT).show();

            }
        });
    }
}
