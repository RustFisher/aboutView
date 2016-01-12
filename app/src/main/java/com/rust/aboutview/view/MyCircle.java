package com.rust.aboutview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

//        DisplayMetrics metrics = new DisplayMetrics();
//        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);

// 屏幕的分辨率
//        int width = metrics.widthPixels;
//        int height = metrics.heightPixels;
//        setContentView(new MyCircle(this, width, height));
public class MyCircle extends View {

    private Context context;

    /**
     * 屏幕的宽
     */
    private int width;

    /**
     * 屏幕的高
     */
    private int height;

    /**
     * 颜色区分区域
     */
    private int[] colors = new int[]{Color.BLACK, Color.BLUE, Color.CYAN,
            Color.GREEN, Color.GRAY, Color.MAGENTA, Color.RED, Color.LTGRAY};
    private String[] colorStrs = new String[]{
            "黑色", "蓝色", "青绿色", "绿色", "灰色", "洋红色", "红色", "浅灰色"};

    /**
     * 大园半径
     */
    private float bigR;

    /**
     * 小圆半径
     */
    private float litterR;

    /**
     * 屏幕中间点的X坐标
     */
    private float centerX;

    /**
     * 屏幕中间点的Y坐标
     */
    private float centerY;

    public MyCircle(Context context, int width, int height) {
        super(context);
        this.context = context;
        this.width = width;
        this.height = height;
        setFocusable(true);

        System.out.println("width=" + width + "<---->height=" + height);
        // 设置两个圆的半径
        bigR = (width - 20) / 2;
        litterR = bigR / 2;

        centerX = width / 2;
        centerY = height / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 画背景颜色
        Paint bg = new Paint();
        bg.setColor(Color.WHITE);
        Rect bgR = new Rect(0, 0, width, height);
        canvas.drawRect(bgR, bg);

        float start = 0F;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        for (int i = 0; i < 4; i++) {
            //注意一定要先画大圆，再画小圆，不然看不到效果，小圆在下面会被大圆覆盖
            // 画大圆
            RectF bigOval = new RectF(centerX - bigR, centerY - bigR,
                    centerX + bigR, centerY + bigR);
            paint.setColor(colors[i]);
            canvas.drawArc(bigOval, start, 90, true, paint);

            // 画小圆
            RectF litterOval = new RectF(centerX - litterR, centerY - litterR,
                    centerX + litterR, centerY + litterR);
            paint.setColor(colors[i + 2]);
            canvas.drawArc(litterOval, start, 90, true, paint);

            start += 90F;
        }

        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 获取点击屏幕时的点的坐标
        float x = event.getX();
        float y = event.getY();
        whichCircle(x, y);
        return super.onTouchEvent(event);
    }

    /**
     * 确定点击的点在哪个圆内
     *
     * @param x
     * @param y
     */
    private void whichCircle(float x, float y) {
        // 将屏幕中的点转换成以屏幕中心为原点的坐标点
        float mx = x - centerX;
        float my = y - centerY;
        float result = mx * mx + my * my;

        StringBuilder tip = new StringBuilder();
        tip.append("您点击了");
        // 高中的解析几何
        if (result <= litterR * litterR) {// 点击的点在小圆内
            tip.append("小圆的");
            tip.append(colorStrs[whichZone(mx, my) + 2]);
            tip.append("区域");
        } else if (result <= bigR * bigR) {// 点击的点在大圆内
            tip.append("大圆的");
            tip.append(colorStrs[whichZone(mx, my)]);
            tip.append("区域");
        } else {// 点不在作作区域
            tip.append("作用区域以外的区域");
        }

        Toast.makeText(context, tip, Toast.LENGTH_SHORT).show();
    }

    /**
     * 判断点击了圆的哪个区域
     *
     * @param x
     * @param y
     * @return
     */
    private int whichZone(float x, float y) {
        // 简单的象限点处理
        // 第一象限在右下角，第二象限在左下角，代数里面的是逆时针，这里是顺时针
        if (x > 0 && y > 0) {
            return 0;
        } else if (x > 0 && y < 0) {
            return 3;
        } else if (x < 0 && y < 0) {
            return 2;
        } else if (x < 0 && y > 0) {
            return 1;
        }

        return -1;
    }

}