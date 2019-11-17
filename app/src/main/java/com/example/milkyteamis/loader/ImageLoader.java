package com.example.milkyteamis.loader;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.provider.SyncStateContract;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.milkyteamis.R;
import com.example.milkyteamis.server.ServerAddress;

import java.io.File;

public class ImageLoader {
    private ImageLoader() {

    }

    private static class SingleHolder {
        private static final ImageLoader INSTANCE = new ImageLoader();
    }

    public static ImageLoader getInstance() {
        return SingleHolder.INSTANCE;
    }


    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        Glide.with(activity)
                .load(Uri.fromFile(new File(path)))
                .override(width, height)
                .centerCrop()
                .skipMemoryCache(true)
                .into(imageView);
    }

    /**
     * 完整的显示地址
     * @param context
     * @param url
     * @param imageView
     */
    public static void display(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.base_ic_place_holder)
                .error(R.drawable.base_ic_place_holder)
                .centerCrop()
                .into(imageView);
    }

    /**
     * 显示服务器图片，由于服务器回传是相对的url
     * 故需要拼接
     * @param context
     * @param relativeUrl
     * @param imageView
     */
    public static void displayServiceImage(Context context, String relativeUrl, ImageView imageView) {
        Glide.with(context)
                .load(ServerAddress.SERVER_ADDRESS + relativeUrl)
                .placeholder(R.drawable.base_ic_place_holder)
                .error(R.drawable.base_ic_place_holder)
                .centerCrop()
                .into(imageView);
    }

    /**
     * 显示本地url
     * @param context
     * @param path
     * @param imageView
     */
    public static void displayFile(Context context, String path, ImageView imageView) {
        Glide.with(context)
                .load(Uri.fromFile(new File(path)))
                .centerCrop()
                .placeholder(R.drawable.base_ic_place_holder)
                .error(R.drawable.base_ic_place_holder)
                .into(imageView);
    }
}
