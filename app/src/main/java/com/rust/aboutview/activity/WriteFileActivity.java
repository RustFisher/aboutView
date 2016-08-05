package com.rust.aboutview.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.rust.aboutview.R;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WriteFileActivity extends Activity {

    private static final String FILE_DIR = "AboutView";
    private static final String TAG = WriteFileActivity.class.getSimpleName();

    private EditText mEt1;
    private EditText mEt2;
    private EditText mEt3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_file);
        mEt1 = (EditText) findViewById(R.id.et_num_1);
        mEt2 = (EditText) findViewById(R.id.et_num_2);
        mEt3 = (EditText) findViewById(R.id.et_num_3);

        findViewById(R.id.write_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num1 = 0;
                int num2 = 0;
                int num3 = 0;
                if (!TextUtils.isEmpty(mEt1.getText().toString())) {
                    num1 = Integer.valueOf(mEt1.getText().toString());
                }
                if (!TextUtils.isEmpty(mEt2.getText().toString())) {
                    num2 = Integer.valueOf(mEt2.getText().toString());
                }
                if (!TextUtils.isEmpty(mEt3.getText().toString())) {
                    num3 = Integer.valueOf(mEt3.getText().toString());
                }
                new SaveHexFileThread(num1, num2, num3).start();
            }
        });
    }

    class SaveHexFileThread extends Thread {
        private FileWriter mmFileWriter = null;
        private DateFormat dateFormat;
        private String mmFileName;
        private int mmValue1;
        private int mmValue2;
        private int mmValue3;

        public SaveHexFileThread(int v1, int v2, int v3) {
            this.mmValue1 = v1;
            this.mmValue2 = v2;
            this.mmValue3 = v3;
        }

        public void saveData() {
            createFiles();
            try {
                DataOutputStream os = new DataOutputStream(new FileOutputStream(mmFileName));
                // 获取现有文件，如果没有则创建一个；用流来写文件，即可写入二进制数据
                os.writeInt(mmValue1);
                os.writeInt(mmValue2);
                os.writeInt(mmValue3);
                os.writeInt(Integer.MAX_VALUE);
                os.writeInt(Integer.MIN_VALUE);
                os.close();
            } catch (IOException ioe) {
                Log.e(TAG, "write data error ", ioe);
            }
        }

        private void createFiles() {
            File dirSDCard = Environment.getExternalStorageDirectory();
            dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.CHINA);
            File fileDir = new File(dirSDCard.getAbsolutePath() + File.separator + FILE_DIR + File.separator + "data");
            String currentTime = dateFormat.format(new Date(System.currentTimeMillis()));
            boolean hasDir = fileDir.exists();
            if (!hasDir) {
                hasDir = fileDir.mkdirs();// 别忘了声明权限
            }
            Log.d(TAG, "createFiles: " + hasDir);
            mmFileName = fileDir.getAbsolutePath() + File.separator + "data" + currentTime + ".dat";
        }

        @Override
        public void run() {
            super.run();
            saveData();
        }
    }

}
