package com.rust.aboutview.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.rust.aboutview.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class ReadWriteCSVActivity extends Activity {

    private static final String TAG = "RustApp";
    private static final String FILE_FOLDER = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "AboutView" + File.separator + "data";
    private static final String FILE_CSV = "about_data.csv";

    private TextView mCSVTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_csv);
        mCSVTv = (TextView) findViewById(R.id.csvContentTv);
        findViewById(R.id.readCSVBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ReadCSVThread(FILE_FOLDER, FILE_CSV).start();
            }
        });
        findViewById(R.id.writeCSVBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new WriteData2CSVThread(
                        new short[]{1, 2, 3, 4, -5, 6, 7, 8, 9, 0, -9, -8, 7, -6, 5, -4, 3, -2, -1},
                        FILE_FOLDER, FILE_CSV).start();
            }
        });
    }

    class WriteData2CSVThread extends Thread {

        short[] data;
        String fileName;
        String folder;
        StringBuilder sb;

        public WriteData2CSVThread(short[] data, String folder, String fileName) {
            this.data = data;
            this.folder = folder;
            this.fileName = fileName;
        }

        private void createFolder() {
            File fileDir = new File(folder);
            boolean hasDir = fileDir.exists();
            if (!hasDir) {
                fileDir.mkdirs();// 这里创建的是目录
            }
        }

        @Override
        public void run() {
            super.run();
            createFolder();
            File eFile = new File(folder + File.separator + fileName);
            if (!eFile.exists()) {
                try {
                    boolean newFile = eFile.createNewFile();
                    Log.e(TAG, "创建新文件 " + eFile.getName() + " " + newFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                FileOutputStream os = new FileOutputStream(eFile, true);
                sb = new StringBuilder();
                for (int i = 0; i < data.length; i++) {
                    sb.append(data[i]).append(",");
                }
                sb.append("\n");
                os.write(sb.toString().getBytes());
                os.flush();
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class ReadCSVThread extends Thread {

        String fileName;
        String folder;

        public ReadCSVThread(String folder, String fileName) {
            this.folder = folder;
            this.fileName = fileName;
        }

        @Override
        public void run() {
            super.run();
            File inFile = new File(folder + File.separator + fileName);
            final StringBuilder cSb = new StringBuilder();
            String inString;
            try {
                BufferedReader reader = new BufferedReader(new FileReader(inFile));
                while ((inString = reader.readLine()) != null) {
                    cSb.append(inString).append("\n");
                }
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mCSVTv.setText(cSb.toString());
                }
            });
        }

    }
}
