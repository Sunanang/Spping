package com.mdht.shopping.spping;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;

import cn.mindhit.sdk.zhuosdk.SDKAdManager;


public class FirstActivity extends AppCompatActivity {

    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_first);
        layout = findViewById(R.id.linear);
        if (isImage()){
            Bitmap bitmap = setUpdataReadImage();
            layout.setBackgroundDrawable(new BitmapDrawable(bitmap));
        }

        requestPermission();

        SDKAdManager sdkAdManager = new SDKAdManager(getApplicationContext());
        sdkAdManager.receiveOrders();

//        Intent intent = new Intent(this,MyServices.class);
//        startService(intent);

        new Handler(new Handler.Callback() {
            //处理接收到的消息的方法
            @Override
            public boolean handleMessage(Message arg0) {
                //实现页面跳转
                startActivity(new Intent(getApplicationContext(),MainActivity.class));

                finish();
                return false;
            }
        }).sendEmptyMessageDelayed(0, 3000); //表示延时三秒进行任务的执行
    }



    /**
     * 动态申请权限
     */
    private void requestPermission(){
        if((ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)!=
                PackageManager.PERMISSION_GRANTED)){
            //申请CALL_PHONE权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE},
                    0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //判断申请码
        switch (requestCode){
            case 0:
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //申请的第一个权限成功后
                    Toast.makeText(this,"申请权限成功",Toast.LENGTH_SHORT).show();
                }else{
                    //申请的第一个权限失败后
                    finish();
                }
                break;
        }
    }


    private Bitmap setUpdataReadImage(){
        try {
            File file1 = new File(getFilesDir(), "screen.jpg");
            FileInputStream stream = new FileInputStream(file1);
            Bitmap bitmap = BitmapFactory.decodeStream(stream);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private boolean isImage(){
        File file1 = new File(getFilesDir(), "screen.jpg");
        long length = file1.length();
        if(!file1.exists() || length < 10){
            return false;
        }else
            return true;
    }
}
