package com.rust.arslan;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rust.aboutview.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ArslanActivity extends AppCompatActivity {

    private TextView tvConnected;
    private Button ConnectBtn;
    private Button NetSettingBtn;
    private EditText urlText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arslan);
        tvConnected = (TextView) findViewById(R.id.tv_website); /* 显示下载到的数据和异常信息 */
        ConnectBtn = (Button) findViewById(R.id.btn_conn);      /* 点击启动联网 */
        NetSettingBtn = (Button) findViewById(R.id.btn_net_setting);
        urlText = (EditText) findViewById(R.id.et_website);     /* 输入网址 */
        ConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myConnectHandler(v);
            }
        });
        NetSettingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(ArslanActivity.this,SettingsActivity.class);
                startActivity(i);
            }
        });
    }

    public void myConnectHandler(View view) {
        String stringUrl = urlText.getText().toString();/* 必须对输入的网址进行判断 */
        if (stringUrl.equals("")) {/* 未输入网址，弹出提示 */
            Toast.makeText(getApplicationContext(),
                    "Please input website", Toast.LENGTH_SHORT).show();
            return;
        }
        /* 检查网络连接 */
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d("rust", "networkInfo == " + networkInfo.toString());
            /* 网络连接正常，可以联网下载数据 */
            /* 传入url进行下载 */
            new DownloadWebpageText().execute(stringUrl);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Please input website", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 在UI线程之外执行网络操作
     */
    private class DownloadWebpageText extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            Log.d("rust", "DownloadWebpageText doInBackground..." + params[0].toString());
            try {
                return downloadUrl(params[0].toString());/* 开始下载 */
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            tvConnected.setText(o.toString()); /* 显示下载到的数据 */
        }
    }

    /*
     * 给定一个url，建立一个HttpURLConnection连接，取回网页数据流
     */
    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        String inUrl = myurl;
        String head = "http://";/* 自动添加的前缀 */
        int len = 500;/* 只显示前500个字符 */
        try {
            /* 对输入网址判断 */
            if (myurl.length() >= 7 && !head.equals(myurl.substring(0, 7))) {
                myurl = head + inUrl;/* 加上http:// 注意判断已有字符串长度，不要越界 */
            }
            URL url = new URL(myurl);
            /* 选择使用 HttpURLConnection */
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000/* 毫秒 */);
            conn.setConnectTimeout(15000/* 毫秒 */);
            conn.setDoInput(true);
            int response = conn.getResponseCode();/* http状态码 */
            Log.d("rust", "The response is " + response);
            if (response >= 200 && response <= 206) {/* 连接正常 */
                is = conn.getInputStream();
                String contentAsString = readIt(is, len);/* 转换为字符串 */
                return contentAsString;
            } else {
                return response + " ERROR";/* 状态异常时报错 */
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "ERROR: " + myurl;
        } finally {
            /* 确保关闭输入流 */
            if (is != null)
                is.close();
        }
    }

    /**
     * 将输入流转换为字符串
     */
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char buffer[] = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
}
