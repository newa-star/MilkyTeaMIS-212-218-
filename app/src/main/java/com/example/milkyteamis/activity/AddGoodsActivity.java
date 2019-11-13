package com.example.milkyteamis.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.TextView;

import com.example.milkyteamis.R;

public class AddGoodsActivity extends BaseActivity {

    private Button bt_add_picture,bt_add_cancle,bt_add_confirm;
    private TextView tv_add_goodName,tv_add_goodType,tv_add_goodPrice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_good);
        setToolbarAndTitle("添加商品",true);

    }
}
