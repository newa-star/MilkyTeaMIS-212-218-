package com.example.milkyteamis.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author: chips
 * @date: 2019/9/3
 * @description:
 **/
public class ImageHelper {
    /**
     * 创建临时文件
     * @param context
     * @return
     * @throws IOException 权限问题
     */
    public static File createImage(Context context) throws IOException {
        String timeStamp = new SimpleDateFormat("yyMMddHHmmss", Locale.getDefault()).format(new Date());
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(timeStamp, ".jpg", storageDir);
    }

    /**
     * 尺寸压缩8倍
     *
     * @param file
     */
    public static void compressBitmapToFile(File file) throws Exception {
        if (file.length() < 1048576) {
            // 小文件，不需要压缩
            return;
        }
        // 长宽压缩8倍
        int ratio = 8;
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        Bitmap result = Bitmap.createBitmap(bitmap.getWidth() / ratio, bitmap.getHeight() / ratio,
                Bitmap.Config.ARGB_8888);
        // 利用canvas重新绘制
        Canvas canvas = new Canvas(result);
        Rect rect = new Rect(0, 0, result.getWidth(), result.getHeight());

        // 绘制压缩图片
        canvas.drawBitmap(bitmap, null, rect, null);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        result.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        FileOutputStream fos = null;
        try {
            // 输出文件
            fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }
}
