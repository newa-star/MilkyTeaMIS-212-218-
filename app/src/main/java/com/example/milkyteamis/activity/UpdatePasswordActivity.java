package com.example.milkyteamis.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

public class UpdatePasswordActivity extends BaseActivity {

    private EditText et_changeuserinfo_new;

    private  EditText et_changeuserinfo_confirmnew;

    private Button bt_changeuserinfo_sure;

    private ImageView iv_toolbar_back;

    private User loginUser;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepasswd);
        loginUser = (User)getIntent().getSerializableExtra("loginUser");
        super.setToolbarAndTitle("修改密码",true);
        iv_toolbar_back = findViewById(R.id.iv_toolbar_back);
        iv_toolbar_back.setVisibility(View.VISIBLE);
        iv_toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initView();
    }
    private void initView(){
        et_changeuserinfo_confirmnew = findViewById(R.id.et_changeuserinfo_confirmnew);
        et_changeuserinfo_new = findViewById(R.id.et_changeuserinfo_new);
        bt_changeuserinfo_sure = findViewById(R.id.bt_changeuserinfo_sure);
        bt_changeuserinfo_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_changeuserinfo_new.getText().toString().equals(et_changeuserinfo_confirmnew.getText().toString())){
                    updatePassword(loginUser,et_changeuserinfo_new.getText().toString());
                }
                else{
                    Toast.makeText(UpdatePasswordActivity.this,"前后输入的新密码不一致，请重新输入",Toast.LENGTH_SHORT).show();
                    et_changeuserinfo_new.clearComposingText();
                    et_changeuserinfo_confirmnew.clearComposingText();
                }
            }
        });
    }

    private void updatePassword(User user,String newPasswd){
        RequestParams params = new RequestParams();
        JsonObject jsonObject = new JsonObject();
        HttpUtils httpUtils = new HttpUtils();
        try{
            jsonObject.addProperty("code",user.getCode());
            jsonObject.addProperty("newPassword",newPasswd);
            Gson gson = new Gson();
            params.setBodyEntity(new StringEntity(gson.toJson(jsonObject),"UTF-8"));
            params.setContentType("application/json");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        httpUtils.send(HttpRequest.HttpMethod.POST, ServerAddress.SERVER_ADDRESS + ServerAddress.UPDATE_PASSWORD, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.i("TAG","------"+responseInfo.result);
                Gson gson = new Gson();
                ResultBean_User resultBean_user = gson.fromJson(responseInfo.result,ResultBean_User.class);
                User newPasswdUser = resultBean_user.getData();
                new MainActivity().setUser(newPasswdUser);
                AlertDialog .Builder builder = new AlertDialog.Builder(UpdatePasswordActivity.this);
                final AlertDialog dialog = builder.create();
                dialog.setTitle("提示");
                TextView textView = new TextView(UpdatePasswordActivity.this);
                textView.setPadding(20,20,20,20);
                textView.setText("修改成功！");
                textView.setTextSize(20);
                dialog.setView(textView);
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                        finish();
                    }
                });
                dialog.show();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.i("失败","------"+s);
                Toast.makeText(UpdatePasswordActivity.this,"网络错误，修改密码失败",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
