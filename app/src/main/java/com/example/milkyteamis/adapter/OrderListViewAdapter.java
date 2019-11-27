package com.example.milkyteamis.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.milkyteamis.R;
import com.example.milkyteamis.activity.OrderActivity;
import com.example.milkyteamis.helper.ImageHelper;
import com.example.milkyteamis.loader.ImageLoader;
import com.example.milkyteamis.model.Good;
import com.example.milkyteamis.server.ServerAddress;
import com.example.milkyteamis.view.BaseViewHolder;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class OrderListViewAdapter extends BaseAdapter {
    private Context mContext;
    private List<Good> goodlist = new ArrayList<>();
    private String[] images;

    public OrderListViewAdapter(Context mContext,List<Good> goodlist){
        super();
        this.mContext = mContext;
        this.goodlist = goodlist;
    }

    @Override
    public int getCount() {
        return goodlist.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertview, ViewGroup parent) {
        convertview = LayoutInflater.from(mContext).inflate(R.layout.main_listview_item,parent,false);
        //显示商品名字
        TextView title = BaseViewHolder.get(convertview,R.id.tv_main_list_item_title);
        //商品价格
        TextView price = BaseViewHolder.get(convertview,R.id.tv_main_list_item_price);
        //商品类别
        TextView type = BaseViewHolder.get(convertview,R.id.tv_main_list_item_type);
        ImageView image = BaseViewHolder.get(convertview,R.id.iv_main_list_item);
        /**
         * 图片显示未完成
         */
        try {
            //设置图片
            FTPClient ftpClient = new FTPClient();
            ftpClient.enterLocalActiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.connect(ServerAddress.FTP_IMAGE);
            ftpClient.setControlEncoding("utf-8");
            FTPFile[] files = ftpClient.listFiles(goodlist.get(i).getPicture());
            InputStream inputStream = ftpClient.retrieveFileStream(goodlist.get(i).getPicture());
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            image.setImageBitmap(bitmap);
        }
            catch (Exception e){
                e.printStackTrace();
            }
            //设置商品名
            title.setText(goodlist.get(i).getName());
            //设置商品类别
            if (goodlist.get(i).getClassfication() == 0)
                type.setText("奶茶");
            else if (goodlist.get(i).getClassfication() == 1)
                type.setText("果茶");
            else if (goodlist.get(i).getClassfication() == 2)
                type.setText("鲜茶");
            else if (goodlist.get(i).getClassfication() == 3)
                type.setText("芝士");
            //设置商品价格
            price.setText(Double.toString(goodlist.get(i).getPrice()) + "元");
        return convertview;
    }
}
