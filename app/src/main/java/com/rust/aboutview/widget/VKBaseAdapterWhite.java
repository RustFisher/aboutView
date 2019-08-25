package com.rust.aboutview.widget;

import android.graphics.Color;
import android.util.Log;

import com.rust.aboutview.R;
import com.rust.aboutview.widget.vkeyboard.VKey;
import com.rust.aboutview.widget.vkeyboard.VKeyboard;
import com.rust.aboutview.widget.vkeyboard.VKeyboardBody;
import com.rust.aboutview.widget.vkeyboard.VRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Sample adapter. White theme.
 * Created on 2019-8-25
 */
public class VKBaseAdapterWhite extends VKeyboard.Adapter {
    private static final String TAG = "rustAppVKeyboard";
    private static final int DEF_SCREEN_WID = 1080;
    private List<VRow> rows;

    private VKeyboardBody vKeyboardBody = new VKeyboardBody(Color.parseColor("#66eaeaea"));

    public VKBaseAdapterWhite() {
        initKeys(DEF_SCREEN_WID);
    }

    public VKBaseAdapterWhite(int screenWidth) {
        initKeys(screenWidth);
    }

    private void initKeys(int screenWid) {
        Log.d(TAG, "initKeys: screen width: " + screenWid);
        List<VKey> line1Keys = Arrays.asList(
                VKey.normal("q", 'q'), VKey.normal("w", 'w'), VKey.normal("e", 'e'),
                VKey.normal("r", 'r'), VKey.normal("t", 't'), VKey.normal("y", 'y'),
                VKey.normal("u", 'u'), VKey.normal("i", 'i'), VKey.normal("o", 'o'), VKey.normal("p", 'p')
        );
        List<VKey> line2Keys = Arrays.asList(
                VKey.normal("a", 'a'), VKey.normal("s", 's'), VKey.normal("d", 'd'),
                VKey.normal("f", 'f'), VKey.normal("g", 'g'), VKey.normal("h", 'h'),
                VKey.normal("j", 'j'), VKey.normal("k", 'k'), VKey.normal("l", 'l'),
                VKey.backspace()
        );
        List<VKey> line3Keys = Arrays.asList(
                VKey.emptyNormal(), VKey.normal("z", 'z'), VKey.normal("x", 'x'), VKey.normal("c", 'c'),
                VKey.normal("v", 'v'), VKey.normal("b", 'b'), VKey.normal("n", 'n'),
                VKey.normal("m", 'm'), VKey.ok()
        );

        int line1Count = line1Keys.size();
        final float keyWidDp = px2Dp((screenWid * 1f) / line1Count) - 8;
        Log.d(TAG, "initKeys: key width(dp): " + keyWidDp);
        rows = new ArrayList<>();
        VRow row1 = new VRow(line1Keys);
        VRow row2 = new VRow(line2Keys);
        VRow row3 = new VRow(line3Keys);
        row1.setPaddingTop(5);
        row2.setPaddingTop(5);
        row3.setPaddingTop(5);
        row3.setPaddingBottom(5);
        rows.add(row1);
        rows.add(row2);
        rows.add(row3);
        for (VRow row : rows) {
            for (VKey key : row.getKeys()) {
                key.setTextColor(Color.BLACK);
                key.setKeyViewWidthDp(keyWidDp);
                key.setUseBgRes(true);
                key.setBackgroundResId(R.drawable.vk_se_tv_1);
                if (key.isOk()) {
                    key.setKeyViewWidthDp(keyWidDp);
                    key.setKeyViewWidthDp((int) (keyWidDp * 2f));
                } else if (key.isBackSpace()) {
                    key.setTextSizeSp(11);
                }
            }
        }

    }

    @Override
    public List<VRow> getRows() {
        return rows;
    }

    @Override
    public VKeyboardBody getKeyboardBody() {
        return vKeyboardBody;
    }
}
