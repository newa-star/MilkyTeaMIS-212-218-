package com.example.milkyteamis.server;

/**
 * 记录后台服务器url & 喜爱那个挂接口url
 */

public class ServerAddress {
    //服务器地址
    public static String SERVER_ADDRESS = "http://192.168.137.208:8081/milkyteapos/";

    //获取所有商品列表
    public static String FIND_ALL_GOODS = "findAllGood";

    //修改商品信息
    public static String UPDATE_GOODS = "updateGood";

    //获取所有订单信息（id,经手人id，状态，总价格）
    public static String FIND_ALL_ORDER_INFO = "findAllOrderInfo";

    //获取订单详情
    public static String ORDER_DETAIL = "orderDetail";

    //根据用户id获取订单
    public static String FIND_ORDERINFO_BY_USERID = "findOrderInfoByUserId";
}
