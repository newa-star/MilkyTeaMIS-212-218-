package com.example.milkyteamis.util;

import com.example.milkyteamis.activity.BaseActivity;

import java.util.LinkedList;

public class ActivityCollector {

    private LinkedList<BaseActivity> activityLinkedList = new LinkedList<>();

    public void add(BaseActivity activity) {
        activityLinkedList.add(activity);
    }

    public void remove(BaseActivity activity) {
        activityLinkedList.remove(activity);
    }

    /**
     * 退出活动栈的所有实例
     */
    public void finishAll() {
        for (BaseActivity baseActivity : activityLinkedList) {
            baseActivity.finish();
        }
    }


    private static ActivityCollector INSTANCE;

    private ActivityCollector(){
    }

    public static ActivityCollector getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new ActivityCollector();
        }
        return INSTANCE;
    }
}
