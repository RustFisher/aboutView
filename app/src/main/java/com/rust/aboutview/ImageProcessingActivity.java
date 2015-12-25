package com.rust.aboutview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageProcessingActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView originView;
    ImageView effectView;
    TextView effectTextView;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button button5;
    Button button6;

    float ratio;
    float alpha;
    float beta;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image_processing);
        initUI();
        originView = (ImageView) findViewById(R.id.origin_image);
        effectView = (ImageView) findViewById(R.id.effect_image);
        Bitmap originBM =
                BitmapFactory.decodeResource(getResources(), R.drawable.littleboygreen_x128);
        originView.setImageBitmap(originBM);
        ratio = 0.1f;
        alpha = Float.MAX_VALUE - 16;
        beta = 360;
    }

    private void initUI() {
        effectTextView = (TextView) findViewById(R.id.effect_tv);
        button1 = (Button) findViewById(R.id.btn1);
        button2 = (Button) findViewById(R.id.btn2);
        button3 = (Button) findViewById(R.id.btn3);
        button4 = (Button) findViewById(R.id.btn4);
        button5 = (Button) findViewById(R.id.btn5);
        button6 = (Button) findViewById(R.id.btn6);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
    }

    /**
     * 根据给定的宽和高进行拉伸
     *
     * @param origin    原图
     * @param newWidth  新图的宽
     * @param newHeight 新图的高
     * @return new Bitmap
     */
    private Bitmap scaleBitmap(Bitmap origin, int newWidth, int newHeight) {
        if (origin == null) {
            return null;
        }
        int height = origin.getHeight();
        int width = origin.getWidth();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (!origin.isRecycled()) {
            origin.recycle();
        }
        return newBM;
    }

    /**
     * 按比例缩放图片
     *
     * @param origin 原图
     * @param ratio  比例
     * @return 新的bitmap
     */
    private Bitmap scaleBitmap(Bitmap origin, float ratio) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(ratio, ratio);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }

    /**
     * 裁剪
     *
     * @param bitmap 原图
     * @return 裁剪后的图像
     */
    private Bitmap cropBitmap(Bitmap bitmap) {
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();
        int cropWidth = w >= h ? h : w;// 裁切后所取的正方形区域边长
        cropWidth /= 2;
        int cropHeight = (int) (cropWidth / 1.2);
        return Bitmap.createBitmap(bitmap, w / 3, 0, cropWidth, cropHeight, null, false);
    }

    /**
     * 选择变换
     *
     * @param origin 原图
     * @param alpha  旋转角度，可正可负
     * @return 旋转后的图片
     */
    private Bitmap rotateBitmap(Bitmap origin, float alpha) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate(alpha);
        // 围绕原地进行旋转
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }

    /**
     * 偏移效果
     * @param origin 原图
     * @return 偏移后的bitmap
     */
    private Bitmap skewBitmap(Bitmap origin) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.postSkew(-0.6f, -0.3f);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }

    @Override
    public void onClick(View v) {
        Bitmap originBM = BitmapFactory.decodeResource(getResources(),
                R.drawable.littleboygreen_x128);
        switch (v.getId()) {
            case R.id.btn1: {
                effectTextView.setText(R.string.scale);
                Bitmap nBM = scaleBitmap(originBM, 100, 72);
                effectView.setImageBitmap(nBM);
                break;
            }
            case R.id.btn2: {
                effectTextView.setText(R.string.scale_ratio);
                if (ratio < 3) {
                    ratio += 0.05f;
                } else {
                    ratio = 0.1f;
                }
                Bitmap nBM = scaleBitmap(originBM, ratio);
                effectView.setImageBitmap(nBM);
                break;
            }
            case R.id.btn3: {
                effectTextView.setText("剪个头");
                Bitmap cropBitmap = cropBitmap(originBM);
                effectView.setImageBitmap(cropBitmap);
                break;
            }
            case R.id.btn4: {
                if (alpha < 345) {
                    alpha += 15;
                } else {
                    alpha = 0;
                }
                effectTextView.setText("旋转");
                Bitmap rotateBitmap = rotateBitmap(originBM, alpha);
                effectView.setImageBitmap(rotateBitmap);
                break;
            }
            case R.id.btn5: {
                if (beta > 15) {
                    beta -= 15;
                } else {
                    beta = 360;
                }
                effectTextView.setText("旋转");
                Bitmap rotateBitmap = rotateBitmap(originBM, beta);
                effectView.setImageBitmap(rotateBitmap);
                break;
            }
            case R.id.btn6: {
                Bitmap skewBM = skewBitmap(originBM);
                effectView.setImageBitmap(skewBM);
                break;
            }
        }
    }
}
