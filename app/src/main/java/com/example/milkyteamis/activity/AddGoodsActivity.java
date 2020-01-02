package com.example.milkyteamis.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.milkyteamis.R;
import com.example.milkyteamis.loader.ImageLoader;
import com.example.milkyteamis.model.Good;
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

import static android.content.Intent.ACTION_GET_CONTENT;

public class AddGoodsActivity extends BaseActivity {

    private static final int REQUEST_PICK_IMAGE = 11101;
    private Button bt_add_picture,bt_add_cancle,bt_add_confirm;
    private EditText et_add_goodName,et_add_goodPrice;
    private static final String[] types = {"奶茶","果茶","鲜茶","芝士"};
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private RadioGroup rg_addgood_type;
    private RadioButton rb_addgood_mid,rb_addgood_large;

    //记录商品名称
    private String name = "";
    //记录商品价格
    private double price = 0;
    //记录商品类别
    private int type = 0;
    //记录商品商品规格:中-0，大-1
    private int size;

    //实例一个商品对象表示新建的商品
    private Good good = new Good();

    String realPathFromUri;
    ImageView iv_add_picture;

    private ImageView iv_toolbar_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_good);
        setToolbarAndTitle("添加商品",true);
        spinner = findViewById(R.id.spinner3);
        adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,types);
        spinner.setAdapter(adapter);
        spinner.setVisibility(View.VISIBLE);
        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());

        initView();
        initListener();
    }


    private void initView(){
        bt_add_cancle = findViewById(R.id.bt_add_cancle);
        bt_add_confirm = findViewById(R.id.bt_add_confirm);
        et_add_goodName = findViewById(R.id.et_add_goodname);
        et_add_goodPrice = findViewById(R.id.et_add_goodsprice);
        bt_add_picture = findViewById(R.id.bt_add_picture);
        rg_addgood_type = findViewById(R.id.rg_addgood_type);
        rb_addgood_large = findViewById(R.id.rb_addgood_large);
        rb_addgood_mid = findViewById(R.id.rb_addgood_mid);
        bt_add_picture.setVisibility(View.INVISIBLE);
        iv_toolbar_back = findViewById(R.id.iv_toolbar_back);
        iv_toolbar_back.setVisibility(View.VISIBLE);
        iv_toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        /**
        String[] mPermissionList = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        ActivityCompat.requestPermissions(AddGoodsActivity.this, mPermissionList, 100);
         */
    }

    private void initListener(){
        bt_add_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        bt_add_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    name = et_add_goodName.getText().toString();
                    price = Double.valueOf(et_add_goodPrice.getText().toString());
                    if(size == 0)
                        name = name + "（中）";
                    else if(size == 1)
                        name = name + "（大）";
                    good.setName(name);
                    good.setPrice(price);
                    good.setSize(size);
                    good.setClassfication(type);
                    AddGood(good);
                    finish();
                }
                catch (Exception e){
                    if (name == "（中）" || name == "（大）")
                        Toast.makeText(AddGoodsActivity.this, "请输入新增商品名称！", Toast.LENGTH_SHORT).show();
                    else if (price == 0.0)
                        Toast.makeText(AddGoodsActivity.this, "请输入商品价格！", Toast.LENGTH_SHORT).show();
                }
            }
        });


        rg_addgood_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.rb_addgood_large:
                        size = 1;
                        break;
                    case R.id.rb_addgood_mid:
                        size = 0;
                        break;
                    default:
                        break;
                }
            }
        });
    }


    //根据填写的信息添加商品
    /**
     * 注意：第一次做的时候为了方便测试不佳权限，后面做的时候需要加上只有管理者才能添加商品的权限
     */
    public void AddGood(final Good good){
        RequestParams params = new RequestParams();
        JsonObject json = new JsonObject();
        try{
            json.addProperty("goodName",good.getName());
            json.addProperty("price",good.getPrice());
            json.addProperty("size",good.getSize());
            json.addProperty("classify",good.getClassfication());
            //json.addProperty("picture",good.getPicture());
            Gson gson = new Gson();
            params.setBodyEntity(new StringEntity(gson.toJson(json),"UTF-8"));
            params.setContentType("application/json");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, ServerAddress.SERVER_ADDRESS + ServerAddress.ADD_GOOD, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Toast.makeText(AddGoodsActivity.this,good.getName()+"已添加成功",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(AddGoodsActivity.this,good.getName()+"添加失败",Toast.LENGTH_SHORT).show();

            }
        });
    }



/**
    private void setPhoto() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            startActivityForResult(new Intent(ACTION_GET_CONTENT).setType("image/*"), REQUEST_PICK_IMAGE);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_PICK_IMAGE);
        }
    }*/

/**
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_PICK_IMAGE:
                    if (data != null) {
                        try {
                            realPathFromUri = getRealPathFromUri(this, data.getData());
                            ImageLoader.display(AddGoodsActivity.this, realPathFromUri,iv_add_picture);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
        try {
            if (resultCode == 0)
                this.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                boolean writeExternalStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readExternalStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if (grantResults.length > 0 && writeExternalStorage && readExternalStorage) {
                    setPhoto();
                } else
                    Toast.makeText(this, "请设置必要的权限", Toast.LENGTH_SHORT).show();
                break;
        }
    }*/

    /**
     * 根据URI获取图片绝对路径

    public static String getRealPathFromUri(Context context, Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion >= 19) { // api >= 19
            return getRealPathFromUriAboveApi19(context, uri);
        } else { // api < 19
            return getRealPathFromUriBelowAPI19(context, uri);
        }
    }*/

    /**
     * 适配api19以下(不包括api19),根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null

    private static String getRealPathFromUriBelowAPI19(Context context, Uri uri) {
        return getDataColumn(context, uri, null, null);
    } */

    /**
     * 适配api19及以上,根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null

    @SuppressLint("NewApi")
    private static String getRealPathFromUriAboveApi19(Context context, Uri uri) {
        String filePath = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是document类型的 uri, 则通过document id来进行处理
            String documentId = DocumentsContract.getDocumentId(uri);
            if (isMediaDocument(uri)) { // MediaProvider
                // 使用':'分割
                String id = documentId.split(":")[1];

                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = {id};
                filePath = getDataColumn(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, selectionArgs);
            } else if (isDownloadsDocument(uri)) { // DownloadsProvider
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
                filePath = getDataColumn(context, contentUri, null, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是 content 类型的 Uri
            filePath = getDataColumn(context, uri, null, null);
        } else if ("file".equals(uri.getScheme())) {
            // 如果是 file 类型的 Uri,直接获取图片对应的路径
            filePath = uri.getPath();
        }
        return filePath;
    }*/

    /**
     * 获取数据库表中的 _data 列，即返回Uri对应的文件路径
     *
     * @return

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String path = null;

        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }*/

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is MediaProvider

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }*/

    /**
     * @return Whether the Uri authority is DownloadsProvider

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }*/


    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if(types[i] == "奶茶")
                type = 0;
            else if(types[i]=="果茶")
                type = 1;
            else if(types[i] == "鲜茶")
                type = 2;
            else if(types[i] == "芝士")
                type = 3;
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
}



