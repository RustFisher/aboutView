package com.rust.aboutview.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.rust.aboutview.R;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class WriteHexFileActivity extends Activity implements View.OnClickListener {

    private static final String TAG = WriteHexFileActivity.class.getSimpleName();

    // 用什么方式写的 就用什么方式读
    enum HEX_TYPE {
        BYTE("byte"), INT("int"), FLOAT("float"), DOUBLE("double");

        String comment;
        HEX_TYPE(String s) {
            this.comment = s ;
        }

        @Override
        public String toString() {
            return comment;
        }
    }

    private static String mIntFileName = "about_data_int.dat";// 每种文件只接受一种写文件方式
    private static String mByteFileName = "about_data_byte.dat";
    private static String mFloatFileName = "about_data_float.bin";
    private static String mDoubleFileName = "about_data_double.bin";

    private EditText mEt1;
    private EditText mEt2;
    private EditText mEt3;
    private TextView mContentIntTv;
    private TextView mContentByteTv;
    private TextView mContentFloatTv;
    private TextView mContentDoubleTv;
    private String mFileDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_file);
        mEt1 = (EditText) findViewById(R.id.et_num_1);
        mEt2 = (EditText) findViewById(R.id.et_num_2);
        mEt3 = (EditText) findViewById(R.id.et_num_3);
        mContentIntTv = (TextView) findViewById(R.id.contentIntTv);
        mContentByteTv = (TextView) findViewById(R.id.contentByteTv);
        mContentFloatTv = (TextView) findViewById(R.id.contentFloatTv);
        mContentDoubleTv = (TextView) findViewById(R.id.contentDoubleTv);

        mFileDir = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "AboutView" + File.separator + "data";

        findViewById(R.id.writeIntBtn).setOnClickListener(this);
        findViewById(R.id.writeByteBtn).setOnClickListener(this);
        findViewById(R.id.writeFloatBtn).setOnClickListener(this);
        findViewById(R.id.writeDoubleBtn).setOnClickListener(this);
        findViewById(R.id.readBtn).setOnClickListener(this);
        findViewById(R.id.deleteBtn).setOnClickListener(this);

        new ReadAllHexThread(mFileDir).start();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.writeByteBtn:
                writeHex2File(HEX_TYPE.BYTE);
                break;
            case R.id.writeIntBtn:
                writeHex2File(HEX_TYPE.INT);
                break;
            case R.id.writeFloatBtn:
                writeHex2File(HEX_TYPE.FLOAT);
                break;
            case R.id.writeDoubleBtn:
                writeHex2File(HEX_TYPE.DOUBLE);
                break;
            case R.id.readBtn:
                new ReadAllHexThread(mFileDir).start();
                break;
            case R.id.deleteBtn:
                File fileInt = new File(mFileDir + File.separator + mIntFileName);
                File fileByte = new File(mFileDir + File.separator + mByteFileName);
                File fileFloat = new File(mFileDir + File.separator + mFloatFileName);
                File fileDouble = new File(mFileDir + File.separator + mDoubleFileName);
                fileInt.delete();
                fileByte.delete();
                fileFloat.delete();
                fileDouble.delete();
                new ReadAllHexThread(mFileDir).start();

                break;
        }
    }

    private void writeHex2File(HEX_TYPE type) {
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
        switch (type) {
            case BYTE:
                new SaveHexFileThread(type, num1, num2, num3,
                        mFileDir, mByteFileName).start();
                break;
            case INT:
                new SaveHexFileThread(type, num1, num2, num3,
                        mFileDir, mIntFileName).start();
                break;
            case FLOAT:
                new SaveHexFileThread(type, num1, num2, num3,
                        mFileDir, mFloatFileName).start();
                break;
            case DOUBLE:
                new SaveHexFileThread(type, num1, num2, num3,
                        mFileDir, mDoubleFileName).start();
                break;
        }
    }

    class SaveHexFileThread extends Thread {
        private int mmValue1;
        private int mmValue2;
        private int mmValue3;
        private String mmDir;
        private String mmName;
        private HEX_TYPE type;

        // 文件路径和文件名分开传送
        public SaveHexFileThread(HEX_TYPE type, int v1, int v2, int v3,
                                 String dir, String name) {
            this.mmValue1 = v1;
            this.mmValue2 = v2;
            this.mmValue3 = v3;
            mmDir = dir;
            mmName = name;
            this.type = type;
        }

        public void saveData() {
            try {
                File file = new File(mmDir + File.separator + mmName);
                if (!file.exists()) {
                    boolean newFile = file.createNewFile();
                    Log.d(TAG, "创建新文件 " + file.getName() + " " + newFile);
                }
                DataOutputStream os = new DataOutputStream(new FileOutputStream(file, true));
                switch (type) {
                    case BYTE:
                        os.writeByte(mmValue1);
                        os.writeByte(mmValue2);
                        os.writeByte(mmValue3);
                        break;
                    case INT:
                        os.writeInt(mmValue1);
                        os.writeInt(mmValue2);
                        os.writeInt(mmValue3);
                        break;
                    case FLOAT:
                        os.writeFloat(mmValue1);
                        os.writeFloat(mmValue2);
                        os.writeFloat(mmValue3);
                        break;
                    case DOUBLE:
                        os.writeDouble(mmValue1);
                        os.writeDouble(mmValue2);
                        os.writeDouble(mmValue3);
                        break;
                }
                os.close();
            } catch (IOException ioe) {
                Log.e(TAG, "write data error ", ioe);
            }
        }

        private void createFiles() {
            File fileDir = new File(mmDir);
            boolean hasDir = fileDir.exists();
            if (!hasDir) {
                fileDir.mkdirs();// 这里创建的是目录
            }
        }

        @Override
        public void run() {
            super.run();
            createFiles();
            saveData();
            new ReadAllHexThread(mmDir).start();
        }
    }

    class ReadAllHexThread extends Thread {

        private String mmDir;
        private byte[] dataBuffer = new byte[1024];

        public ReadAllHexThread(String dir) {
            mmDir = dir;
        }

        @Override
        public void run() {
            readHexFile(HEX_TYPE.INT);
            readHexFile(HEX_TYPE.BYTE);
            readHexFile(HEX_TYPE.FLOAT);
            readHexFile(HEX_TYPE.DOUBLE);
        }

        private void readHexFile(final HEX_TYPE type) {
            String fileName = mByteFileName;
            switch (type) {
                case BYTE:
                    fileName = mByteFileName;
                    break;
                case INT:
                    fileName = mIntFileName;
                    break;
                case FLOAT:
                    fileName = mFloatFileName;
                    break;
                case DOUBLE:
                    fileName = mDoubleFileName;
                    break;
            }
            File file = new File(mmDir + File.separator + fileName);
            try {
                if (file.exists()) {
                    DataInputStream is = new DataInputStream(new FileInputStream(file));
                    int readRes = is.read(dataBuffer);
                    StringBuilder contentBuilder = new StringBuilder(type.toString());
                    contentBuilder.append(": \n");
                    for (int i = 0; i < readRes; i++) {
                        contentBuilder.append(Integer.toHexString(dataBuffer[i] & 0xff));
                        contentBuilder.append(", ");
                    }
                    final String finalContent = contentBuilder.toString();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            switch (type) {
                                case BYTE:
                                    mContentByteTv.setText(finalContent);
                                    break;
                                case INT:
                                    mContentIntTv.setText(finalContent);
                                    break;
                                case FLOAT:
                                    mContentFloatTv.setText(finalContent);
                                    break;
                                case DOUBLE:
                                    mContentDoubleTv.setText(finalContent);
                                    break;
                            }
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            switch (type) {
                                case BYTE:
                                    mContentByteTv.setText("No byte");
                                    break;
                                case INT:
                                    mContentIntTv.setText("No int");
                                    break;
                                case FLOAT:
                                    mContentFloatTv.setText("No float");
                                    break;
                                case DOUBLE:
                                    mContentDoubleTv.setText("No double");
                                    break;
                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
