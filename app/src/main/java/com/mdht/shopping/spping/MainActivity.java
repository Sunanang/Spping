package com.mdht.shopping.spping;


import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private ImageView img;
    //记录用户首次点击返回键的时间
    private long firstTime=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(getSupportActionBar().isShowing()){
            getSupportActionBar().hide();
        }


        webView = findViewById(R.id.web_load);
        img = findViewById(R.id.img_bug);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setAllowFileAccess(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setDomStorageEnabled(true);
        webView.loadUrl("");

        setWebView();
        setWebViewClient();
        setListener();

        setImageUrl("");

    }




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode== KeyEvent.KEYCODE_BACK) {
            if(webView.canGoBack()) {
                webView.goBack();
            } else {
                long secondTime=System.currentTimeMillis();
                if(secondTime-firstTime>2000){
                    Toast.makeText(MainActivity.this,"双击退出应用",Toast.LENGTH_SHORT).show();
                    firstTime=secondTime;
                    return true;
                }else{
                    finish();
                }
            }
        }
        return true;
    }


    public void setWebView(){
        webView.setWebChromeClient(new WebChromeClient() {
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Display display = getWindowManager().getDefaultDisplay();
                // 获取屏幕高度
                int height = display.getHeight();
                Toast toast = Toast.makeText(view.getContext(),message,Toast.LENGTH_SHORT);
                // 这里给了一个1/4屏幕高度的y轴偏移量
                toast.setGravity(Gravity.TOP, 0, height / 4);
                toast.show();
                result.confirm();// 因为没有绑定事件，需要强行confirm,否则页面会变黑显示不了内容。
                return true;
            }

            public boolean onJsBeforeUnload(WebView view, String url,
                                            String message, JsResult result) {
                return super.onJsBeforeUnload(view, url, message, result);

            }
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {

                Display display = getWindowManager().getDefaultDisplay();
                // 获取屏幕高度
                int height = display.getHeight();
                Toast toast = Toast.makeText(view.getContext(),message,Toast.LENGTH_SHORT);
                // 这里给了一个1/4屏幕高度的y轴偏移量
                toast.setGravity(Gravity.TOP, 0, height / 4);
                toast.show();
                return true;
            }

            /**
             * 覆盖默认的window.prompt展示界面，避免title里显示为“：来自file:////”
             */

            public boolean onJsPrompt(WebView view, String url, String message,
                                      String defaultValue, final JsPromptResult result) {

                Display display = getWindowManager().getDefaultDisplay();
                // 获取屏幕高度
                int height = display.getHeight();
                Toast toast = Toast.makeText(view.getContext(),message,Toast.LENGTH_SHORT);
                // 这里给了一个1/4屏幕高度的y轴偏移量
                toast.setGravity(Gravity.TOP, 0, height / 4);
                toast.show();
                return true;
            }
            //结束自定义弹出
        });
    }


    public void setWebViewClient(){
        webView.setWebViewClient(new WebViewClient(){

            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                view.setVisibility(View.GONE);
            }

            @TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
//                view.setVisibility(View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.indexOf("taobao://") != -1 || url.indexOf("tbopen://") != -1){
                    if(ShareUtils.checkPackage(MainActivity.this,"com.taobao.taobao")){
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse(url);
                        intent.setData(content_url);
                        startActivity(intent);
                    }
                }else
                    view.loadUrl(url);

                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
    }

    private void setListener() {
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.setVisibility(View.VISIBLE);
                webView.loadUrl("");

            }
        });
    }


    private void setImageUrl(final String urls){
        final StringBuffer stringBuffer = new StringBuffer();
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    URL url = new URL(urls);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");//使用get方法接收
                    if(connection.getResponseCode() == 200){
                        InputStream inputStream = connection.getInputStream();//得到一个输入流
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTf-8"));
                        String sread = null;
                        while ((sread = bufferedReader.readLine()) != null) {
                            stringBuffer.append(sread);
                        }
                        getJson(stringBuffer.toString());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void getJson(String data){
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JSONObject object = new JSONObject(data);
            String imgname = object.optString("imgname");
            boolean share = ShareUtils.getShare(getApplicationContext(), imgname);
            File file1 = new File(getFilesDir(), "screen.jpg");
            if(share && file1.exists()){

                return;
            }else {
                ShareUtils.setShare(getApplicationContext(),imgname);
                String imgurl = object.optString("imgurl");
                URL url = new URL(imgurl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");//使用get方法接收
                if(connection.getResponseCode() == 200){
                    InputStream inputStream = connection.getInputStream();
                    int temp = 0;
                    byte[] buffer = new byte[1024];
                    while ((temp = inputStream.read(buffer)) !=-1) {
                        outputStream.write(buffer, 0, temp);
                        outputStream.flush();
                    }
                    setUpdataWriteImage(outputStream.toByteArray());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void setUpdataWriteImage(byte[] data){
        File file1 = new File(getFilesDir(), "screen.jpg");
        file1.delete();
        try {
            OutputStream outputStream1 = new FileOutputStream(file1);
            outputStream1.write(data);
            outputStream1.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
