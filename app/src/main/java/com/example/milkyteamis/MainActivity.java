package com.example.milkyteamis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.milkyteamis.activity.OrderActivity;
import com.example.milkyteamis.model.Order;

public class MainActivity extends AppCompatActivity {

    //启动从登陆界面开始

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usermenu);
        setToolbarAndTitle("菜单");
        ImageView iv = findViewById(R.id.imageView9);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, OrderActivity.class);
                startActivity(i);
            }
        });
    }

    protected void setToolbarAndTitle(CharSequence title) {
        ((TextView) findViewById(R.id.tv_toolbar_title)).setText(title);
    }
}
