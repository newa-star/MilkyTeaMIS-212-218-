package com.example.milkyteamis.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.milkyteamis.R;
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

public class RegisterActivity extends BaseActivity {
    private EditText et_register_code,et_register_name,et_register_passwd,et_register_confirm;
    private RadioGroup rg_register_identity;
    private RadioButton rb_register_normal,rb_register_admin;
    private Button bt_resiter_confirm;
    private User registerUser;
    private int identity = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        super.setToolbarAndTitle("注册",true);
        initView();
        initListener();
    }

    private void initView(){
        et_register_code = findViewById(R.id.et_register_code);
        et_register_confirm = findViewById(R.id.et_register_confirmpasswd);
        et_register_name = findViewById(R.id.et_register_name);
        et_register_passwd = findViewById(R.id.et_register_passwd);
        rg_register_identity = findViewById(R.id.rg_register_identity);
        rb_register_admin = findViewById(R.id.rb_register_admin);
        rb_register_normal = findViewById(R.id.rb_register_normal);
        bt_resiter_confirm = findViewById(R.id.bt_register_confirm);
    }

    private void initListener(){
        bt_resiter_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_register_passwd.getText().toString().equals(et_register_confirm.getText().toString())){
                    registerUser = new User();
                    registerUser.setCode(et_register_code.getText().toString());
                    registerUser.setName(et_register_name.getText().toString());
                    registerUser.setPassword(et_register_passwd.getText().toString());
                    registerUser.setIdentity(identity);
                    register(registerUser);
                }
                else {
                    Toast.makeText(RegisterActivity.this,"前后输入的密码不一致，请重新输入",Toast.LENGTH_SHORT).show();
                    et_register_passwd.setText("");
                    et_register_confirm.setText("");
                }

            }
        });

        rg_register_identity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.rb_register_normal:
                        identity = 1;
                        break;
                    case R.id.rb_register_admin:
                        identity = 0;
                        break;
                }
            }
        });
    }

    private void register(final User registerUser){
        RequestParams params = new RequestParams();
        JsonObject jsonObject = new JsonObject();
        try{
            jsonObject.addProperty("code",registerUser.getCode());
            jsonObject.addProperty("userName",registerUser.getName());
            jsonObject.addProperty("password",registerUser.getPassword());
            jsonObject.addProperty("identity",identity);
            Gson gson = new Gson();
            params.setBodyEntity(new StringEntity(gson.toJson(jsonObject),"UTF-8"));
            params.setContentType("application/json");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, ServerAddress.SERVER_ADDRESS + ServerAddress.REGISTER, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.i("TAG","-------"+responseInfo.result);
                Toast.makeText(RegisterActivity.this,"成功提交注册申请！如果申请普通用户，现在可以直接使用系统；如果申请管理员用户，请等待管理员的进一步审核",Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.i("失败","------"+s);
                Toast.makeText(RegisterActivity.this,"网络错误，注册失败",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
